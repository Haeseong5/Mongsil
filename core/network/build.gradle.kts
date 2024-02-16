plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
//    id("com.google.dagger.hilt.android")
    kotlin("kapt")
    kotlin("plugin.serialization") version kotlinVersion
}

android {
    namespace = "com.cashproject.mongsil.network"
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

    //coil
    implementation(coil)

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

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-config-ktx")


    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //hilt
    implementation(hiltAndroid)
    kapt(hiltKapt)

}