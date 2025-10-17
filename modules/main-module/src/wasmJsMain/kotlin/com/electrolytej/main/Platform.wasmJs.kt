package com.electrolytej.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavController


class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()
@Composable
actual fun getWebScreen(controller: NavController){

}