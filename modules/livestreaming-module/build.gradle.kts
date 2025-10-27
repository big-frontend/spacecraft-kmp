import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    //    val hostOs = System.getProperty("os.name")
//    val isArm64 = System.getProperty("os.arch") == "aarch64"
//    val isMingwX64 = hostOs.startsWith("Windows")
//    val nativeTarget = when {
//        hostOs == "Mac OS X" && isArm64 -> macosArm64("native")
//        hostOs == "Mac OS X" && !isArm64 -> macosX64("native")
//        hostOs == "Linux" && isArm64 -> linuxArm64("native")
//        hostOs == "Linux" && !isArm64 -> linuxX64("native")
//        isMingwX64 -> mingwX64("native")
//        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
//    }
//    nativeTarget.apply {
//        compilations.getByName("main") {
//            cinterops {
//                val libcurl by creating {
//                    defFile(project.file("src/nativeInterop/cinterop/libcurl.def"))
//                    packageName("com.spacecraft.kmp")
//                    compilerOpts("-I/path")
//                    includeDirs.allHeaders("path")
//                }
//            }
//        }
//        binaries {
//            executable {
//                entryPoint = "main"
//            }
//        }
//    }

//    listOf(
//        linuxX64(),
//        mingwX64(),//on Windows
//        macosX64()// on macOS
//    ).forEach { nativeTarget ->
//        val main by nativeTarget.compilations.getting {
//            cinterops {
//                val libcurl by creating {
//                    defFile(project.file("src/nativeInterop/cinterop/libcurl.def"))
////                    packageName("com.spacecraft.kmp")
//                    compilerOpts("-I/src/nativeInterop/cinterop/")
//                    includeDirs.allHeaders("src/nativeInterop/cinterop/")
//                }
//            }
//        }
//        nativeTarget.binaries {
//            executable {
//                entryPoint = "main"
//            }
//        }
//    }

    //指定main函数入口
//    fun KotlinNativeTarget.config() {
//        binaries {
//            executable {
//                entryPoint = "main"
//            }
//        }
//    }
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    listOf(
        androidNativeArm64(),
    ).forEach { androidTarget ->
        androidTarget.compilations.getByName("main") {
            cinterops {
                val libcurl by creating{}
//                val libcurl by creating {
//                    defFile(project.file("src/nativeInterop/cinterop/libcurl.def"))
//                    packageName("com.electrolytej.kmp")
//                    compilerOpts("-I/path")
//                    includeDirs.allHeaders("path")
//                }
            }
        }
        androidTarget.binaries {
            sharedLib("aa", listOf(RELEASE))
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        //        iosTarget.compilations.getByName("main") {
//            cinterops {
//                val libcurl by creating {
//                    defFile(project.file("src/nativeInterop/cinterop/libcurl.def"))
//                    packageName("com.electrolytej.kmp")
//                    compilerOpts("-I/path")
//                    includeDirs.allHeaders("path")
//                }
//            }
//        }
        it.binaries.framework {
            baseName = "LiveStreamingModule"
            isStatic = true
        }
    }
    jvm("desktop")

    sourceSets {
        nativeMain.dependencies {
//            implementation("com.otaliastudios.opengl:egloo-multiplatform:0.6.1")
        }
        androidNativeMain{
            dependencies {
                // Kotlin Multiplatform projects: or use the granular dependencies:
//                implementation("com.otaliastudios.opengl:egloo-android:0.6.1") // Android AAR
//                implementation("com.otaliastudios.opengl:egloo-androidnativex86:0.6.1") // Android Native KLib
//                implementation("com.otaliastudios.opengl:egloo-androidnativex64:0.6.1") // Android Native KLib
//                implementation("com.otaliastudios.opengl:egloo-androidnativearm32:0.6.1") // Android Native KLib
//                implementation("com.otaliastudios.opengl:egloo-androidnativearm64:0.6.1") // Android Native KLib
            }
        }
    }
}

android {
    namespace = "com.electrolytej.livestreaming"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
tasks.withType<Wrapper> {
    gradleVersion = "8.14"
    distributionType = Wrapper.DistributionType.BIN
}
