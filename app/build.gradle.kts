@file:Suppress("UnstableApiUsage")
var useANGLE by extra(true)

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.bzlzhh.plugin.ngg"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    flavorDimensions.add("useANGLE")

    productFlavors {
        create("ANGLE") {
            dimension = "useANGLE"
            useANGLE = true
            buildConfigField("boolean", "useANGLE", "true")
        }
        create("NO-ANGLE") {
            dimension = "useANGLE"
            useANGLE = false
            buildConfigField("boolean", "useANGLE", "false")
        }
    }
    
    defaultConfig {
        applicationId = if (useANGLE) "com.bzlzhh.plugin.ngg" else "com.bzlzhh.plugin.ngg.angleless"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "Release 0.1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
        configureEach {
            resValue("string","app_name","Krypton Wrapper")
            if (useANGLE) {
                manifestPlaceholders["des"] = "Krypton Wrapper (OpenGL ~3.0+)"
                manifestPlaceholders["renderer"] = "NGGL4ES:libng_gl4es.so:libEGL_angle.so"
            } else {
                manifestPlaceholders["des"] = "Krypton Wrapper, NO-ANGLE (OpenGL ~3.0+)"
                manifestPlaceholders["renderer"] = "NGGL4ES:libng_gl4es.so:libEGL.so"
            }
            manifestPlaceholders["boatEnv"] = mutableMapOf<String,String>().apply {
                if(useANGLE) {
                    put("LIBGL_EGL","libEGL_angle.so")
                    put("LIBGL_GLES","libGLESv2_angle.so")
                }
                put("LIBGL_USE_MC_COLOR","1")
                put("DLOPEN","libspirv-cross-c-shared.so")
                put("LIBGL_GL","30")
                put("LIBGL_ES","3")
                put("LIBGL_MIPMAP","3")
                put("LIBGL_NORMALIZE","1")
                put("LIBGL_NOINTOVLHACK","1")
                put("LIBGL_NOERROR","1")
            }.run {
                var env = ""
                forEach { (key, value) ->
                    env += "$key=$value:"
                }
                env.dropLast(1)
            }

            manifestPlaceholders["pojavEnv"] = mutableMapOf<String,String>().apply {
                if(useANGLE) {
                    put("LIBGL_EGL","libEGL_angle.so")
                    put("LIBGL_GLES","libGLESv2_angle.so")
                }
                put("LIBGL_USE_MC_COLOR","1")
                put("DLOPEN","libspirv-cross-c-shared.so")
                put("LIBGL_GL","30")
                put("LIBGL_ES","3")
                put("LIBGL_MIPMAP","3")
                put("LIBGL_NORMALIZE","1")
                put("LIBGL_NOINTOVLHACK","1")
                put("LIBGL_NOERROR","1")
                put("POJAV_RENDERER","opengles3")
            }.run {
                var env = ""
                forEach { (key, value) ->
                    env += "$key=$value:"
                }
                env.dropLast(1)
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("io.noties.markwon:core:4.6.2")
    implementation("io.noties.markwon:ext-strikethrough:4.6.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("androidx.core:core:1.13.1")
}