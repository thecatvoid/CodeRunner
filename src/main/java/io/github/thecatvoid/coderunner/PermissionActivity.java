package io.github.thecatvoid.coderunner;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


public class PermissionActivity extends AppCompatActivity {
        private static final int STORAGE_PERMISSION_CODE = 100;
        private static final int ALL_FILES_PERMISSION_CODE = 101;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_permission);

                Button grantPermissionButton = findViewById(R.id.grantPermissionButton);
                grantPermissionButton.setOnClickListener(v -> checkAndRequestPermissions());

                // Initial check, but don't auto-request on first launch
                if (hasRequiredPermissions()) {
                        proceedToMainActivity();
                }
        }

        private void checkAndRequestPermissions() {
                if (hasRequiredPermissions()) {
                        proceedToMainActivity();
                } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                requestAndroidRPermission();
                        } else {
                                requestLegacyStoragePermission();
                        }
                }
        }

        private boolean hasRequiredPermissions() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        return Environment.isExternalStorageManager();
                } else {
                        return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                }
        }

        private void requestAndroidRPermission() {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, ALL_FILES_PERMISSION_CODE);
        }

        private void requestLegacyStoragePermission() {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == ALL_FILES_PERMISSION_CODE) {
                        if (hasRequiredPermissions()) {
                                proceedToMainActivity();
                        } else {
                                Toast.makeText(this, "Storage permission is required", Toast.LENGTH_LONG).show();
                        }
                }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                if (requestCode == STORAGE_PERMISSION_CODE) {
                        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                                proceedToMainActivity();
                        } else {
                                Toast.makeText(this, "Storage permission is required", Toast.LENGTH_LONG).show();
                        }
                }
        }

        private void proceedToMainActivity() {
                finish();
        }

        @Override
        protected void onResume() {
                super.onResume();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (hasRequiredPermissions()) {
                                proceedToMainActivity();
                        }
                }
        }
}
