package com.electrolytej.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

@Composable
expect fun getWebScreen(controller: NavController)

@Composable
expect fun OpenGLView(modifier: Modifier)