import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose)
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
//
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
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
//    linuxX64() {
//        val main by compilations.getting {
//            cinterops {
//                val libcurl by creating {
//                    defFile(project.file("src/nativeInterop/cinterop/libcurl.def"))
////                    packageName("com.spacecraft.kmp")
//                    compilerOpts("-I/src/nativeInterop/cinterop/")
//                    includeDirs.allHeaders("src/nativeInterop/cinterop/")
//                }
//            }
//        }
//        binaries {
//            executable {
//                entryPoint = "main"
//            }
//        }
//    }
//    mingwX64() // on Windows
//    macosX64() { // on macOS
//        binaries {
//            executable()
//        }
//    }
//    androidNativeArm64()
//    androidNativeArm32 {
//        binaries {
//            sharedLib("aa", listOf(RELEASE))
//        }
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
//    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "base-module"
            isStatic = true
        }
    }
    jvm("desktop")
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.library()
    }
    sourceSets {
        commonMain.dependencies {
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material)
            api(compose.ui)
            api(compose.components.resources)
            api(compose.components.uiToolingPreview)
            api(libs.androidx.lifecycle.viewmodel)
            api(libs.androidx.lifecycle.runtime.compose)
            api("io.coil-kt.coil3:coil-compose:3.0.4")
            api("io.coil-kt.coil3:coil-svg:3.0.4")
            api("io.coil-kt.coil3:coil-compose:3.0.4")
//            implementation("com.otaliastudios.opengl:egloo-multiplatform:0.6.1")
        }
        androidMain.dependencies {
            api("io.coil-kt.coil3:coil-network-okhttp:3.0.4")
            api("io.coil-kt.coil3:coil-gif:3.0.4")
            api("io.coil-kt.coil3:coil-video:3.0.4")
            api(libs.androidx.lifecycle.viewmodel)
            api(libs.androidx.compose.ui.tooling.preview)
            api(libs.androidx.activity.compose)
            api("androidx.databinding:viewbinding:8.1.4")
        }
        appleMain {
            dependencies {
                api("io.coil-kt.coil3:coil-network-ktor3:3.0.4")
//                api("io.ktor:ktor-client-darwin:3.0.4")
            }
        }
        val desktopMain by getting {
            dependencies {

            }
        }
//        jsMain {
//            dependencies {
//                api("io.coil-kt.coil3:coil-network-ktor3:3.0.4")
//
//            }
//        }
        wasmJsMain {
            dependencies {
                api("io.coil-kt.coil3:coil-network-ktor3:3.0.4")
            }
        }
        jvmMain {
            dependencies {
                api("io.coil-kt.coil3:coil-network-okhttp:3.0.4")
                api(libs.kotlinx.coroutines.swing)
            }
        }



    }
}

android {
    namespace = "com.electrolytej.kmp.base"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
tasks.withType<Wrapper> {
    gradleVersion = "8.1.1"
    distributionType = Wrapper.DistributionType.BIN
}
