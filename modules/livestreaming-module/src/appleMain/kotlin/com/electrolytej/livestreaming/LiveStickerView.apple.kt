package com.electrolytej.livestreaming

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIColor
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun LiveStickerView(modifier: Modifier, stickers: List<Sticker>) {
    UIKitView(
        modifier = modifier,
        factory = {
            UIView().apply {
                backgroundColor = UIColor.clearColor
            }
        },
        update = { view ->
            // Clear existing sticker views
            view.subviews.forEach { 
                (it as? UIView)?.removeFromSuperview() 
            }
            
            val screenWidth = view.frame.useContents { size.width }
            val screenHeight = view.frame.useContents { size.height }
                
            stickers.forEach { sticker ->
                val stickerView = UIView().apply {
                    val x = sticker.x * screenWidth
                    val y = sticker.y * screenHeight
                    val w = sticker.width * screenWidth
                    val h = sticker.height * screenHeight
                    
                    setFrame(CGRectMake(x, y, w, h))
                    
                    // Map the color (Int) to UIColor
                    val r = ((sticker.color shr 16) and 0xFF).toDouble() / 255.0
                    val g = ((sticker.color shr 8) and 0xFF).toDouble() / 255.0
                    val b = (sticker.color and 0xFF).toDouble() / 255.0
                    val a = ((sticker.color shr 24) and 0xFF).let { 
                        if (it == 0) 1.0 else it.toDouble() / 255.0 
                    }
                    
                    backgroundColor = UIColor.colorWithRed(r, g, b, a)
                }
                view.addSubview(stickerView)
            }
        }
    )
}
