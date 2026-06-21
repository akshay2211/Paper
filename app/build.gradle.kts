import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

// val paperPropertiesFile = rootProject.file("../keys/paper.properties")
// val paperProperties = java.util.Properties().apply {
//     load(java.io.FileInputStream(paperPropertiesFile))
// }

android {
    namespace = "io.ak1.paper"
    compileSdk = 37

    defaultConfig {
        applicationId = "io.ak1.paper"
        minSdk = 24
        versionCode = 3
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

    signingConfigs {
        // create("release") {
        //     storeFile = file(paperProperties["STORE_PATH"] as String)
        //     storePassword = paperProperties["STORE_PASSWORD"] as String
        //     keyAlias = paperProperties["KEY_ALIAS"] as String
        //     keyPassword = paperProperties["KEY_PASSWORD"] as String
        // }
    }

    buildTypes {
        // release {
        //     resValue("string", "app_name", "Paper")
        //     resValue("string", "paper_file_provider", "io.ak1.paper.file_provider")
        //     isMinifyEnabled = true
        //     isShrinkResources = true
        //     signingConfig = signingConfigs.getByName("release")
        //     proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        // }
        debug {
            resValue("string", "app_name", "Paper Debug")
            resValue("string", "paper_file_provider", "io.ak1.paper.debug.file_provider")
            applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    sourceSets {
        getByName("androidTest").java.srcDirs("src/test-common/java")
        getByName("test").java.srcDirs("src/test-common/java")
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)

    // Compose (BOM-managed)
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Lifecycle
    implementation(libs.bundles.lifecycle)

    // Image Size Compressor
    implementation(libs.compressor)

    // Room
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    // Koin (multiplatform-ready)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Datastore Preferences
    implementation(libs.androidx.datastore.preferences)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material.navigation)

    // Coil
    implementation(libs.coil.compose)

    // Drawbox
    implementation(libs.drawbox)

    // Snapper
    implementation(libs.snapper)

    // RangVikalp
    implementation(libs.rang.vikalp)

    // Gson
    implementation(libs.gson)

    // Accompanist
    implementation(libs.bundles.accompanist)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
