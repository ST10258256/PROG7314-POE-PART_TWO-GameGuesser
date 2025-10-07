import java.util.Properties
import java.io.FileInputStream
import java.net.ServerSocket

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
}

// Load local properties
//val localProps = Properties()
//val localPropsFile = rootProject.file("local.properties")
//if (localPropsFile.exists()) {
//    FileInputStream(localPropsFile).use { localProps.load(it) }
//}
//val openrouterKey: String = (localProps.getProperty("OPENROUTER_API_KEY") ?: "").trim()
val openrouterKey = "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"
android {
    namespace = "com.example.gameguesser"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.gameguesser"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "OPENROUTER_API_KEY", "\"$openrouterKey\"")
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    signingConfigs {
        create("sharedDebug") {
            storeFile = file("keystore/shared-debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        create("release") {
            storeFile = file("keystore/release-key.jks")
            storePassword = "Group14"
            keyAlias = "release"
            keyPassword = "Group14"
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("sharedDebug")
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

// --------------------------
// Backend start task
// --------------------------
//tasks.register("startBackend") {
//    doLast {
//        // Correct backend directory
//        val backendDir = file("${rootDir}/api/GameGuesserAPI")
//
//        if (!backendDir.exists()) {
//            throw GradleException("Backend directory does not exist: $backendDir")
//        }
//
//        println("Starting backend from: $backendDir")
//
//        // Open backend in a new terminal
//        ProcessBuilder("cmd", "/c", "start", "cmd", "/k", "dotnet run")
//            .directory(backendDir)
//            .start()
//    }
//}
//
//
//// Ensure backend starts before building the app
//tasks.named("preBuild") {
//    dependsOn("startBackend")
//}

// --------------------------
// Dependencies
// --------------------------
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)

    // Room (correct Android dependencies)
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")       // annotation processor
    implementation("androidx.room:room-ktx:2.6.1")  // coroutines support
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    // SSO
    implementation("com.google.android.gms:play-services-auth:21.4.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

}

