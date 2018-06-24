package org.jabref.ASV;

import org.jabref.pdfimport.PdfFileFilter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfFileFilterTest {
    PdfFileFilter pdf;

    @Test
    void wrongFileNameTest() {
        String fileName = "wrongFile";
        assertFalse(pdf.accept(fileName));
    }

    @Test
    void wrongExtensiontest() {
        String fileName = "book.ppd";
        assertFalse(pdf.accept(fileName));

    }

    @Test
    void fileNameTest(){
        String fileName = "bin/pdf/test.pdf";
        assertTrue(pdf.accept(fileName));

    }

    @Test
    void extensionTest(){
        String fileName = "book.pdf";
        assertTrue(pdf.accept(fileName));
    }
}
