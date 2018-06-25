package org.jabref.gui.exporter;

import javafx.stage.FileChooser;
import org.jabref.Globals;
import org.jabref.gui.JabRefFrame;
import org.jabref.gui.util.FileFilterConverter;
import org.jabref.logic.exporter.Exporter;
import org.jabref.logic.util.io.FileUtil;
import org.jabref.model.entry.BibEntry;
import org.jabref.preferences.JabRefPreferences;

import java.nio.file.Path;
import java.util.List;
import org.slf4j.Logger;


public class ExporterUtil {
    private static Exporter format;
    private static List<BibEntry> entries;
    private static String errorMessage;

    public static void exporter(Path file, FileChooser.ExtensionFilter selectedExtensionFilter, List<Exporter> exporters,
                         boolean selectedOnly, JabRefFrame frame){
        String selectedExtension = selectedExtensionFilter.getExtensions().get(0).replace("*", "");
        if (!file.endsWith(selectedExtension)) {
            FileUtil.addExtension(file, selectedExtension);
        }
        format = FileFilterConverter.getExporter(selectedExtensionFilter, exporters).orElseThrow(() -> new IllegalStateException("User didn't selected a file type for the extension"));
        if (selectedOnly) {
            // Selected entries
            entries = frame.getCurrentBasePanel().getSelectedEntries();
        } else {
            // All entries
            entries = frame.getCurrentBasePanel().getDatabase().getEntries();
        }
        // Set the global variable for this database's file directory before exporting,
        // so formatters can resolve linked files correctly.
        // (This is an ugly hack!)
        Globals.prefs.fileDirForDatabase = frame.getCurrentBasePanel()
                .getBibDatabaseContext()
                .getFileDirectories(Globals.prefs.getFileDirectoryPreferences());

        // Make sure we remember which filter was used, to set
        // the default for next time:
        Globals.prefs.put(JabRefPreferences.LAST_USED_EXPORT, format.getName());
        Globals.prefs.put(JabRefPreferences.EXPORT_WORKING_DIRECTORY, file.getParent().toString());
    }

    public static void exporterRun(JabRefFrame frame,Path file,List<BibEntry> finEntries, Logger LOGGER){
        try {
            ExporterUtil.getFormat().export(frame.getCurrentBasePanel().getBibDatabaseContext(),
                    file,
                    frame.getCurrentBasePanel().getBibDatabaseContext().getMetaData().getEncoding().orElse(Globals.prefs.getDefaultEncoding()),
                    finEntries);
        } catch (Exception ex) {
            LOGGER.warn("Problem exporting", ex);
            if (ex.getMessage() == null) {
                setErrorMessage(ex.toString());
            } else {
                setErrorMessage(ex.getMessage());
            }
        }
    }

    public static Exporter getFormat(){
        return format;
    }

    public static List getEntries(){
        return entries;
    }

    private static void setErrorMessage(String message){
        errorMessage = message;
    }

    public static String getErrorMessage(){
        return errorMessage;
    }
}
