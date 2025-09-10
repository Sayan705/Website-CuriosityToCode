package org.example;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import com.lowagie.text.Document;
// DocToPdfConverter must exist in your project

@RestController
@RequestMapping("/word")
public class WordToPdfController {
    @PostMapping("/upload")
    public ResponseEntity<byte[]> uploadDoc(@RequestParam("file") MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename != null && originalFilename.toLowerCase().endsWith(".docx") ? ".docx" : ".doc";
        File tempDoc = File.createTempFile("uploaded", ext);
        try (FileOutputStream fos = new FileOutputStream(tempDoc)) {
            fos.write(file.getBytes());
        }
        File tempPdf = File.createTempFile("converted", ".pdf");
        if (ext.equals(".docx")) {
            DocToPdfConverter.convertDocxToPdf(tempDoc.getAbsolutePath(), tempPdf.getAbsolutePath());
        } else {
            DocToPdfConverter.convertDocToPdf(tempDoc.getAbsolutePath(), tempPdf.getAbsolutePath());
        }
        byte[] pdfBytes = Files.readAllBytes(tempPdf.toPath());
        tempDoc.delete();
        tempPdf.delete();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=converted.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
