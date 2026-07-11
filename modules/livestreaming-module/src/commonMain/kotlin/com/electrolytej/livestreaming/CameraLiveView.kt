package com.electrolytej.livestreaming

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun CameraLiveView(modifier: Modifier, stickers: List<Sticker>)
