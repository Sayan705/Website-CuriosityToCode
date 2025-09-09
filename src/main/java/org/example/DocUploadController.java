package org.example;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/")
public class DocUploadController {
    @PostMapping(value = "/upload-doc", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> uploadDoc(@RequestParam("file") MultipartFile file) throws Exception {
        // Save uploaded DOC file to temp location
        File tempDoc = File.createTempFile("uploaded", ".doc");
        try (FileOutputStream fos = new FileOutputStream(tempDoc)) {
            fos.write(file.getBytes());
        }
        // Convert DOC to PDF
        File tempPdf = File.createTempFile("converted", ".pdf");
        DocToPdfConverter.convertDocToPdf(tempDoc.getAbsolutePath(), tempPdf.getAbsolutePath());
        byte[] pdfBytes = java.nio.file.Files.readAllBytes(tempPdf.toPath());
        // Clean up temp files
        tempDoc.delete();
        tempPdf.delete();
        // Return PDF as download
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=converted.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}

