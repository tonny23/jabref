package org.jabref.gui.exporter;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.stage.FileChooser;

import org.jabref.Globals;
import org.jabref.gui.DialogService;
import org.jabref.gui.JabRefFrame;
import org.jabref.gui.actions.SimpleCommand;
import org.jabref.gui.util.DefaultTaskExecutor;
import org.jabref.gui.util.FileDialogConfiguration;
import org.jabref.gui.worker.AbstractWorker;
import org.jabref.logic.exporter.Exporter;
import org.jabref.logic.exporter.ExporterFactory;
import org.jabref.logic.exporter.SavePreferences;
import org.jabref.logic.exporter.TemplateExporter;
import org.jabref.logic.l10n.Localization;
import org.jabref.logic.layout.LayoutFormatterPreferences;
import org.jabref.logic.util.FileType;
import org.jabref.logic.xmp.XmpPreferences;
import org.jabref.model.entry.BibEntry;
import org.jabref.preferences.JabRefPreferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Performs an export action
 */
public class ExportCommand extends SimpleCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportCommand.class);
    private final JabRefFrame frame;
    private final boolean selectedOnly;
    private final JabRefPreferences preferences;

    /**
     * @param selectedOnly true if only the selected entries should be exported, otherwise all entries are exported
     */
    public ExportCommand(JabRefFrame frame, boolean selectedOnly, JabRefPreferences preferences) {
        this.frame = frame;
        this.selectedOnly = selectedOnly;
        this.preferences = preferences;
    }

    @Override
    public void execute() {
        Map<String, TemplateExporter> customExporters = preferences.customExports.getCustomExportFormats(preferences, Globals.journalAbbreviationLoader);
        LayoutFormatterPreferences layoutPreferences = preferences.getLayoutFormatterPreferences(Globals.journalAbbreviationLoader);
        SavePreferences savePreferences = preferences.loadForExportFromPreferences();
        XmpPreferences xmpPreferences = preferences.getXMPPreferences();
        Globals.exportFactory = ExporterFactory.create(customExporters, layoutPreferences, savePreferences, xmpPreferences);
        FileDialogConfiguration fileDialogConfiguration = createExportFileChooser(Globals.exportFactory, Globals.prefs.get(JabRefPreferences.EXPORT_WORKING_DIRECTORY));
        DialogService dialogService = frame.getDialogService();
        DefaultTaskExecutor.runInJavaFXThread(() -> dialogService.showFileSaveDialog(fileDialogConfiguration)
                .ifPresent(path -> export(path, fileDialogConfiguration.getSelectedExtensionFilter(), Globals.exportFactory.getExporters())));
    }

    private void export(Path file, FileChooser.ExtensionFilter selectedExtensionFilter, List<Exporter> exporters) {

        ExporterUtil.exporter(file,selectedExtensionFilter,exporters,selectedOnly,frame);

        final List<BibEntry> finEntries = ExporterUtil.getEntries();
        AbstractWorker exportWorker = new AbstractWorker() {

            @Override
            public void run() {
                ExporterUtil.exporterRun(frame,file,finEntries,LOGGER);
            }

            @Override
            public void update() {
                // No error message. Report success:
                if (ExporterUtil.getErrorMessage() == null) {
                    frame.output(Localization.lang("%0 export successful", ExporterUtil.getFormat().getName()));
                }
                // ... or show an error dialog:
                else {
                    frame.output(Localization.lang("Could not save file.") + " - " + ExporterUtil.getErrorMessage());
                    // Need to warn the user that saving failed!
                    frame.getDialogService().showErrorDialogAndWait(Localization.lang("Save library"), Localization.lang("Could not save file.") + "\n" + ExporterUtil.getErrorMessage());

                }
            }
        };

        // Run the export action in a background thread:
        exportWorker.getWorker().run();
        // Run the update method:
        exportWorker.update();
    }

    private static FileDialogConfiguration createExportFileChooser(ExporterFactory exportFactory, String currentDir) {
        List<FileType> fileTypes = exportFactory.getExporters().stream().map(Exporter::getFileType).collect(Collectors.toList());
        return new FileDialogConfiguration.Builder()
                .addExtensionFilter(fileTypes.toArray(new FileType[fileTypes.size()]))
                .withDefaultExtension(Globals.prefs.get(JabRefPreferences.LAST_USED_EXPORT))
                .withInitialDirectory(currentDir)
                .build();
    }

}
