package com.bunji.hackathon.bunjiservice;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.util.List;

public class PrefillDocx {
    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("user.dir"));
        XWPFDocument doc = new XWPFDocument(OPCPackage.open("test.docx"));
        for (XWPFParagraph p : doc.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null && text.contains("American")) {
                        text = text.replace("American", "haystack");
                        r.setText(text, 0);
                    }
                }
            }
        }
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        for (XWPFRun r : p.getRuns()) {
                            String text = r.getText(0);
                            if (text != null && text.contains("Rules")) {
                                text = text.replace("Rules", "haystack");
                                r.setText(text,0);
                            }
                        }
                    }
                }
            }
        }

        doc.write(new FileOutputStream("output.docx"));
    }

    public void prefill(String content) throws Exception {
        XWPFDocument doc = new XWPFDocument(OPCPackage.open("test.docx"));
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        for (XWPFRun r : p.getRuns()) {
                            String text = r.getText(0);
                            if (text != null && text.contains("Rules")) {
                                text = text.replace("Rules", content);
                                r.setText(text,0);
                            }
                        }
                    }
                }
            }
        }

        doc.write(new FileOutputStream("output.docx"));
    }
}
