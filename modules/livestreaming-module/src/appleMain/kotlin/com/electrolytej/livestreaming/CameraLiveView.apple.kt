package com.electrolytej.livestreaming

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.*
import platform.AVFoundation.*
import platform.CoreVideo.*
import platform.CoreMedia.*
import platform.CoreGraphics.*
import platform.QuartzCore.*
import platform.UIKit.*
import platform.Foundation.*
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraLiveView(modifier: Modifier, stickers: List<Sticker>) {
    val cameraManager = remember { CameraManager() }

    DisposableEffect(Unit) {
        cameraManager.startSession()
        onDispose {
            cameraManager.stopSession()
        }
    }

    UIKitView(
        modifier = modifier,
        factory = {
            cameraManager.previewView
        },
        update = { view ->
            cameraManager.updateStickers(stickers)
        }
    )
}

@OptIn(ExperimentalForeignApi::class)
class CameraManager {
    val previewView = UIView().apply {
        backgroundColor = UIColor.blackColor
    }
    private val stickerOverlay = UIView().apply {
        backgroundColor = UIColor.clearColor
    }
    private val captureSession = AVCaptureSession()
    private var previewLayer: AVCaptureVideoPreviewLayer? = null
    private var stickers: List<Sticker> = emptyList()

    init {
        setupCamera()
        previewView.addSubview(stickerOverlay)
    }

    private fun setupCamera() {
        val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo) ?: return
        val input = AVCaptureDeviceInput.deviceInputWithDevice(device, null) as? AVCaptureDeviceInput ?: return
        
        if (captureSession.canAddInput(input)) {
            captureSession.addInput(input)
        }

        previewLayer = AVCaptureVideoPreviewLayer.layerWithSession(captureSession).apply {
            videoGravity = AVLayerVideoGravityResizeAspectFill
        }
        previewView.layer.addSublayer(previewLayer!!)
        
        // Match sticker overlay frame to preview view
        stickerOverlay.setFrame(previewView.bounds)
    }

    fun startSession() {
        dispatch_async(dispatch_get_main_queue()) {
            if (!captureSession.isRunning()) {
                captureSession.startRunning()
            }
        }
    }

    fun stopSession() {
        if (captureSession.isRunning()) {
            captureSession.stopRunning()
        }
    }

    fun updateStickers(newStickers: List<Sticker>) {
        this.stickers = newStickers
        
        // Clear existing sticker views
        stickerOverlay.subviews.forEach { (it as? UIView)?.removeFromSuperview() }
        
        val width = previewView.frame.useContents { size.width }
        val height = previewView.frame.useContents { size.height }

        stickers.forEach { sticker ->
            val stickerView = UIView().apply {
                val x = sticker.x * width
                val y = sticker.y * height
                val w = sticker.width * width
                val h = sticker.height * height
                setFrame(CGRectMake(x, y, w, h))
                
                val r = ((sticker.color shr 16) and 0xFF).toDouble() / 255.0
                val g = ((sticker.color shr 8) and 0xFF).toDouble() / 255.0
                val b = (sticker.color and 0xFF).toDouble() / 255.0
                val a = ((sticker.color shr 24) and 0xFF).let { if(it==0) 255 else it }.toDouble() / 255.0
                backgroundColor = UIColor.colorWithRed(r, g, b, a)
            }
            stickerOverlay.addSubview(stickerView)
        }
        
        // Ensure preview layer fills the view
        previewLayer?.setFrame(previewView.bounds)
        stickerOverlay.setFrame(previewView.bounds)
    }
}
