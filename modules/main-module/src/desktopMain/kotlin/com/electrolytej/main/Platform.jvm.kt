package com.electrolytej.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

@Composable
actual fun getWebScreen(controller: NavController) {
//    Scaffold(topBar = {
//        TopAppBar(
//            title = { Text("basic", color = Color.White) },
//            backgroundColor = Color(0xff0f9d58),
//            navigationIcon = {
//                IconButton(onClick = { controller.popBackStack() }) {
//                    Icon(
//                        imageVector = Icons.Filled.ArrowBack,
//                        tint = Color.White,
//                        contentDescription = "back button"
//                    )
//                }
//            }
//        )
//
//    }, content = { WebScreen(controller) })
//    WebScreen(controller)
    val state = rememberWebViewState("https://example.com")

    WebView(state)
}