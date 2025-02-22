package io.github.thecatvoid.coderunner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;
import java.io.File;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import io.github.thecatvoid.coderunner.R;
import io.github.thecatvoid.coderunner.EditorActivity;
import io.github.thecatvoid.coderunner.PermissionActivity;
import io.github.thecatvoid.coderunner.CONST;
import io.github.thecatvoid.coderunner.AssetLoader;
import io.github.thecatvoid.coderunner.TerminalUtility;

public class MainActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                // Check permissions and launch the appropriate activity
                if (hasRequiredPermissions()) {
                        createDir();
                        AssetLoader.copyScriptFromAssets(this);
                        AssetLoader.copyTemplatesFromAssets(this);
                        copyScripts();
                        launchEditor();
                } else {
                        launchPermissionActivity();
                }
        }

        private boolean hasRequiredPermissions() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        return Environment.isExternalStorageManager();
                } else {
                        return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                }
        }

        // Output handling from termux requires accessible folder 
        private void createDir() {
                new File(CONST.RUNTIME_SCRIPT_DIR).mkdirs();
                new File(CONST.CODERUNNER_OUTPUT_FILEDIR).mkdirs();
                new File(CONST.RUNTIME_CODE_TEMPLATES_DIR).mkdirs();
        }

        private void copyScripts() {
                TerminalUtility utility = new TerminalUtility(this);

                // Use Termux to setup directories and retrieve the shell scripts from RUNTIME_SCRIPT_DIR
                utility.executeCommand(CONST.TERMUX_PATH + "mkdir", new String[] { "-p", CONST.SCRIPTS_COPIED_DIR });
                utility.executeCommand(CONST.TERMUX_PATH + "cp", new String[] { "-a", CONST.RUNTIME_SCRIPT_DIR_CONTENTS, CONST.SCRIPTS_COPIED_DIR });
                utility.executeCommand(CONST.TERMUX_PATH + "chmod", new String[] { "+x", CONST.CODERUNNER_HANDLER_FILEPATH });
        }

        private void launchEditor() {
                Intent editorIntent = new Intent(this, EditorActivity.class);
                startActivity(editorIntent);
                finish();
        }

        private void launchPermissionActivity() {
                Intent permissionIntent = new Intent(this, PermissionActivity.class);
                startActivity(permissionIntent);
                finish();
        }
}
