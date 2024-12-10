plugins {
    alias(libs.plugins.android.application) // Usando alias del archivo TOML
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.financiero"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.financiero"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true // Habilitamos Jetpack Compose
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2" // Actualiza según tu versión de Compose
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.support.annotations)
    // BOM para mantener versiones consistentes
    val composeBom = platform("androidx.compose:compose-bom:2024.10.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation ("com.google.android.material:material:1.12.0")
    // Componentes esenciales de Jetpack Compose
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3:1.2.0") // Asegúrate de usar la última versión estable
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Opcional - Integración con actividades
    implementation("androidx.activity:activity-compose:1.9.2")

    // Opcional - Otras herramientas de Compose
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    implementation("androidx.compose.foundation:foundation")

}
