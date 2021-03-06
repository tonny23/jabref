package org.jabref.gui.importer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.stream.Collectors;

import javafx.stage.FileChooser;

import org.jabref.Globals;
import org.jabref.gui.DialogService;
import org.jabref.gui.JabRefFrame;
import org.jabref.gui.actions.SimpleCommand;
import org.jabref.gui.util.DefaultTaskExecutor;
import org.jabref.gui.util.FileDialogConfiguration;
import org.jabref.gui.util.FileFilterConverter;
import org.jabref.logic.importer.Importer;
import org.jabref.logic.l10n.Localization;
import org.jabref.logic.util.FileType;
import org.jabref.preferences.JabRefPreferences;

/**
 * Perform import operation
 */
public class ImportCommand extends SimpleCommand {

    private final JabRefFrame frame;
    private final boolean openInNew;
    private final DialogService dialogService;

    /**
     * @param openInNew Indicate whether the entries should import into a new database or into the currently open one.
     */
    public ImportCommand(JabRefFrame frame, boolean openInNew) {
        this.frame = frame;
        this.openInNew = openInNew;
        this.dialogService = frame.getDialogService();
    }

    @Override
    public void execute() {
        SortedSet<Importer> importers = Globals.IMPORT_FORMAT_READER.getImportFormats();
        List<FileType> extensions = importers.stream().map(Importer::getFileType)
                                             .collect(Collectors.toList());

        FileChooser.ExtensionFilter allImports = FileFilterConverter.forAllImporters(importers);
        FileChooser.ExtensionFilter anyFile = new FileChooser.ExtensionFilter(Localization.lang("Any file"), "*.*");
        FileDialogConfiguration fileDialogConfiguration = new FileDialogConfiguration.Builder()
                .addExtensionFilter(anyFile)
                .addExtensionFilter(allImports)
                .addExtensionFilter(extensions.toArray(new FileType[extensions.size()]))
                .withInitialDirectory(Globals.prefs.get(JabRefPreferences.IMPORT_WORKING_DIRECTORY))
                .build();
        DefaultTaskExecutor.runInJavaFXThread(() -> {
            dialogService.showFileOpenDialog(fileDialogConfiguration)
                         .ifPresent(path -> doImport(path, importers, fileDialogConfiguration.getSelectedExtensionFilter()));
        });
    }

    private void doImport(Path file, SortedSet<Importer> importers, FileChooser.ExtensionFilter selectedExtensionFilter) {
        if (!Files.exists(file)) {
            dialogService.showErrorDialogAndWait(Localization.lang("Import"),
                    Localization.lang("File not found") + ": '" + file.getFileName() + "'.");

            return;
        }
        Optional<Importer> format = FileFilterConverter.getImporter(selectedExtensionFilter, importers);
        ImportAction importMenu = new ImportAction(frame, openInNew, format.orElse(null));
        importMenu.automatedImport(Collections.singletonList(file.toString()));
        // Set last working dir for import
        Globals.prefs.put(JabRefPreferences.IMPORT_WORKING_DIRECTORY, file.getParent().toString());
    }
}
