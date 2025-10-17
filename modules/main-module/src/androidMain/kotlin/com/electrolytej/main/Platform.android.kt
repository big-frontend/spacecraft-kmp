package com.electrolytej.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
actual fun getWebScreen(controller: NavController) {
}