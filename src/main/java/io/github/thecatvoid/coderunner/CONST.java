package io.github.thecatvoid.coderunner;

public class CONST {
        // General
        public static final String CODERUNNER_HANDLER_FILENAME = "coderunner.sh";

        // Only App can access (assets)
        public static final String SCRIPTS[] = { "coderunner.sh" };
        public static final String ASSETS_SCRIPT_DIR = "scripts/";
        public static final String CODE_TEMPLATES_DIR = "templates/";

        // Only Termux can access
        public static final String CODERUNNER_HANDLER_FILEPATH = "/data/data/com.termux/files/home/coderunner/scripts/" + CODERUNNER_HANDLER_FILENAME;
        public static final String TERMUX_HOME = "/data/data/com.termux/files/home/";
        public static final String TERMUX_PATH = "/data/data/com.termux/files/usr/bin/";
        public static final String SCRIPTS_COPIED_DIR = "/data/data/com.termux/files/home/coderunner/scripts/";

        // Both Termux and App can access
        public static final String APP_DIR = "/sdcard/coderunner/";
        public static final String RUNTIME_SCRIPT_DIR = APP_DIR + ASSETS_SCRIPT_DIR;
        public static final String RUNTIME_CODE_TEMPLATES_DIR = APP_DIR + CODE_TEMPLATES_DIR;
        public static final String RUNTIME_SCRIPT_DIR_CONTENTS = "/sdcard/coderunner/scripts/.";
        public static final String CODERUNNER_OUTPUT_FILEDIR = "/sdcard/coderunner/output/";
        public static final String CODERUNNER_OUTPUT_FILENAME = "coderunner_termux_output.log";
        public static final String CODERUNNER_OUTPUT_FILEPATH = CODERUNNER_OUTPUT_FILEDIR + CODERUNNER_OUTPUT_FILENAME;
}
