import java.util.Properties

plugins {
    id("com.android.application")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "be.mygod.vpnhotspot"

    val javaVersion = 11
    buildToolsVersion = "34.0.0"
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility(javaVersion)
        targetCompatibility(javaVersion)
    }
    kotlinOptions.jvmTarget = javaVersion.toString()
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = javaVersion.toString()
    }
    compileSdk = 34
    defaultConfig {
        applicationId = "be.mygod.vpnhotspot"
        minSdk = 28
        targetSdk = 34
        versionCode = 1018
        versionName = "2.17.4"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ksp {
            arg("room.expandProjection", "true")
            arg("room.incremental", "true")
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
    buildFeatures {
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }
    buildTypes {
        debug {
            isPseudoLocalesEnabled = true
            isDebuggable = false
        }
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            vcsInfo.include = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    packagingOptions.resources.excludes.addAll(listOf(
        "**/*.kotlin_*",
        "META-INF/versions/**",
    ))
    lint.warning += "FullBackupContent"
    lint.warning += "UnsafeOptInUsageError"
    flavorDimensions.add("freedom")
    productFlavors {
        create("freedom") {
            dimension = "freedom"
            resourceConfigurations.addAll(arrayOf("it", "pt-rBR", "ru", "zh-rCN", "zh-rTW"))
        }
        create("google") {
            dimension = "freedom"
            versionNameSuffix = "-g"
            val prop = Properties().apply {
                val f = rootProject.file("local.properties")
                if (f.exists()) load(f.inputStream())
            }
            if (prop.containsKey("codeTransparency.storeFile")) bundle.codeTransparency.signing {
                storeFile = file(prop["codeTransparency.storeFile"]!!)
                storePassword = prop["codeTransparency.storePassword"] as? String
                keyAlias = prop["codeTransparency.keyAlias"] as? String
                keyPassword = if (prop.containsKey("codeTransparency.keyPassword")) {
                    prop["codeTransparency.keyPassword"] as? String
                } else storePassword
            }
        }
    }
    sourceSets.getByName("androidTest").assets.srcDir("$projectDir/schemas")
}

dependencies {
    val lifecycleVersion = "2.7.0"
    val roomVersion = "2.6.1"

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation(kotlin("stdlib-jdk8"))
    implementation("androidx.browser:browser:1.8.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.preference:preference:1.2.1")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("be.mygod.librootkotlinx:librootkotlinx:1.1.1")
    implementation("com.android.billingclient:billing-ktx:6.2.0")
    implementation("com.google.android.gms:play-services-base:18.3.0")  // fix for GoogleApiActivity crash @ 18.1.0+
    implementation("com.google.android.gms:play-services-oss-licenses:17.0.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.firebase:firebase-analytics:21.5.1")
    implementation("com.google.firebase:firebase-crashlytics:18.6.2")
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.linkedin.dexmaker:dexmaker:2.28.3")
    implementation("com.takisoft.preferencex:preferencex-simplemenu:1.1.0")
    implementation("dnsjava:dnsjava:3.5.3")
    implementation("io.ktor:ktor-network-jvm:2.3.8")
    implementation("org.lsposed.hiddenapibypass:hiddenapibypass:4.3")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    add("googleImplementation", "com.google.android.play:app-update-ktx:2.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.room:room-testing:$roomVersion")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
}
