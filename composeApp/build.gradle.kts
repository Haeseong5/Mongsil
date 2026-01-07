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
            // SQLite 링커 옵션 추가
            linkerOpts("-lsqlite3")
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
            
            // Navigation Compose (Android 전용)
            implementation(libs.androidx.navigation.compose)
            
            // Koin Android
            implementation(libs.koin.android)
            
            // SQLDelight Android Driver
            implementation(libs.sqldelight.android.driver)
            
            // Ktor Android Engine
            implementation(libs.ktor.client.okhttp)
        }
        
        iosMain.dependencies {
            // SQLDelight Native Driver for iOS
            implementation(libs.sqldelight.native.driver)
            
            // Ktor iOS Engine
            implementation(libs.ktor.client.darwin)
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
            
            // DateTime
            implementation(libs.kotlinx.datetime)
            
            // Koin for KMP
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            
            // SQLDelight
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)
            
            // Ktor Client
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
        }
    }
}

android {
    namespace = "com.cashproject.mongsil.kmp"
    compileSdk = sdkCompileVersion
    
    defaultConfig {
        applicationId = "com.cashproject.mongsil"
        minSdk = sdkMinVersion
        targetSdk = sdkTargetVersion
        versionCode = 24
        versionName = "2.0.0"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    
    // 기존 앱과 동일한 서명 키 사용 (스토어 업데이트 필수!)
    val SIGNED_STORE_FILE: String by rootProject.extra
    val SIGNED_STORE_PASSWORD: String by rootProject.extra
    val SIGNED_STORE_KEY_ALIAS: String by rootProject.extra
    val SIGNED_STORE_KEY_PASSWORD: String by rootProject.extra
    
    signingConfigs {
        create("release") {
            storeFile = file(SIGNED_STORE_FILE)
            storePassword = SIGNED_STORE_PASSWORD
            keyAlias = SIGNED_STORE_KEY_ALIAS
            keyPassword = SIGNED_STORE_KEY_PASSWORD
        }
    }
    
    buildTypes {
        getByName("debug") {

        }
        
        getByName("release") {
            // 릴리즈는 기존 앱과 동일한 ID 사용
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            // TODO: 배포 전 ProGuard 규칙 추가
//             proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
