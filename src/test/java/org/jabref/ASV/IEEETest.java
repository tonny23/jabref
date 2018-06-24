package org.jabref.ASV;

import org.jabref.logic.importer.fetcher.IEEE;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;
import org.jabref.support.DisabledOnCIServer;
import org.jabref.testutils.category.FetcherTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@FetcherTest
public class IEEETest {

    private IEEE finder;
    private BibEntry entry;

    @BeforeEach
    void setUp() {
        finder = new IEEE();
        entry = new BibEntry();
    }

    @Test
    void findByURL() throws IOException {
        entry.setField(FieldName.URL, "https://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=8211663");
        Optional<String> urlString = entry.getField(FieldName.URL);
        System.out.println(urlString.get());
        assertEquals(
                Optional.of(
                        new URL("https://ieeexplore.ieee.org/ielx7/8168935/8211524/08211663.pdf?tp=&arnumber=8211663&isnumber=8211524")),
                finder.findFullText(entry));
    }


}
