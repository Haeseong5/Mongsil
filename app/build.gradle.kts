plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    kotlin("plugin.serialization") version kotlinVersion
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = sdkCompileVersion

    defaultConfig {
        applicationId = "com.cashproject.mongsil"
        minSdk = sdkMinVersion
        targetSdk = sdkTargetVersion
        versionCode = 20
        versionName = "1.1.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = compose_compiler_version
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
        getByName("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    namespace = "com.cashproject.mongsil"
}

dependencies {
    //module
    implementation(project(":tedadmobdialog"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:common"))

    //kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    //androidx
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation(Dependency.KTX.CORE)
    implementation(androidMaterial)
    implementation("androidx.annotation:annotation:1.7.0")

    //lifeCycle
    implementation(Dependency.AndroidX.LIFECYCLE.RUNTIME)
    implementation(Dependency.AndroidX.LIFECYCLE.LIVEDATA)
    implementation(Dependency.AndroidX.LIFECYCLE.VIEWMODEL)
    implementation(Dependency.AndroidX.LIFECYCLE.VIEWMODEL_COMPOSE)

    //Room
    implementation(Dependency.AndroidX.ROOM.RUNTIME)
    implementation(Dependency.AndroidX.ROOM.KTX)
    implementation(Dependency.AndroidX.ROOM.RXJAVA2)
    kapt(Dependency.AndroidX.ROOM.COMPILER)
    implementation(Dependency.AndroidX.ROOM.BACKUP_AND_RESTORE)
    androidTestImplementation(Dependency.AndroidX.ROOM.TESTING)

    //Compose
    implementation(platform(compose_bom))
    implementation(composeRuntime)
    implementation(composeRuntimeLivedata)
    implementation(composeUi)
    implementation(composeFoundation)
    implementation(composeFoundationLayout)
    implementation(composeMaterial)
    implementation(composeMaterial3)
    implementation(composeUiViewBinding)
    implementation(composeUiTooling)
    implementation(composeUiToolingPreview)
    implementation(activityCompose)
    androidTestImplementation(platform(compose_bom))
    androidTestImplementation(composeUiTest)

    implementation("com.github.GIGAMOLE:ComposeFadingEdges:1.0.4")
    implementation("com.kizitonwose.calendar:compose:2.4.0")

    //Ads
    implementation("com.google.android.gms:play-services-ads:$googleAdVersion")
    implementation("com.google.firebase:firebase-ads:$googleAdVersion")

    //Coroutines
    implementation(coroutinesCore)
    implementation(coroutinesAndroid)
    implementation(coroutinesRx2)
    implementation(coroutinesTest)
    implementation(coroutinePlayServices)

    //Rx
    implementation(rxjava2)
    implementation(rxjava2Android)
    implementation(rxjava2Kotlin)
    implementation("com.jakewharton.rxbinding3:rxbinding:3.1.0")
    implementation("com.jakewharton.rxbinding3:rxbinding-material:3.1.0")

    //hilt
    implementation(hiltAndroid)
    kapt(hiltKapt)

    //navigation
    implementation(navigationFragmentKtx)
    implementation(navigationUiKtx)
    implementation(navigationDynamicFeaturesFragment)
    implementation(navigationCompose)
    androidTestImplementation(navigationTesting)

    //network & json
    implementation(retrofit2)
    implementation(retrofit2ConverterGson)
    implementation(retrofit2RxJava)
    implementation(retrofit2ConverterScarlars)
    implementation(kotlinxSerialization)
    implementation(kotlinxSerializationConverter)
    implementation(platform(okhttpBom))
    implementation(okhttp)
    implementation(okhttpLoggingIntercepter)

    //glide
    implementation(glide)
    kapt(glideCompiler)

    //coil
    implementation(coil)

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-config-ktx")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics-ktx")
}