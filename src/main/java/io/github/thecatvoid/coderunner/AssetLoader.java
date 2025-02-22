package io.github.thecatvoid.coderunner;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.*;

public class AssetLoader {

    public static void copyScriptFromAssets(Context context) {
        AssetManager assetManager = context.getAssets();

        // Load shell scripts
        for (String scriptName : CONST.SCRIPTS) {
            String scriptPath = CONST.ASSETS_SCRIPT_DIR + scriptName;
            File destFile = new File(CONST.RUNTIME_SCRIPT_DIR, scriptName);

            copyHandler(scriptPath, destFile, assetManager);
        }

        // Load template files
        copyTemplatesFromAssets(context);
    }

    public static void copyTemplatesFromAssets(Context context) {
        AssetManager assetManager = context.getAssets();
        File destDir = new File(CONST.RUNTIME_CODE_TEMPLATES_DIR);

        if (!destDir.exists() && !destDir.mkdirs()) {
            System.err.println("Failed to create directory: " + CONST.RUNTIME_CODE_TEMPLATES_DIR);
            return;
        }

        try {
            String[] templateFiles = assetManager.list(CONST.CODE_TEMPLATES_DIR);
            if (templateFiles != null) {
                for (String fileName : templateFiles) {
                    String assetPath = CONST.CODE_TEMPLATES_DIR + fileName;
                    File destFile = new File(destDir, fileName);

                    copyHandler(assetPath, destFile, assetManager);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyHandler(String assetPath, File destFile, AssetManager assetManager) {
        try (InputStream inputStream = assetManager.open(assetPath);
             OutputStream outputStream = new FileOutputStream(destFile)) {

            // Copy file contents
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}