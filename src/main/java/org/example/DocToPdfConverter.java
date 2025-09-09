package org.example;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.*;

public class DocToPdfConverter {
    public static void convertDocToPdf(String docPath, String pdfPath) throws Exception {
        try (FileInputStream fis = new FileInputStream(docPath);
             HWPFDocument doc = new HWPFDocument(fis);
             WordExtractor extractor = new WordExtractor(doc);
             FileOutputStream fos = new FileOutputStream(pdfPath)) {

            Document pdfDoc = new Document();
            PdfWriter.getInstance(pdfDoc, fos);
            pdfDoc.open();

            String[] paragraphs = extractor.getParagraphText();
            for (String para : paragraphs) {
                pdfDoc.add(new Paragraph(para));
            }
            pdfDoc.close();
        }
    }

    public static void convertDocxToPdf(String docxPath, String pdfPath) throws Exception {
        try (FileInputStream fis = new FileInputStream(docxPath);
             XWPFDocument docx = new XWPFDocument(fis);
             FileOutputStream fos = new FileOutputStream(pdfPath)) {

            Document pdfDoc = new Document();
            PdfWriter.getInstance(pdfDoc, fos);
            pdfDoc.open();

            for (org.apache.poi.xwpf.usermodel.XWPFParagraph para : docx.getParagraphs()) {
                pdfDoc.add(new Paragraph(para.getText()));
            }
            pdfDoc.close();
        }
    }
}
