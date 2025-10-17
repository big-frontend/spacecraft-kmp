package com.electrolytej.main.page.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.electrolytej.main.Greeting
import org.jetbrains.compose.resources.painterResource
import spacecraft_kmp.modules.main_module.generated.resources.Res
import spacecraft_kmp.modules.main_module.generated.resources.compose_multiplatform
import androidx.navigation.NavController
import com.electrolytej.main.network.api.AccountApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.protobuf.protobuf

@Composable
fun HomeScreen(controller: NavController) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("basic", color = Color.White) },
            backgroundColor = Color(0xff0f9d58),
            navigationIcon = {
                IconButton(onClick = { controller.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        tint = Color.White,
                        contentDescription = "back button"
                    )
                }
            }
        )

    }, content = { MyContent(controller) })
}
@Composable
fun MyContent(controller: NavController) {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                showContent = !showContent
                runCatching {
                    val httpClient = HttpClient {
                        install(DefaultRequest) {
                            url {
                                protocol = URLProtocol.HTTP  // HTTP协议
                                host = "192.168.31.229"      // 本地IP
                                port = 8088                  // 本地端口
                            }
                        }
                        install(Logging) {
                            level = LogLevel.ALL
                        }
                        install(ContentNegotiation) {
                            protobuf()
                        }
                        controller.navigate("web")
                    }
//                    httpClient.get("random-cat").body()

                }
            }) {
                val ssoSignInUrl = AccountApi().getSSOSignInUrl()
                println(ssoSignInUrl)
                Text("Click me! ${ssoSignInUrl}")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")

                }
            }
        }
    }
}