import org.example.DocToPdfConverter;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DocToPdfConverterTest {
    @Test
    public void testConvertDocToPdf() throws Exception {
        // Prepare test DOC file path and output PDF file path
        String docPath = "src/test/resources/Sample.doc";
        String pdfPath = "src/test/resources/test_output.pdf";

        // Ensure the DOC file exists and is a valid Word file
        File docFile = new File(docPath);
        assertTrue(docFile.exists() && docFile.length() > 0, "Sample.doc must be a valid DOC file");

        // Run conversion
        DocToPdfConverter.convertDocToPdf(docPath, pdfPath);

        // Assert PDF file is created
        File pdfFile = new File(pdfPath);
        assertTrue(pdfFile.exists() && pdfFile.length() > 0);

        // Cleanup
        pdfFile.delete();
    }
}
