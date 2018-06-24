package org.jabref.ASV;

import org.jabref.logic.importer.ImportFormatPreferences;
import org.jabref.logic.importer.fileformat.FreeCiteImporter;
import org.jabref.model.entry.BibEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class FreeCiteImporterTest {

    private FreeCiteImporter importer;

    @BeforeEach
    public void setUp() {
        importer = new FreeCiteImporter(mock(ImportFormatPreferences.class));
    }

    @Test
    public void testImportEntries() {
        String entryText = "Udvarhelyi, I.S., Gatsonis, C.A., Epstein, A.M., Pashos, C.L., Newhouse, J.P. and McNeil, B.J. Acute Myocardial Infarction in the Medicare population: process of care and clinical outcomes. Journal of the American Medical Association, 1992; 18:2530-2536.";
        List<BibEntry> bibEntries = importer.importEntries(entryText).getDatabase().getEntries();
        BibEntry bibEntry = bibEntries.get(0);

        assertEquals(1, bibEntries.size());
        assertEquals(bibEntry.getField("author"), Optional.of("I S Udvarhelyi and C A Gatsonis and A M Epstein and C L Pashos and J P Newhouse and B J McNeil"));
        assertEquals(bibEntry.getField("title"),
                Optional.of("Acute Myocardial Infarction in the Medicare population: process of care and clinical outcomes"));
    }

}
