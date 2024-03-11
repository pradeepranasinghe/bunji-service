package com.bunji.hackathon.bunjiservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
@RestController
public class BunjiController {

    @GetMapping("/helloworld")
    String hello() {
        return "HelloWorld";
    }

    @GetMapping ("/document")
    ResponseEntity processDocument() throws Exception{
//        //return repository.save(newEmployee);
//        System.out.println(System.getProperty("user.dir"));

        String response = getWikiContentById("65538");
        System.out.println("original "+response);
        String extracted = extractContent(response);
        System.out.println("extracted "+extracted);


        PrefillDocx docx = new PrefillDocx();
        docx.prefill(extracted);

        File file = new File("output.docx");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test.docx");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    private String getWikiContentById(String id){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic cHJhZGVlcC5yYW5hc2luZ2hlQGdtYWlsLmNvbTpBVEFUVDN4RmZHRjA2cUlHX2FmUGJ2WkVaLTRsMzU5ZHV2VGl3NlF4STFSOTZ1bHM5SHpsWGtlNTN6RjdyLWtTTU9ucjE3aGFfWENXRUNVc2VPWjlQdGc5cTRfUkJFNllWOTh1WUVxMnRFNVVrSS05c0s1MjdZRk1jZ1N2cnRMNTQ3TFJxOGh5MERmYTVFZ0lzT3BwQWZCbV9SbHh0WEhDcEJmUngzUGZtbU1KWlA0aG5kRkk5STQ9MDBCMzM1QjE=");
        HttpEntity<String> request = new HttpEntity<String>(headers);

        String fooResourceUrl
                = "https://pradeepranasinghe.atlassian.net/wiki/rest/api/content/"+id+"?expand=body.storage";
//        ResponseEntity<String> response
//                = restTemplate.getForEntity(fooResourceUrl + "/1", String.class);
//
        ResponseEntity<String> response = restTemplate.exchange(fooResourceUrl, HttpMethod.GET, request, String.class);


        return response.getBody();
    }

    private String extractContent(String jsonContent) throws JsonProcessingException {

        String s = Jsoup.parse(jsonContent).text();
        System.out.println(s);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(s);
        JsonNode body = root.get("id");


        String value = root.path("body").path("storage").path("value").asText();

        return value.replaceAll("Project Description ","");
    }



}
