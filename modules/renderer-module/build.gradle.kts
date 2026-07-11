import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    val appleTargets = listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    appleTargets.forEach {
        it.binaries.framework {
            baseName = "RendererModule"
            isStatic = true
        }
    }

    jvm("desktop")

    listOf(
        androidNativeArm64(),
    ).forEach { androidNativeTarget ->
        androidNativeTarget.compilations.getByName("main") {
            cinterops {
                val vulkan by creating {
                    defFile(project.file("src/nativeInterop/cinterop/vulkan.def"))
                }
                val libcurl by creating {
                    defFile(project.file("src/nativeInterop/cinterop/libcurl.def"))
                }
            }
        }
        androidNativeTarget.binaries.sharedLib {
            linkerOpts("-lvulkan", "-landroid", "-llog")
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
        }

        val appleMain by creating {
            dependsOn(commonMain)
        }

        val iosMain by creating {
            dependsOn(appleMain)
        }

        val iosX64Main by getting { dependsOn(iosMain) }
        val iosArm64Main by getting { dependsOn(iosMain) }
        val iosSimulatorArm64Main by getting { dependsOn(iosMain) }

        val desktopMain by getting {
            dependsOn(commonMain)
        }

        val androidNativeMain by creating {
            dependsOn(commonMain)
        }
        
        val androidNativeArm64Main by getting {
            dependsOn(androidNativeMain)
        }
    }
}

android {
    namespace = "com.electrolytej.renderer"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
