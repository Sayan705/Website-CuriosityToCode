package org.example;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
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
}
