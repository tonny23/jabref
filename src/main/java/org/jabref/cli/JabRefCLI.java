package org.jabref.cli;

import java.util.List;

import org.jabref.Globals;
import org.jabref.logic.l10n.Localization;
import org.jabref.model.database.BibDatabaseMode;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JabRefCLI {

    private final CommandLine cl;
    private List<String> leftOver;

    private static final String AUTOMATICALLY_SET_FILE_LINKS = "automaticallySetFileLinks";
    private static final String AUX = "aux";
    private static final String BLANK = "blank";
    private static final String DEBUG = "debug";
    private static final String EXPORT_MATCHES = "exportMatches";
    private static final String FETCH = "fetch";
    private static final String FIELD = "field";
    private static final String FILE = "FILE";
    private static final String GENERATE_BIBTEX_KEYS = "generateBibtexKeys";
    private static final String HELP = "help";
    private static final String IMPORT = "import";
    private static final String IMPORT_BIBTEX = "importBibtex";
    private static final String IMPORT_TO_OPEN = "importToOpen";
    private static final String LOADS = "loads";
    private static final Logger LOGGER = LoggerFactory.getLogger(JabRefCLI.class);
    private static final String NO_GUI = "nogui";
    private static final String OUTPUT = "output";
    private static final String PRDEF = "prdef";
    private static final String PREFS_XML = "jabref_prefs.xml";
    private static final String PREXP = "prexp";
    private static final String PRIMP = "primp";
    private static final String VERSION = "version";


    public JabRefCLI(String[] args) {

        Options options = getOptions();

        try {
            this.cl = new DefaultParser().parse(options, args);
            this.leftOver = cl.getArgList();
        } catch (ParseException e) {
            LOGGER.warn("Problem parsing arguments", e);

            this.printUsage();
            throw new RuntimeException();
        }
    }

    public static String getExportMatchesSyntax() {
        return String.format("[%s]searchTerm,outputFile: %s[,%s]",
                Localization.lang(FIELD),
                Localization.lang(FILE),
                Localization.lang("exportFormat"));
    }

    public boolean isHelp() {
        return cl.hasOption(HELP);
    }

    public boolean isShowVersion() {
        return cl.hasOption(VERSION);
    }

    public boolean isBlank() {
        return cl.hasOption(BLANK);
    }

    public boolean isLoadSession() {
        return cl.hasOption(LOADS);
    }

    public boolean isDisableGui() {
        return cl.hasOption(NO_GUI);
    }

    public boolean isPreferencesExport() {
        return cl.hasOption(PREXP);
    }

    public String getPreferencesExport() {
        return cl.getOptionValue(PREXP, PREFS_XML);
    }

    public boolean isPreferencesImport() {
        return cl.hasOption(PRIMP);
    }

    public String getPreferencesImport() {
        return cl.getOptionValue(PRIMP, PREFS_XML);
    }

    public boolean isPreferencesReset() {
        return cl.hasOption(PRDEF);
    }

    public String getPreferencesReset() {
        return cl.getOptionValue(PRDEF);
    }

    public boolean isFileExport() {
        return cl.hasOption(OUTPUT);
    }

    public String getFileExport() {
        return cl.getOptionValue(OUTPUT);
    }

    public boolean isBibtexImport() {
        return cl.hasOption(IMPORT_BIBTEX);
    }

    public String getBibtexImport() {
        return cl.getOptionValue(IMPORT_BIBTEX);
    }

    public boolean isFileImport() {
        return cl.hasOption(IMPORT);
    }

    public String getFileImport() {
        return cl.getOptionValue(IMPORT);
    }

    public boolean isAuxImport() {
        return cl.hasOption(AUX);
    }

    public String getAuxImport() {
        return cl.getOptionValue(AUX);
    }

    public boolean isImportToOpenBase() {
        return cl.hasOption(IMPORT_TO_OPEN);
    }

    public String getImportToOpenBase() {
        return cl.getOptionValue(IMPORT_TO_OPEN);
    }

    public boolean isDebugLogging() {
        return cl.hasOption(DEBUG);
    }

    public boolean isFetcherEngine() {
        return cl.hasOption(FETCH);
    }

    public String getFetcherEngine() {
        return cl.getOptionValue(FETCH);
    }

    public boolean isExportMatches() {
        return cl.hasOption(EXPORT_MATCHES);
    }

    public String getExportMatches() {
        return cl.getOptionValue(EXPORT_MATCHES);
    }

    public boolean isGenerateBibtexKeys() { return cl.hasOption(GENERATE_BIBTEX_KEYS); }

    public boolean isAutomaticallySetFileLinks() { return cl.hasOption(AUTOMATICALLY_SET_FILE_LINKS); }

    private Options getOptions() {
        Options options = new Options();

        // boolean options
        options.addOption("v", VERSION, false, Localization.lang("Display version"));
        options.addOption("n", NO_GUI, false, Localization.lang("No GUI. Only process command line options."));
        options.addOption("h", HELP, false, Localization.lang("Display help on command line options"));
        options.addOption("b", BLANK, false, Localization.lang("Do not open any files at startup"));
        options.addOption(null, DEBUG, false, Localization.lang("Show debug level messages"));

        // The "-console" option is handled by the install4j launcher
        options.addOption(null, "console", false, Localization.lang("Show console output (only necessary when the launcher is used)"));

        options.addOption(Option.builder("i").
                longOpt(IMPORT).
                desc(String.format("%s: %s[,import format]", Localization.lang("Import file"),
                        Localization.lang("filename"))).
                hasArg().
                argName(FILE).build());

        options.addOption(
                Option.builder("ib")
                      .longOpt(IMPORT_BIBTEX)
                        .desc(String.format("%s: %s[,importBibtex bibtexString]", Localization.lang(IMPORT) + " " + BibDatabaseMode.BIBTEX.getFormattedName(), Localization.lang("filename")))
                      .hasArg()
                      .argName(FILE)
                      .build());

        options.addOption(Option.builder("o").
                longOpt(OUTPUT).
                desc(String.format("%s: %s[,export format]", Localization.lang("Output or export file"),
                        Localization.lang("filename"))).
                hasArg().
                argName(FILE).
                build());

        options.addOption(Option.builder("x").
                longOpt(PREXP).
                desc(Localization.lang("Export preferences to file")).
                hasArg().
                argName(FILE).
                build());

        options.addOption(Option.builder("p").
                longOpt(PRIMP).
                desc(Localization.lang("Import preferences from file")).
                hasArg().
                argName(FILE).
                build());
        options.addOption(Option.builder("d").
                longOpt(PRDEF).
                desc(Localization.lang("Reset preferences (key1,key2,... or 'all')")).
                hasArg().
                argName(FILE).
                build());

        options.addOption(Option.builder("a").
                longOpt(AUX).
                desc(String.format("%s: %s[.aux],%s[.bib]", Localization.lang("Sublibrary from AUX"),
                        Localization.lang(FILE),
                        Localization.lang("new"))).
                hasArg().
                argName(FILE).
                build());

        options.addOption(Option.builder().
                longOpt(IMPORT_TO_OPEN).
                desc(Localization.lang("Import to open tab")).
                hasArg().
                argName(FILE).
                build());

        options.addOption(Option.builder("f").
                longOpt(FETCH).
                desc(Localization.lang("Run fetcher, e.g. \"--fetch=Medline:cancer\"")).
                hasArg().
                argName(FILE).
                build());

        options.addOption(Option.builder("m").
                longOpt(EXPORT_MATCHES).
                desc(JabRefCLI.getExportMatchesSyntax()).
                hasArg().
                argName(FILE).
                build());

        options.addOption(Option.builder("g").
                longOpt(GENERATE_BIBTEX_KEYS).
                desc(Localization.lang("Regenerate all keys for the entries in a BibTeX file"))
                .build());

        options.addOption(Option.builder("asfl").
                longOpt(AUTOMATICALLY_SET_FILE_LINKS).
                desc(Localization.lang("Automatically set file links")).
                build());

        return options;
    }

    public void displayVersion() {
        System.out.println(getVersionInfo());
    }

    public void printUsage() {
        String header = "";

        String importFormats = Globals.IMPORT_FORMAT_READER.getImportFormatList();
        String importFormatsList = String.format("%s:%n%s%n", Localization.lang("Available import formats"), importFormats);

        String outFormats = Globals.exportFactory.getExportersAsString(70, 20, "");
        String outFormatsList = String.format("%s: %s%n", Localization.lang("Available export formats"), outFormats);

        String footer = '\n' + importFormatsList + outFormatsList + "\nPlease report issues at https://github.com/JabRef/jabref/issues.";

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("jabref [OPTIONS] [BIBTEX_FILE]\n\nOptions:", header, getOptions(), footer, true);
    }

    private String getVersionInfo() {
        return String.format("JabRef %s", Globals.BUILD_INFO.getVersion());
    }

    public List<String> getLeftOver() {
        return leftOver;
    }
}
