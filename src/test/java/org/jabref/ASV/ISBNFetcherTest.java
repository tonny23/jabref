package org.jabref.ASV;



import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Optional;

import org.jabref.logic.importer.FetcherException;
import org.jabref.logic.importer.ImportFormatPreferences;
import org.jabref.logic.importer.fetcher.IsbnFetcher;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.BiblatexEntryTypes;
import org.jabref.testutils.category.FetcherTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


public class ISBNFetcherTest {

    private IsbnFetcher fetcher;
    private BibEntry bibEntry;

    @BeforeEach
    public void setUp() {
        fetcher = new IsbnFetcher(mock(ImportFormatPreferences.class, Answers.RETURNS_DEEP_STUBS));

        bibEntry = new BibEntry();
        bibEntry.setType(BiblatexEntryTypes.BOOK);
        bibEntry.setField("bibtexkey", "9781400201945");
        bibEntry.setField("author", "Comm, Joel");
        bibEntry.setField("title", "The Fun Formula: How Curiosity, Risk-Taking, and Serendipity Can Revolutionize How You Work");
        bibEntry.setField("publisher", "NELSONWORD PUB GROUP");
        bibEntry.setField("pagetotal", "240");
        bibEntry.setField("date", "2018-06-05");
        bibEntry.setField("ean", "9781400201945");
        bibEntry.setField("isbn", "1400201942");
        bibEntry.setField("year", "2018");
        bibEntry.setField("url", "https://www.ebook.de/de/product/30871260/joel_comm_the_fun_formula_how_curiosity_risk_taking_and_serendipity_can_revolutionize_how_you_work.html");

    }

    @Test
    public void searchByIsbn() throws FetcherException, MalformedURLException, URISyntaxException {
        Optional<BibEntry> fetchedEntry = fetcher.performSearchById("1400201942");
        assertEquals(Optional.of(bibEntry), fetchedEntry);
        assertNull(fetcher.getURLForID("9781400201945"));
    }
}
