plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.sqldelight)
    id("com.android.application")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    // iOS Deployment Target 설정
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
        binaries.all {
            freeCompilerArgs += "-Xbinary=bundleId=com.cashproject.mongsil.kmp.ComposeApp"
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.appcompat)
            
            // Koin Android
            implementation(libs.koin.android)
            
            // SQLDelight Android Driver
            implementation(libs.sqldelight.android.driver)
        }
        
        iosMain.dependencies {
            // SQLDelight Native Driver for iOS
            implementation(libs.sqldelight.native.driver)
        }
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            
            // Coroutines
            implementation(libs.kotlinx.coroutines.core)
            
            // Koin for KMP
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            
            // SQLDelight
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)
        }
    }
}

android {
    namespace = "com.cashproject.mongsil.kmp"
    compileSdk = sdkCompileVersion
    
    defaultConfig {
        applicationId = "com.cashproject.mongsil.kmp"
        minSdk = sdkMinVersion
        targetSdk = sdkTargetVersion
        versionCode = 1
        versionName = "1.0.0"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

// SQLDelight 설정
sqldelight {
    databases {
        create("MongsilDatabase") {
            packageName.set("com.cashproject.mongsil.kmp.database")
        }
    }
}

dependencies {
    debugImplementation(libs.androidx.compose.ui.tooling)
}
