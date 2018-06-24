package org.jabref.logic.msbib;

import org.jabref.logic.msbib.MSBibConverter;
import org.jabref.logic.msbib.MSBibEntry;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

// Test voor import in MS Word
public class MSBibEntryTest {

    BibEntry entry;
    BibEntry entry1;
    MSBibEntry msBibEntry;
    MSBibEntry msBibEntry1;

    @Before
    public void setup() {
        entry = new BibEntry();
        entry1 = new BibEntry();
        msBibEntry = new MSBibEntry();
        msBibEntry1 = new MSBibEntry();
    }

    @Test
    public void MsBibEntryTypeTest() {
        entry.setField("title", "Dikke paarden");
        entry.setType("Patent");

        msBibEntry = MSBibConverter.convert(entry);

        Assert.assertEquals("Patent", msBibEntry.getType());
    }

    @Test
    public void MsBibEntryNoTypetest(){
        entry1.setField("title","Dode mensen klagen niet");

        msBibEntry1 = MSBibConverter.convert(entry1);

        Assert.assertEquals("Misc",msBibEntry1.getType());
    }
}
