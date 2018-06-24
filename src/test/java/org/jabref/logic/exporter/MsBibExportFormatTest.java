package org.jabref.logic.exporter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.entry.BibEntry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MsBibExportFormatTest {

    public BibDatabaseContext databaseContext;
    public Charset charset;
    public Path tempFile;
    public MSBibExporter msBibExportFormat;
    private BibEntry entry;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();


    @Before
    public void setUp() throws Exception {
        databaseContext = new BibDatabaseContext();
        charset = StandardCharsets.UTF_8;
        msBibExportFormat = new MSBibExporter();
        tempFile = testFolder.newFile().toPath();
        entry = new BibEntry();
    }

    @Test
    public final void testPerformExportWithNoEntry() throws IOException, SaveException {
        List<BibEntry> entries = Collections.emptyList();
        msBibExportFormat.export(databaseContext, tempFile, charset, entries);
        assertEquals(Collections.emptyList(), Files.readAllLines(tempFile));
    }

    @Test
    public final void testPerformExportWithEntry() throws IOException, SaveException {
        entry.setField("author", "Someone, Van Something");
        entry.setType("book");
        List<BibEntry> entries = Arrays.asList(entry);
        msBibExportFormat.export(databaseContext, tempFile, charset, entries);
        List<String> lines = Files.readAllLines(tempFile);

        assertTrue(lines.get(4).contains("Book"));
        assertTrue(lines.get(5).contains("Author"));
        assertTrue(lines.get(9).contains("Someone"));
        assertTrue(lines.get(10).contains("Something"));
        assertTrue(lines.get(11).contains("Van"));
    }
}
