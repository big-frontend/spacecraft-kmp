package com.electrolytej.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

@Composable
actual fun getWebScreen(controller: NavController){

}