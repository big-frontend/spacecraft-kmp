package com.electrolytej.main

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.electrolytej.web.WebScreen
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
actual fun getWebScreen(controller: NavController) {
    WebScreen(controller)
}

@Composable
actual fun OpenGLView(modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            GLSurfaceView(context).apply {
                setEGLContextClientVersion(2)
                setRenderer(object : GLSurfaceView.Renderer {
                    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
                        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f)
                    }

                    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
                        GLES20.glViewport(0, 0, width, height)
                    }

                    override fun onDrawFrame(gl: GL10?) {
                        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
                    }
                })
            }
        }
    )
}