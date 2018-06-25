package org.jabref.ASV;

import org.jabref.logic.exporter.BibTeXMLExporter;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.BibtexEntryTypes;
import org.jabref.model.entry.FieldName;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.TemporaryFolder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BibTeXMLExporterTest {

    private BibTeXMLExporter exportFormat;
    private BibDatabaseContext databaseContext;
    private Path file;
    private Charset charset;
    private BibEntry entry;
    private BibEntry entry1;
    private BibEntry entry2;
    private BibEntry entry3;
    private BibEntry entry4;
    private BibEntry entry5;
    private BibEntry entry6;
    private BibEntry entry7;
    private BibEntry entry8;
    private BibEntry entry9;
    private BibEntry entry10;
    private BibEntry entry11;
    private BibEntry entry12;
    private BibEntry entry13;
    private BibEntry entry14;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        charset = StandardCharsets.UTF_8;
        databaseContext = new BibDatabaseContext();
        exportFormat = new BibTeXMLExporter();
        file = testFolder.newFile().toPath();
        entry = new BibEntry();
        entry1 = new BibEntry();
        entry2 = new BibEntry();
        entry3 = new BibEntry();
        entry4 = new BibEntry();
        entry5 = new BibEntry();
        entry6 = new BibEntry();
        entry7 = new BibEntry();
        entry8 = new BibEntry();
        entry9 = new BibEntry();
        entry10 = new BibEntry();
        entry11 = new BibEntry();
        entry12 = new BibEntry();
        entry13 = new BibEntry();
        entry14 = new BibEntry();
    }

    @Test
    public void exportBibteXML() throws Exception {
        entry.setType("book");
        entry1.setType("inbook");
        entry2.setType("booklet");
        entry3.setType("conference");
        entry4.setType("incollection");
        entry5.setType("mastersthesis");
        entry6.setType("manual");
        entry7.setType("misc");
        entry8.setType("phdthesis");
        entry9.setType("proceedings");
        entry10.setType("techreport");
        entry11.setType("unpublished");
        entry12.setType("asdasd");
        entry13.setType("article");
        entry14.setType("inproceedings");

        List<BibEntry> entries = Arrays.asList(entry, entry1, entry2, entry3, entry4,
                entry5, entry6, entry7, entry8, entry9, entry10, entry11, entry12, entry13, entry14);
        exportFormat.export(databaseContext, file, charset, entries);
        List<String> lines = Files.readAllLines(file);
        assertEquals(20, lines.size());
    }

    @Test
    public void exportArticle() throws Exception {
        entry.setType(BibtexEntryTypes.ARTICLE);
        entry.setField(FieldName.AUTHOR, "Wrezj Kebabdji");
        entry.setField(FieldName.TITLE, "Hello world");
        entry.setField(FieldName.JOURNAL, "Java");
        entry.setField(FieldName.YEAR, "2018");
        entry.setField(FieldName.NUMBER, "1");

        exportFormat.export(databaseContext, file, charset, Collections.singletonList(entry));
        List<String> lines = Files.readAllLines(file);

        Assertions.assertTrue(lines.get(3).contains("<article>"));
        Assertions.assertTrue(lines.get(4).contains("<author>Wrezj Kebabdji</author>"));
        Assertions.assertTrue(lines.get(5).contains("<title>Hello world</title>"));
        Assertions.assertTrue(lines.get(6).contains("<journal>Java</journal>"));
        Assertions.assertTrue(lines.get(7).contains("<year>2018</year>"));
        Assertions.assertTrue(lines.get(8).contains("<number>1</number>"));
        Assertions.assertTrue(lines.get(9).contains("</article>"));

    }

    @Test
    public void exportBook() throws Exception {
        entry.setType(BibtexEntryTypes.BOOK);
        entry.setField(FieldName.AUTHOR, "Wrezj Kebabdji");
        entry.setField(FieldName.TITLE, "Hello world");
        entry.setField(FieldName.PUBLISHER, "Oracle");
        entry.setField(FieldName.YEAR, "2018");

        exportFormat.export(databaseContext, file, charset, Collections.singletonList(entry));
        List<String> lines = Files.readAllLines(file);

        Assertions.assertTrue(lines.get(3).contains("<book>"));
        Assertions.assertTrue(lines.get(4).contains("<author>Wrezj Kebabdji</author>"));
        Assertions.assertTrue(lines.get(5).contains("<title>Hello world</title>"));
        Assertions.assertTrue(lines.get(6).contains("<publisher>Oracle</publisher>"));
        Assertions.assertTrue(lines.get(7).contains("<year>2018</year>"));
        Assertions.assertTrue(lines.get(8).contains("</book>"));

    }

    @Test
    public void exportBooklet() throws Exception {
        entry.setType(BibtexEntryTypes.BOOKLET);
        entry.setField(FieldName.TITLE, "Hello world");

        exportFormat.export(databaseContext, file, charset, Collections.singletonList(entry));
        List<String> lines = Files.readAllLines(file);

        Assertions.assertTrue(lines.get(3).contains("<booklet>"));
        Assertions.assertTrue(lines.get(4).contains("<title>Hello world</title>"));
        Assertions.assertTrue(lines.get(5).contains("</booklet>"));
    }


    @Test
    public void exportInBook() throws Exception {
        entry.setType(BibtexEntryTypes.INBOOK);
        entry.setField(FieldName.CHAPTER, "11");
        entry.setField(FieldName.PAGES, "123");
        entry.setField(FieldName.TITLE, "Hello world");
        entry.setField(FieldName.PUBLISHER, "Oracle");
        entry.setField(FieldName.AUTHOR, "Wrezj Kebabdji");
        entry.setField(FieldName.YEAR, "2018");

        exportFormat.export(databaseContext, file, charset, Collections.singletonList(entry));
        List<String> lines = Files.readAllLines(file);

        Assertions.assertTrue(lines.get(3).contains("<inbook>"));
        Assertions.assertTrue(lines.get(4).contains("<chapter>11</chapter>"));
        Assertions.assertTrue(lines.get(5).contains("<pages>123</pages>"));
        Assertions.assertTrue(lines.get(6).contains("<year>2018</year>"));
        Assertions.assertTrue(lines.get(7).contains("<author>Wrezj Kebabdji</author>"));
        Assertions.assertTrue(lines.get(8).contains("<publisher>Oracle</publisher>"));
        Assertions.assertTrue(lines.get(9).contains("<title>Hello world</title>"));
        Assertions.assertTrue(lines.get(10).contains("</inbook>"));
    }

    @Test
    public void exportInCollection() throws Exception {
        entry.setType(BibtexEntryTypes.INCOLLECTION);
        entry.setField(FieldName.TITLE, "Hello");
        entry.setField(FieldName.BOOKTITLE, "world");
        entry.setField(FieldName.PUBLISHER, "Oracle");
        entry.setField(FieldName.AUTHOR, "Wrezj Kebabdji");
        entry.setField(FieldName.YEAR, "2018");

        exportFormat.export(databaseContext, file, charset, Collections.singletonList(entry));
        List<String> lines = Files.readAllLines(file);

        System.out.println(lines.toString());

        Assertions.assertTrue(lines.get(3).contains("<incollection>"));
        Assertions.assertTrue(lines.get(4).contains("<author>Wrezj Kebabdji</author>"));
        Assertions.assertTrue(lines.get(5).contains("<title>Hello</title>"));
        Assertions.assertTrue(lines.get(6).contains("<booktitle>world</booktitle>"));
        Assertions.assertTrue(lines.get(7).contains("<publisher>Oracle</publisher>"));
        Assertions.assertTrue(lines.get(8).contains("<year>2018</year>"));
        Assertions.assertTrue(lines.get(9).contains("</incollection>"));
    }

    @Test
    public void exportConference() throws Exception {
        entry.setType(BibtexEntryTypes.CONFERENCE);
        entry.setField(FieldName.TITLE, "Hello");
        entry.setField(FieldName.BOOKTITLE, "world");
        entry.setField(FieldName.AUTHOR, "Wrezj Kebabdji");
        entry.setField(FieldName.YEAR, "2018");

        exportFormat.export(databaseContext, file, charset, Collections.singletonList(entry));
        List<String> lines = Files.readAllLines(file);

        Assertions.assertTrue(lines.get(3).contains("<conference>"));
        Assertions.assertTrue(lines.get(4).contains("<author>Wrezj Kebabdji</author>"));
        Assertions.assertTrue(lines.get(5).contains("<title>Hello</title>"));
        Assertions.assertTrue(lines.get(6).contains("<booktitle>world</booktitle>"));
        Assertions.assertTrue(lines.get(7).contains("<year>2018</year>"));
        Assertions.assertTrue(lines.get(8).contains("</conference>"));
    }

    @Test
    public void exportInProceedings() throws Exception {
        entry.setType(BibtexEntryTypes.INPROCEEDINGS);
        entry.setField(FieldName.TITLE, "Hello");
        entry.setField(FieldName.BOOKTITLE, "world");
        entry.setField(FieldName.AUTHOR, "Wrezj Kebabdji");
        entry.setField(FieldName.YEAR, "2018");

        exportFormat.export(databaseContext, file, charset, Collections.singletonList(entry));
        List<String> lines = Files.readAllLines(file);

        Assertions.assertTrue(lines.get(3).contains("<inproceedings>"));
        Assertions.assertTrue(lines.get(4).contains("<author>Wrezj Kebabdji</author>"));
        Assertions.assertTrue(lines.get(5).contains("<title>Hello</title>"));
        Assertions.assertTrue(lines.get(6).contains("<booktitle>world</booktitle>"));
        Assertions.assertTrue(lines.get(7).contains("<year>2018</year>"));
        Assertions.assertTrue(lines.get(8).contains("</inproceedings>"));
    }

    @Test
    public void exportProceedings() throws Exception {
        entry.setType(BibtexEntryTypes.PROCEEDINGS);
        entry.setField(FieldName.TITLE, "Hello world");
        entry.setField(FieldName.YEAR, "2018");

        exportFormat.export(databaseContext, file, charset, Collections.singletonList(entry));
        List<String> lines = Files.readAllLines(file);

        Assertions.assertTrue(lines.get(3).contains("<proceedings>"));
        Assertions.assertTrue(lines.get(4).contains("<title>Hello world</title>"));
        Assertions.assertTrue(lines.get(5).contains("<year>2018</year>"));
        Assertions.assertTrue(lines.get(6).contains("</proceedings>"));
    }

    @Test
    public void exportManual() throws Exception {
        entry.setType(BibtexEntryTypes.MANUAL);
        entry.setField(FieldName.TITLE, "Hello world");

        exportFormat.export(databaseContext, file, charset, Collections.singletonList(entry));
        List<String> lines = Files.readAllLines(file);

        Assertions.assertTrue(lines.get(3).contains("<manual>"));
        Assertions.assertTrue(lines.get(4).contains("<title>Hello world</title>"));
        Assertions.assertTrue(lines.get(5).contains("</manual>"));
    }

    @Test
    public void exportMastersThesis() throws Exception {
        entry.setType(BibtexEntryTypes.MASTERSTHESIS);
        entry.setField(FieldName.TITLE, "Hello world");
        entry.setField(FieldName.SCHOOL, "HvA");
        entry.setField(FieldName.AUTHOR, "Wrezj Kebabdji");
        entry.setField(FieldName.YEAR, "2018");

        exportFormat.export(databaseContext, file, charset, Collections.singletonList(entry));
        List<String> lines = Files.readAllLines(file);

        System.out.println(lines.toString());

        Assertions.assertTrue(lines.get(3).contains("<mastersthesis>"));
        Assertions.assertTrue(lines.get(4).contains("<author>Wrezj Kebabdji</author>"));
        Assertions.assertTrue(lines.get(5).contains("<title>Hello world</title>"));
        Assertions.assertTrue(lines.get(6).contains("<school>HvA</school>"));
        Assertions.assertTrue(lines.get(7).contains("<year>2018</year>"));
        Assertions.assertTrue(lines.get(8).contains("</mastersthesis>"));
    }


    @Test
    public void exportPhdThesis() throws Exception {
        entry.setType(BibtexEntryTypes.PHDTHESIS);
        entry.setField(FieldName.TITLE, "Hello world");
        entry.setField(FieldName.SCHOOL, "HvA");
        entry.setField(FieldName.AUTHOR, "Wrezj Kebabdji");
        entry.setField(FieldName.YEAR, "2018");

        exportFormat.export(databaseContext, file, charset, Collections.singletonList(entry));
        List<String> lines = Files.readAllLines(file);

        Assertions.assertTrue(lines.get(3).contains("<phdthesis>"));
        Assertions.assertTrue(lines.get(4).contains("<author>Wrezj Kebabdji</author>"));
        Assertions.assertTrue(lines.get(5).contains("<title>Hello world</title>"));
        Assertions.assertTrue(lines.get(6).contains("<school>HvA</school>"));
        Assertions.assertTrue(lines.get(7).contains("<year>2018</year>"));
        Assertions.assertTrue(lines.get(8).contains("</phdthesis>"));
    }

    @Test
    public void exportTechReport() throws Exception {
        entry.setType(BibtexEntryTypes.TECHREPORT);
        entry.setField(FieldName.TITLE, "Hello world");
        entry.setField(FieldName.INSTITUTION, "HvA");
        entry.setField(FieldName.AUTHOR, "Wrezj Kebabdji");
        entry.setField(FieldName.YEAR, "2018");

        exportFormat.export(databaseContext, file, charset, Collections.singletonList(entry));
        List<String> lines = Files.readAllLines(file);

        Assertions.assertTrue(lines.get(3).contains("<techreport>"));
        Assertions.assertTrue(lines.get(4).contains("<author>Wrezj Kebabdji</author>"));
        Assertions.assertTrue(lines.get(5).contains("<title>Hello world</title>"));
        Assertions.assertTrue(lines.get(6).contains("<institution>HvA</institution>"));
        Assertions.assertTrue(lines.get(7).contains("<year>2018</year>"));
        Assertions.assertTrue(lines.get(8).contains("</techreport>"));
    }

    @Test
    public void exportUnpublished() throws Exception {
        entry.setType(BibtexEntryTypes.UNPUBLISHED);
        entry.setField(FieldName.TITLE, "Hello world");
        entry.setField(FieldName.NOTE, "Hello back");
        entry.setField(FieldName.AUTHOR, "Wrezj Kebabdji");

        exportFormat.export(databaseContext, file, charset, Collections.singletonList(entry));
        List<String> lines = Files.readAllLines(file);

        Assertions.assertTrue(lines.get(3).contains("<unpublished>"));
        Assertions.assertTrue(lines.get(4).contains("<author>Wrezj Kebabdji</author>"));
        Assertions.assertTrue(lines.get(5).contains("<title>Hello world</title>"));
        Assertions.assertTrue(lines.get(6).contains("<note>Hello back</note>"));
        Assertions.assertTrue(lines.get(7).contains("</unpublished>"));
    }

}
