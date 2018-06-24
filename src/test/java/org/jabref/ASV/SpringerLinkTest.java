package org.jabref.ASV;

import org.jabref.logic.importer.fetcher.SpringerLink;
import org.jabref.model.entry.BibEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SpringerLinkTest {

    private SpringerLink finder;
    private BibEntry entry;

    @BeforeEach
    public void setUp() {
        finder = new SpringerLink();
        entry = new BibEntry();
    }

    @Test
    public void doiNotPresent() throws IOException {
        assertEquals(Optional.empty(), finder.findFullText(entry));
    }

    @Test
    public void findByDOI() throws IOException {
        entry.setField("doi", "10.1186/1756-0500-5-514");
        assertEquals(
                Optional.of(new URL("http://link.springer.com/content/pdf/10.1186/1756-0500-5-514.pdf")),
                finder.findFullText(entry));
    }

    @Test
    public void notFoundByDOI() throws IOException {
        entry.setField("doi", "10.1186/unknown-doi");
        assertEquals(Optional.empty(), finder.findFullText(entry));
    }

}
