plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "io.github.lucf15.adaptivenotes"
    compileSdk = 37
    defaultConfig {
        applicationId = "io.github.lucf15.adaptivenotes"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "0.1.0"
    }
    flavorDimensions += "layout"
    productFlavors {
        create("adaptive") {
            dimension = "layout"
            buildConfigField("boolean", "EMBEDDING_ENABLED", "true")
            manifestPlaceholders["embeddingEnabled"] = "true"
            resValue("string", "app_name", "Fieldnotes")
        }
        create("classic") {
            dimension = "layout"
            applicationIdSuffix = ".classic"
            buildConfigField("boolean", "EMBEDDING_ENABLED", "false")
            manifestPlaceholders["embeddingEnabled"] = "false"
            resValue("string", "app_name", "Fieldnotes Classic")
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures { compose = true; buildConfig = true; resValues = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2026.09.00"))
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.11.0")
    implementation("androidx.window:window:1.5.1")
    // R8 needs the device-provided SAM signature when desugaring WindowManager's calculator.
    compileOnly("androidx.window.extensions.core:core:1.0.0")
    implementation("io.insert-koin:koin-android:4.2.2")
}
