package io.github.thecatvoid.coderunner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.github.thecatvoid.coderunner.R;
import io.github.thecatvoid.coderunner.TerminalActivity;
import io.github.thecatvoid.coderunner.CONST;
import android.net.Uri;
import java.io.*;
import java.io.FileOutputStream;
import android.database.Cursor;
import android.provider.OpenableColumns;

public class EditorActivity extends AppCompatActivity {
        private EditText editText;
        private String filename;
        private static final int PICK_FILE_REQUEST_CODE = 42;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_editor);

                // Toolbar as action bar for save button
                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                // Initialize text editor
                editText = findViewById(R.id.editText);

                // Prevent null value crashes
                if (editText == null) {
                        Toast.makeText(this, "Editor not initialized!", Toast.LENGTH_SHORT).show();
                        return;
                }
        }

        private void saveFileButton() {
                final EditText input = new EditText(this);

                new AlertDialog.Builder(this)
                        .setTitle("Enter File Name")
                        .setMessage("Please enter the File Name:")
                        .setView(input)
                        .setPositiveButton("OK", (dialog, which) -> {
                                String content = editText.getText().toString();
                                filename = input.getText().toString();

                                if (filename.isEmpty()) {
                                        Toast.makeText(this, "Filename cannot be empty!", Toast.LENGTH_SHORT).show();
                                        return;
                                }

                                saveFile(content);
                        })
                .setNegativeButton("Cancel", null)
                        .show();
        }

        private void saveFile(String content) {
                try {
                        // Get app directory
                        File file = new File(CONST.APP_DIR + filename);

                        // Write content to file
                        try (FileOutputStream f = new FileOutputStream(file)) {
                                f.write(content.getBytes());
                                f.flush();
                                Toast.makeText(this, "Content saved successfully", Toast.LENGTH_SHORT).show();
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error saving file", Toast.LENGTH_SHORT).show();
                }
        }

        // Inflate menu view
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_editor, menu);
                return true;
        }

        // Buttons on the toolbar
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.action_open) {
                        openFilePicker();
                        return true;
                } else if (item.getItemId() == R.id.action_save) {
                        saveFileButton();
                        return true;
                } else if (item.getItemId() == R.id.action_run) {
                        runCode();
                        return true;
                } else {
                        return super.onOptionsItemSelected(item);
                }
        }

        private void runCode() {
                String intentKey = "lang";
                String language = identifyLanguageExtension();
                Intent intent = new Intent(this, TerminalActivity.class);
                intent.putExtra(intentKey, language);

                // Ensure full path is used
                String fullFilePath = CONST.APP_DIR + filename;
                intent.putExtra("filepath", fullFilePath); 

                startActivity(intent);
        }


        private String identifyLanguageExtension() {
                // Identify the language based on its saved extension
                for (int i = filename.length() - 1; i >= 0; i--) {
                        if (filename.charAt(i) == '.') {
                                return filename.substring(i + 1);
                        }
                }
                return "LanguageDontExist"; // Default to arbitrary code for error handling
        }

        private void openFilePicker() {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*"); // Allows all file types
                startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
                        if (data != null) {
                                Uri uri = data.getData();
                                if (uri != null) {
                                        String realPath = getRealPathFromUri(uri);
                                        filename = getFilenameFromUri(uri); // Extract filename
                                        readTextFromUri(uri); // Load and display file content

                                        if (realPath != null) {
                                                filename = new File(realPath).getName(); // Get only filename
                                        }

                                        // Use absolute path if available, otherwise fallback to Uri
                                        String executionPath = (realPath != null) ? realPath : uri.toString();
                                        editText.setTag(executionPath); // Store execution path
                                } else {
                                        Toast.makeText(this, "Failed to get file path", Toast.LENGTH_SHORT).show();
                                }
                        }
                }
        }

        private void readTextFromUri(Uri uri) {
                try (InputStream inputStream = getContentResolver().openInputStream(uri);
                                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                                stringBuilder.append(line).append("\n");
                        }

                        editText.setText(stringBuilder.toString()); // Display content in editor
                        filename = uri.getLastPathSegment(); // Extract filename
                } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to open file", Toast.LENGTH_SHORT).show();
                }
        }

        private String getFilenameFromUri(Uri uri) {
                String result = null;
                if (uri.getScheme().equals("content")) {
                        try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                                if (cursor != null && cursor.moveToFirst()) {
                                        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                        if (nameIndex != -1) {
                                                result = cursor.getString(nameIndex);
                                        }
                                }
                        }
                }
                if (result == null) {
                        result = uri.getPath();
                        int cut = result.lastIndexOf('/');
                        if (cut != -1) {
                                result = result.substring(cut + 1);
                        }
                }
                return result;
        }

        private String getRealPathFromUri(Uri uri) {
                String realPath = null;

                if ("content".equalsIgnoreCase(uri.getScheme())) {
                        try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                                if (cursor != null && cursor.moveToFirst()) {
                                        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                        if (nameIndex != -1) {
                                                String fileName = cursor.getString(nameIndex);
                                                File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);

                                                try (InputStream inputStream = getContentResolver().openInputStream(uri);
                                                                OutputStream outputStream = new FileOutputStream(file)) {

                                                        byte[] buffer = new byte[1024];
                                                        int length;
                                                        while ((length = inputStream.read(buffer)) > 0) {
                                                                outputStream.write(buffer, 0, length);
                                                        }
                                                        outputStream.flush();
                                                        realPath = file.getAbsolutePath(); // Now we have a real path
                                                } catch (IOException e) {
                                                        e.printStackTrace();
                                                }
                                        }
                                }
                        }
                }

                return realPath;
        }

}
