@file:Suppress("UnstableApiUsage")

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

    defaultConfig {
        applicationId = "com.bzlzhh.plugin.ngg"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "Development 0.3.0"

        manifestPlaceholders["des"] = "Krypton Wrapper (OpenGL 3.0+)"
        manifestPlaceholders["renderer"] = "NGGL4ES:libng_gl4es.so:libEGL.so"
        manifestPlaceholders["boatEnv"] = mutableMapOf<String, String>().apply {
            put("LIBGL_USE_MC_COLOR", "1")
            put("DLOPEN", "libspirv-cross-c-shared.so")
            put("LIBGL_GL", "30")
            put("LIBGL_ES", "3")
            put("LIBGL_NORMALIZE", "1")
            put("LIBGL_NOINTOVLHACK", "1")
            put("LIBGL_NOERROR", "1")
        }.run {
            var env = ""
            forEach { (key, value) ->
                env += "$key=$value:"
            }
            env.dropLast(1)
        }
        manifestPlaceholders["pojavEnv"] =
            manifestPlaceholders["boatEnv"] as String +
                    (mutableMapOf<String, String>().apply {
                        put("POJAV_RENDERER", "opengles3")
                    }.run {
                        var env = ":"
                        forEach { (key, value) ->
                            env += "$key=$value:"
                        }
                        env.dropLast(1)
                    })

        buildConfigField("boolean", "useANGLE", "false")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
        configureEach {
            resValue("string", "app_name", "Krypton Wrapper")
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
    implementation(libs.okhttp)
    implementation(libs.core)
    implementation(libs.ext.strikethrough)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.core)
    implementation(libs.material)
    implementation(project(":NGG"))
}
