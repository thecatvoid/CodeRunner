package io.github.thecatvoid.coderunner;

import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.os.FileObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import androidx.appcompat.app.AppCompatActivity;
import io.github.thecatvoid.coderunner.R;
import io.github.thecatvoid.coderunner.CONST;
import io.github.thecatvoid.coderunner.TerminalUtility;
import java.io.*;
import android.os.Handler;
import android.os.Looper;

public class TerminalActivity extends AppCompatActivity {

        private TextView terminalOutput;
        private Button executeButton;
        private String Lang;
        private String filename;
        private FileObserver fileObserver;
        private TerminalUtility utility;
        private String intentKey = "lang";
        private Handler mainHandler;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_terminal);

                mainHandler = new Handler(Looper.getMainLooper());
                terminalOutput = findViewById(R.id.terminal_output);
                executeButton = findViewById(R.id.execute_button);

                utility = new TerminalUtility(this);

                // Listen for file output
                setupFileObserver();

                // On play button click
                runCode();

                // Execute on play button and Execute button press
                executeButton.setOnClickListener(v -> runCode());
        }

        private void runCode() {
                Lang = getIntent().getStringExtra(intentKey);
                if (Lang == null) {
                        Toast.makeText(this, "Unable to identify language!", Toast.LENGTH_SHORT).show();
                        finish();
                }

                // Get full file path instead of just filename
                String filePath = getIntent().getStringExtra("filepath");
                if (filePath == null) {
                        Toast.makeText(this, "Unable to get the file path!", Toast.LENGTH_SHORT).show();
                        finish();
                }

                String[] codeRunnerArgs = { "-l", Lang, filePath };

                terminalOutput.setText(""); // Clear output before execution

                new Thread(() -> {
                        utility.executeCommand(CONST.CODERUNNER_HANDLER_FILEPATH, codeRunnerArgs);
                        mainHandler.post(this::readOutputFile);
                }).start();
        }

        private void setupFileObserver() {
                // Stop existing observer
                if (fileObserver != null) {
                        fileObserver.stopWatching();
                }

                // Create new observer
                fileObserver = new FileObserver(CONST.CODERUNNER_OUTPUT_FILEPATH) {
                        @Override
                        public void onEvent(int event, String path) {
                                if (event == FileObserver.MODIFY || event == FileObserver.CREATE) {
                                        mainHandler.post(() -> readOutputFile());
                                }
                        }
                };
                fileObserver.startWatching();
        }

        private void readOutputFile() {
                File file = new File(CONST.CODERUNNER_OUTPUT_FILEPATH);
                if (!file.exists()) {
                        mainHandler.post(() -> terminalOutput.setText("Waiting for output..."));
                        return;
                }

                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        StringBuilder content = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                                content.append(line).append("\n");
                        }
                        updateTerminalUI(content.toString());
                } catch (IOException e) {
                        String errorMessage = "Error reading output: " + e.getMessage();
                        mainHandler.post(() -> {
                                terminalOutput.setText(errorMessage);
                                utility.showError(errorMessage);
                        });
                }
        }

        private void updateTerminalUI(String content) {
                if (content.trim().isEmpty()) {
                        return;
                }

                mainHandler.post(() -> {
                        terminalOutput.setText(content);
                        // Auto-scroll to bottom
                        int scrollAmount = terminalOutput.getLayout().getLineTop(terminalOutput.getLineCount()) - terminalOutput.getHeight();
                        if (scrollAmount > 0)
                                terminalOutput.scrollTo(0, scrollAmount);
                        else
                                terminalOutput.scrollTo(0, 0);
                });
        }

        @Override
        protected void onDestroy() {
                super.onDestroy();
                if (fileObserver != null) {
                        fileObserver.stopWatching();
                }
        }
}