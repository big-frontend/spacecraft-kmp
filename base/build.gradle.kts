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
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Base"
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
            api("androidx.annotation:annotation:1.9.1")
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material)
            api(compose.ui)
            api(compose.components.resources)
            api(compose.components.uiToolingPreview)
            api(libs.androidx.lifecycle.viewmodel)
            api(libs.androidx.lifecycle.runtime.compose)
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
            api("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")
            api("io.coil-kt.coil3:coil-compose:3.0.4")
            api("io.coil-kt.coil3:coil-svg:3.0.4")
            api(libs.ktor.client.core)
            api(libs.ktor.client.logging)
            api(libs.ktor.client.content.negotiation)
            api(libs.ktor.serialization.kotlinx.json)
            api(libs.ktor.serialization.kotlinx.protobuf)
            api(libs.kermit)
        }
        androidMain.dependencies {
            api("io.coil-kt.coil3:coil-network-okhttp:3.0.4")
            api("io.coil-kt.coil3:coil-gif:3.0.4")
            api("io.coil-kt.coil3:coil-video:3.0.4")
            api(libs.androidx.lifecycle.viewmodel)
            api(libs.androidx.compose.ui.tooling.preview)
            api(libs.androidx.activity.compose)
            api("androidx.databinding:viewbinding:8.1.4")
//            implementation(libs.ktor.client.okhttp)
            api(libs.ktor.client.android)
            api(libs.kotlinx.coroutines.android)
            implementation("androidx.graphics:graphics-core:1.0.3")
            implementation("androidx.graphics:graphics-path:1.1.0-alpha01")
            implementation("androidx.graphics:graphics-shapes:1.1.0")
        }
        appleMain {
            dependencies {
                api("io.coil-kt.coil3:coil-network-ktor3:3.0.4")
                implementation(libs.ktor.client.darwin)
            }
        }
        val desktopMain by getting {
            dependencies {
                api(libs.ktor.client.cio)
                api("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")
                api(libs.kotlinx.coroutines.swing)
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
                api(libs.ktor.client.wasm)
            }
        }
//        jvmMain {
//            dependencies {
//                api("io.coil-kt.coil3:coil-network-okhttp:3.0.4")
//                api(libs.kotlinx.coroutines.swing)
//            }
//        }


    }
}

android {
    namespace = "com.electrolytej.kmp.base"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
tasks.withType<Wrapper> {
    gradleVersion = "8.1.1"
    distributionType = Wrapper.DistributionType.BIN
}
