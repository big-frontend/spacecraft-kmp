package com.electrolytej.livestreaming

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

@Composable
actual fun LiveStickerView(modifier: Modifier, stickers: List<Sticker>) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            StickerGLSurfaceView(context).apply {
                setStickers(stickers)
            }
        },
        update = { view ->
            view.setStickers(stickers)
        }
    )
}

class StickerGLSurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer: StickerRenderer

    init {
        setEGLContextClientVersion(2)
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        holder.setFormat(android.graphics.PixelFormat.TRANSLUCENT)
        setZOrderOnTop(true)
        renderer = StickerRenderer()
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    fun setStickers(stickers: List<Sticker>) {
        renderer.updateStickers(stickers)
    }
}

class StickerRenderer : GLSurfaceView.Renderer {
    private var program = 0
    private var positionHandle = 0
    private var colorHandle = 0
    private var mvpMatrixHandle = 0

    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    private var stickers: List<Sticker> = emptyList()

    private val vertexShaderCode = """
        uniform mat4 uMVPMatrix;
        attribute vec4 vPosition;
        void main() {
          gl_Position = uMVPMatrix * vPosition;
        }
    """.trimIndent()

    private val fragmentShaderCode = """
        precision mediump float;
        uniform vec4 vColor;
        void main() {
          gl_FragColor = vColor;
        }
    """.trimIndent()

    fun updateStickers(newStickers: List<Sticker>) {
        this.stickers = newStickers
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        GLES20.glUseProgram(program)
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition")
        colorHandle = GLES20.glGetUniformLocation(program, "vColor")
        mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")

        stickers.forEach { sticker ->
            drawSticker(sticker)
        }
    }

    private fun drawSticker(sticker: Sticker) {
        // Simple square as a placeholder for the sticker
        val coords = floatArrayOf(
            sticker.x * 2 - 1, (1 - sticker.y) * 2 - 1, 0f,
            (sticker.x + sticker.width) * 2 - 1, (1 - sticker.y) * 2 - 1, 0f,
            sticker.x * 2 - 1, (1 - (sticker.y + sticker.height)) * 2 - 1, 0f,
            (sticker.x + sticker.width) * 2 - 1, (1 - (sticker.y + sticker.height)) * 2 - 1, 0f
        )
        
        val vertexBuffer = ByteBuffer.allocateDirect(coords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(coords)
                position(0)
            }
        }

        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer)
        GLES20.glUniform4fv(colorHandle, 1, floatArrayOf(1.0f, 1.0f, 0.0f, 1.0f), 0)
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, vPMatrix, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        GLES20.glDisableVertexAttribArray(positionHandle)
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }
}
