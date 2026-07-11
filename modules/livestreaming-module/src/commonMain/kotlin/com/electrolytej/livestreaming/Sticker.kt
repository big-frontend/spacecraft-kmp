package com.electrolytej.livestreaming

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class Sticker(
    val id: String,
    val imageUrl: String = "",
    val x: Float, // 0.0 to 1.0
    val y: Float, // 0.0 to 1.0
    val width: Float, // 0.0 to 1.0
    val height: Float, // 0.0 to 1.0
    val color: Int = 0xFFFFFF00.toInt()
)

@Composable
expect fun LiveStickerView(modifier: Modifier, stickers: List<Sticker>)
