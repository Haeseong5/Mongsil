plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    kotlin("plugin.serialization") version kotlinVersion
    id("kotlin-parcelize")
}

android {
    namespace = "com.cashproject.mongsil.database"
    compileSdk = sdkCompileVersion

    defaultConfig {
        minSdk = sdkMinVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core:common"))

    implementation(Dependency.KTX.CORE)
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    //Room
    implementation(Dependency.AndroidX.ROOM.RUNTIME)
    implementation(Dependency.AndroidX.ROOM.KTX)
    implementation(Dependency.AndroidX.ROOM.RXJAVA2)
    kapt(Dependency.AndroidX.ROOM.COMPILER)
    implementation(Dependency.AndroidX.ROOM.BACKUP_AND_RESTORE)
    androidTestImplementation(Dependency.AndroidX.ROOM.TESTING)

    implementation(kotlinxSerialization)
//    implementation(kotlinxSerializationConverter)
}