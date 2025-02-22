package io.github.thecatvoid.coderunner;

import android.app.Activity;
import android.os.FileObserver;
import android.widget.TextView;
import android.widget.Toast;
import io.github.thecatvoid.coderunner.CONST;
import android.content.Intent;
import android.content.pm.PackageManager;
import java.io.*;
import androidx.annotation.NonNull;

public class TerminalUtility {
        private final Activity activity;


        public TerminalUtility(@NonNull Activity activity) {
                this.activity = activity;
        }

        // @CommandPath: The full absolute path to the binary of a program
        // @Arguments: The arguments to be passed to the program
        // Passed command must implement output capturing by itself
        public void executeCommand(String CommandPath, String[] Arguments) {
                if (isTermuxInstalled()) {
                        try {
                                // Create intent to run command in Termux
                                Intent intent = new Intent();
                                intent.setClassName("com.termux", "com.termux.app.RunCommandService");
                                intent.setAction("com.termux.RUN_COMMAND");
                                intent.putExtra("com.termux.RUN_COMMAND_PATH", CommandPath);
                                intent.putExtra("com.termux.RUN_COMMAND_ARGUMENTS", Arguments);
                                intent.putExtra("com.termux.RUN_COMMAND_WORKDIR", CONST.TERMUX_HOME);
                                intent.putExtra("com.termux.RUN_COMMAND_BACKGROUND", true);
                                intent.putExtra("com.termux.RUN_COMMAND_SESSION_ACTION", "0");
                                activity.startService(intent);

                        } catch (Exception e) {
                                showError("Error: " + e.getMessage());
                        }
                } else {
                        showError("Termux is not installed!");
                }
        }

        private boolean isTermuxInstalled() {
                try {
                        activity.getPackageManager().getPackageInfo("com.termux", 0);
                        return true;
                } catch (PackageManager.NameNotFoundException e) {
                        return false;
                }
        }


        public void showError(String message) {
                activity.runOnUiThread(() -> {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                });
        }

}