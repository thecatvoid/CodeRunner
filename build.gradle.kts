import java.util.Properties
import java.io.File

plugins {
    id("com.android.application") version "8.8.0"
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
}

android {
    namespace = "io.github.thecatvoid.coderunner"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.github.thecatvoid.coderunner"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0-alpha"
    }

    // Load properties from local.properties
    val properties = Properties().apply {
        val localPropertiesFile = File(rootProject.projectDir, "local.properties")
        if (localPropertiesFile.exists()) {
            load(localPropertiesFile.inputStream())
        }
    }

}
