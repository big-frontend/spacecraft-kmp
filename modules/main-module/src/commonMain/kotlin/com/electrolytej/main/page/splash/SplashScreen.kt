package com.electrolytej.main.page.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SplashScreen(controller: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Button(onClick = {
            controller.navigate("home")
        }) {
            Text("splash", fontSize = 18.sp)
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    // Created a separate preview function to provide a NavController instance,
    // as Compose Preview cannot automatically provide one for parameters.
    SplashScreen(controller = rememberNavController())
}