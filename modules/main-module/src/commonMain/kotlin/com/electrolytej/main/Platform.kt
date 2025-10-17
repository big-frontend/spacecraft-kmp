package com.electrolytej.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

@Composable
expect fun getWebScreen(controller: NavController)