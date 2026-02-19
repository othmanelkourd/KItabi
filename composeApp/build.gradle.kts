import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)

}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            // iOS frameworks for barcode scanning
            linkerOpts.add("-framework")
            linkerOpts.add("Vision")
            linkerOpts.add("-framework")
            linkerOpts.add("AVFoundation")
            // iOS frameworks for Google Books API
            linkerOpts.add("-framework")
            linkerOpts.add("Foundation")
            linkerOpts.add("-framework")
            linkerOpts.add("CFNetwork")
            linkerOpts.add("-framework")
            linkerOpts.add("Security")
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.kator)
            implementation(libs.koin.android)
            // Camera and Barcode Scanning
            implementation(libs.camerax.core)
            implementation(libs.camerax.camera2)
            implementation(libs.camerax.lifecycle)
            implementation(libs.camerax.view)
            implementation(libs.mlkit.barcode)
            // Room Database
            implementation(libs.room.runtime)
            implementation(libs.room.ktx)
            // Guava for ListenableFuture support
            implementation(libs.guava)
        }
        iosMain.dependencies {
            // iOS HTTP client engine for Google Books API
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.compose.tooling)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kamel)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            // Google Books API integration
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.smile.kitabi"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.smile.kitabi"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    // KSP for Room annotation processing on Android
    ksp("androidx.room:room-compiler:${libs.versions.room.version.get()}")
}

