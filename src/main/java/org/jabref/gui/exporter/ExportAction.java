package org.jabref.gui.exporter;

import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import javafx.stage.FileChooser;

import org.jabref.Globals;
import org.jabref.gui.DialogService;
import org.jabref.gui.FXDialogService;
import org.jabref.gui.JabRefFrame;
import org.jabref.gui.actions.MnemonicAwareAction;
import org.jabref.gui.util.DefaultTaskExecutor;
import org.jabref.gui.util.FileDialogConfiguration;
import org.jabref.gui.util.FileFilterConverter;
import org.jabref.gui.worker.AbstractWorker;
import org.jabref.logic.exporter.Exporter;
import org.jabref.logic.l10n.Localization;
import org.jabref.model.entry.BibEntry;
import org.jabref.preferences.JabRefPreferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExportAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportAction.class);

    private ExportAction() {
    }

    /**
     * Create an AbstractAction for performing an export operation.
     *
     * @param frame
     *            The JabRefFrame of this JabRef instance.
     * @param selectedOnly
     *            true indicates that only selected entries should be exported,
     *            false indicates that all entries should be exported.
     * @return The action.
     */
    public static AbstractAction getExportAction(JabRefFrame frame, boolean selectedOnly) {

        class InternalExportAction extends MnemonicAwareAction {

            private final JabRefFrame frame;

            private final boolean selectedOnly;

            public InternalExportAction(JabRefFrame frame, boolean selectedOnly) {
                this.frame = frame;
                this.selectedOnly = selectedOnly;
                putValue(Action.NAME, selectedOnly ? Localization.lang("Export selected entries") : Localization
                        .lang("Export"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                Globals.exportFactory = Globals.prefs.getExporterFactory(Globals.journalAbbreviationLoader);
                FileDialogConfiguration fileDialogConfiguration = new FileDialogConfiguration.Builder()
                        .addExtensionFilter(FileFilterConverter.exporterToExtensionFilter(Globals.exportFactory.getExporters()))
                        .withDefaultExtension(Globals.prefs.get(JabRefPreferences.LAST_USED_EXPORT))
                        .withInitialDirectory(Globals.prefs.get(JabRefPreferences.EXPORT_WORKING_DIRECTORY))
                        .build();
                DialogService dialogService = new FXDialogService();
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
                            // FIXME: This should be JavaFX
                            /*
                            JOptionPane.showMessageDialog(frame,
                                    Localization.lang("Could not save file.") + "\n" + errorMessage,
                                    Localization.lang("Save library"), JOptionPane.ERROR_MESSAGE);
                                    */
                        }
                    }
                };

                // Run the export action in a background thread:
                exportWorker.getWorker().run();
                // Run the update method:
                exportWorker.update();
            }
        }

        return new InternalExportAction(frame, selectedOnly);
    }
}
