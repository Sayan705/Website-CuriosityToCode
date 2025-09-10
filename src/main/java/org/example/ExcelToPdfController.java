package org.example;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Chunk;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.util.Iterator;

@RestController
@RequestMapping("/excel")
public class ExcelToPdfController {
    @PostMapping("/upload")
    public ResponseEntity<byte[]> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, pdfOut);
            document.open();
            String filename = file.getOriginalFilename();
            if (filename.endsWith(".csv")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] cells = line.split(",");
                    for (String cell : cells) {
                        document.add(new Phrase(cell + "    "));
                    }
                    document.add(Chunk.NEWLINE);
                }
            } else {
                Workbook workbook = filename.endsWith(".xlsx") ? new XSSFWorkbook(file.getInputStream()) : new HSSFWorkbook(file.getInputStream());
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    document.add(new Paragraph("Sheet: " + sheet.getSheetName()));
                    Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();
                    while (rowIterator.hasNext()) {
                        org.apache.poi.ss.usermodel.Row row = rowIterator.next();
                        Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
                        while (cellIterator.hasNext()) {
                            org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
                            document.add(new Phrase(cell.toString() + "    "));
                        }
                        document.add(Chunk.NEWLINE);
                    }
                }
                workbook.close();
            }
            document.close();
            byte[] pdfBytes = pdfOut.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "excel-converted.pdf");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("Error: " + e.getMessage()).getBytes());
        }
    }
}
