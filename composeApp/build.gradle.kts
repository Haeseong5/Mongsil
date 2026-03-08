plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.sqldelight)
    id("com.android.application")
}

kotlin {
    androidTarget()

    @Suppress("OPT_IN_USAGE")
    compilerOptions {
        jvmToolchain(17)
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

            // Ktor Android Engine
            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            // SQLDelight Native Driver for iOS
            implementation(libs.sqldelight.native.driver)

            // SQLite Bundled Driver for iOS (Room KMP)
            implementation(libs.androidx.sqlite.bundled)

            // Ktor iOS Engine
            implementation(libs.ktor.client.darwin)
        }

        commonMain.dependencies {
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.ui)
            implementation(libs.components.resources)
            implementation(libs.ui.tooling.preview)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // DateTime
            implementation(libs.kotlinx.datetime)

            // Kotlin Serialization
            implementation(libs.kotlinx.serialization.json)

            // Koin for KMP
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel.navigation)

            // Room KMP
            implementation(libs.androidx.room.runtime)

            // SQLDelight
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)

            // Ktor Client
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)

            // Coil - Image Loading (KMP)
            implementation(libs.coil.compose.kmp)
            implementation(libs.coil.network.ktor)

            // Calendar
            implementation("com.kizitonwose.calendar:compose-multiplatform:2.10.0")

            implementation(libs.androidx.datastore.core)
            implementation(libs.androidx.datastore.core.okio)
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

// Room KMP 스키마 디렉토리 설정
room {
    schemaDirectory("$projectDir/schemas")
}

// SQLDelight 설정
sqldelight {
    databases {
        create("MongsilDatabase") {
            packageName.set("com.cashproject.mongsil.kmp.database")
        }
    }
}

// Room KSP - 각 플랫폼 타겟에 개별 등록 필요
dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
