package org.jabref.ASV;

import org.jabref.logic.bibtex.FieldContentParserPreferences;
import org.jabref.logic.importer.FetcherException;
import org.jabref.logic.importer.ImportFormatPreferences;
import org.jabref.logic.importer.fetcher.GoogleScholar;
import org.jabref.logic.net.URLDownload;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;
import org.jabref.testutils.category.FetcherTest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@FetcherTest
public class GoogleScholarTest {

    private GoogleScholar finder;
    private BibEntry entry;
    private Document doc;
    private Elements link;

    @BeforeEach
    void setUp() throws IOException {
        ImportFormatPreferences importFormatPreferences = mock(ImportFormatPreferences.class);
        when(importFormatPreferences.getFieldContentParserPreferences()).thenReturn(
                mock(FieldContentParserPreferences.class));
        finder = new GoogleScholar(importFormatPreferences);

        entry = new BibEntry();
        doc = Jsoup.connect("https://scholar.google.nl/scholar?q=bitcoin&hl=en&as_sdt=0,5")
                .userAgent(URLDownload.USER_AGENT).get();
        for (int i = 0; i < 10; i++) {
            link = doc.select(String.format("div[data-rp=%S] div.gs_or_ggsm a", i));
        }
    }

    @Test
    void findFullText() throws IOException, FetcherException {
        String query = "bitcoin";
        entry.setField(FieldName.TITLE, query);
        if (!link.isEmpty()) {
            assertEquals(Optional.of(new URL(" https://s3.amazonaws.com/academia.edu.documents/32413652/" +
                            "BitCoin_P2P_electronic_cash_system.pdf?AWSAccessKeyId=AKIAIWOWYYGZ2Y53UL3A&Expires=" +
                            "1528903475&Signature=YDk9ah%2F3JJSMIH3Y13pc6xIem0I%3D&response-content-disposition=" +
                            "inline%3B%20filename%3DBitcoin_A_Peer-to-Peer_Electronic_Cash_S.pdf")),
                    finder.findFullText(entry));
        }
    }

    @Test
    void performSearch() throws FetcherException {
        String query = "bitcoin";
        finder.performSearch(query);

        if (!link.isEmpty()) {
            List<BibEntry> foundEntries = finder.performSearch(query);
            assertEquals(Collections.singletonList(entry), foundEntries);
        }
    }
}
