plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.sqldelight)
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}
kotlin {
    androidTarget()

    jvm("desktop")

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

    @Suppress("OPT_IN_USAGE")
    compilerOptions {
        jvmToolchain(17)
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.biometric)
            implementation(libs.google.billing)

            // Koin Android
            implementation(libs.koin.android)

            // SQLDelight Android Driver
            implementation(libs.sqldelight.android.driver)

            // Ktor Android Engine
            implementation(libs.ktor.client.okhttp)

            // AdMob
            implementation(libs.google.ads)

            // Google Drive Backup
            implementation(libs.google.auth)
            implementation(libs.google.api.client.android)
            implementation(libs.google.api.drive)

            // Firebase (GitLive KMP)
            implementation(libs.gitlive.firebase.analytics)
            implementation(libs.gitlive.firebase.crashlytics)
            implementation(libs.gitlive.firebase.config)

            // Glance AppWidget
            implementation(libs.androidx.glance.appwidget)
        }

        iosMain.dependencies {
            // SQLDelight Native Driver for iOS
            implementation(libs.sqldelight.native.driver)

            // SQLite Bundled Driver for iOS (Room KMP)
            implementation(libs.androidx.sqlite.bundled)

            // Ktor iOS Engine
            implementation(libs.ktor.client.darwin)
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                // Coroutines Swing — provides Dispatchers.Main for Desktop (JVM)
                implementation(libs.kotlinx.coroutines.swing)

                // SQLDelight JDBC Driver for Desktop
                implementation(libs.sqldelight.sqlite.driver)

                // SQLite Bundled Driver for Desktop (Room KMP)
                implementation(libs.androidx.sqlite.bundled)

                // Ktor Desktop Engine
                implementation(libs.ktor.client.cio)
            }
        }

        commonMain.dependencies {
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.ui)
            implementation(compose.materialIconsExtended)
            implementation(libs.components.resources)
            implementation(libs.ui.tooling.preview)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // DateTime
            implementation(libs.kotlinx.datetime)

            // Kotlin Serialization
            implementation(libs.kotlinx.serialization.json)

            // Lifecycle
            implementation(libs.androidx.lifecycle.runtime.compose)

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
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.cashproject.mongsil"
        minSdk = libs.versions.sdkMinVersion.get().toInt()
        targetSdk = libs.versions.sdkTargetVersion.get().toInt()
        versionCode = 24
        versionName = "2.0.0"
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/INDEX.LIST"
            excludes += "/META-INF/DEPENDENCIES"
        }
    }

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
            buildConfigField("boolean", "IS_DEBUG", "true")
            buildConfigField("String", "ADMOB_APP_ID", "\"ca-app-pub-3940256099942544~3347511713\"")
            buildConfigField("String", "ADMOB_BANNER_AD_UNIT_ID", "\"ca-app-pub-3940256099942544/6300978111\"")
            buildConfigField("String", "ADMOB_REWARDED_AD_UNIT_ID", "\"ca-app-pub-3940256099942544/5224354917\"")
            manifestPlaceholders["ADMOB_APP_ID"] = "ca-app-pub-3940256099942544~3347511713"
        }

        getByName("release") {
            buildConfigField("boolean", "IS_DEBUG", "false")
            buildConfigField("String", "ADMOB_APP_ID", "\"ca-app-pub-1939032811151400~6706129481\"")
            buildConfigField("String", "ADMOB_BANNER_AD_UNIT_ID", "\"ca-app-pub-1939032811151400/5081188516\"")
            buildConfigField("String", "ADMOB_REWARDED_AD_UNIT_ID", "\"ca-app-pub-1939032811151400/2343344537\"")
            manifestPlaceholders["ADMOB_APP_ID"] = "ca-app-pub-1939032811151400~6706129481"
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

// Compose Desktop 설정
compose.desktop {
    application {
        mainClass = "com.cashproject.mongsil.kmp.MainKt"

        nativeDistributions {
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,   // macOS
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,   // Windows
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb,   // Linux
            )
            packageName = "Mongsil"
            packageVersion = "2.0.0"
            description = "몽실 — 일기 앱"
            copyright = "© 2024 Mongsil"

            macOS { bundleID = "com.cashproject.mongsil" }
            windows { menuGroup = "Mongsil" }
            linux { packageName = "mongsil" }
        }
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

val syncXcodeFrameworkForIndexing by tasks.registering(XcodeFrameworkIndexTask::class) {
    configuration.set(providers.environmentVariable("CONFIGURATION"))
    sdkName.set(providers.environmentVariable("SDK_NAME"))
    xcodeFrameworksDir.set(layout.buildDirectory.dir("xcode-frameworks"))
}

tasks.matching { it.name == "embedAndSignAppleFrameworkForXcode" }.configureEach {
    finalizedBy(syncXcodeFrameworkForIndexing)
}

// Room KSP - 각 플랫폼 타겟에 개별 등록 필요
dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
    debugImplementation(libs.androidx.compose.ui.tooling)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.4")
}
