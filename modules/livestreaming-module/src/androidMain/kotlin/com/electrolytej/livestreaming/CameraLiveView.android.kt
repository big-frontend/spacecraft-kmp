package com.electrolytej.livestreaming

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

@Composable
actual fun CameraLiveView(modifier: Modifier, stickers: List<Sticker>) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            CameraGLSurfaceView(ctx).apply {
                onSurfaceTextureAvailable = { surfaceTexture ->
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build()
                    preview.setSurfaceProvider { request ->
                        val surface = android.view.Surface(surfaceTexture)
                        request.provideSurface(surface, ContextCompat.getMainExecutor(ctx)) {
                            surface.release()
                        }
                    }
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        },
        update = { view ->
            view.setStickers(stickers)
        }
    )
}

class CameraGLSurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer: CameraRenderer
    var onSurfaceTextureAvailable: ((SurfaceTexture) -> Unit)? = null

    init {
        setEGLContextClientVersion(2)
        renderer = CameraRenderer { surfaceTexture ->
            onSurfaceTextureAvailable?.invoke(surfaceTexture)
        }
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    fun setStickers(stickers: List<Sticker>) {
        renderer.updateStickers(stickers)
    }
}

class CameraRenderer(private val onSurfaceCreated: (SurfaceTexture) -> Unit) : GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {
    private var surfaceTexture: SurfaceTexture? = null
    private var textureId = -1
    private var program = 0
    private var positionHandle = 0
    private var texCoordHandle = 0
    private var matrixHandle = 0
    
    // Sticker handles
    private var stickerProgram = 0
    private var stickerPosHandle = 0
    private var stickerColorHandle = 0
    private var stickerMvpHandle = 0

    private val transformMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private var stickers: List<Sticker> = emptyList()

    private val vertexShaderCode = """
        attribute vec4 vPosition;
        attribute vec2 vTexCoord;
        varying vec2 texCoord;
        uniform mat4 uMatrix;
        void main() {
            gl_Position = vPosition;
            texCoord = (uMatrix * vec4(vTexCoord, 0.0, 1.0)).xy;
        }
    """.trimIndent()

    private val fragmentShaderCode = """
        #extension GL_OES_EGL_image_external : require
        precision mediump float;
        varying vec2 texCoord;
        uniform samplerExternalOES sTexture;
        void main() {
            gl_FragColor = texture2D(sTexture, texCoord);
        }
    """.trimIndent()

    private val stickerVertexShader = """
        attribute vec4 vPosition;
        uniform mat4 uMVPMatrix;
        void main() {
            gl_Position = uMVPMatrix * vPosition;
        }
    """.trimIndent()

    private val stickerFragmentShader = """
        precision mediump float;
        uniform vec4 vColor;
        void main() {
            gl_FragColor = vColor;
        }
    """.trimIndent()

    private val vertexBuffer: FloatBuffer = ByteBuffer.allocateDirect(8 * 4).run {
        order(ByteOrder.nativeOrder())
        asFloatBuffer().apply {
            put(floatArrayOf(-1f, -1f, 1f, -1f, -1f, 1f, 1f, 1f))
            position(0)
        }
    }

    private val texCoordBuffer: FloatBuffer = ByteBuffer.allocateDirect(8 * 4).run {
        order(ByteOrder.nativeOrder())
        asFloatBuffer().apply {
            put(floatArrayOf(0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f))
            position(0)
        }
    }

    fun updateStickers(newStickers: List<Sticker>) {
        this.stickers = newStickers
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        textureId = textures[0]
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST.toFloat())
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR.toFloat())
        
        surfaceTexture = SurfaceTexture(textureId).apply {
            setOnFrameAvailableListener(this@CameraRenderer)
            onSurfaceCreated(this)
        }

        program = createProgram(vertexShaderCode, fragmentShaderCode)
        stickerProgram = createProgram(stickerVertexShader, stickerFragmentShader)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        Matrix.orthoM(projectionMatrix, 0, 0f, 1f, 1f, 0f, -1f, 1f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        surfaceTexture?.updateTexImage()
        surfaceTexture?.getTransformMatrix(transformMatrix)

        // Draw Camera
        GLES20.glUseProgram(program)
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition")
        texCoordHandle = GLES20.glGetAttribLocation(program, "vTexCoord")
        matrixHandle = GLES20.glGetUniformLocation(program, "uMatrix")

        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer)
        GLES20.glEnableVertexAttribArray(texCoordHandle)
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 0, texCoordBuffer)
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, transformMatrix, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        // Draw Stickers
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        GLES20.glUseProgram(stickerProgram)
        stickerPosHandle = GLES20.glGetAttribLocation(stickerProgram, "vPosition")
        stickerColorHandle = GLES20.glGetUniformLocation(stickerProgram, "vColor")
        stickerMvpHandle = GLES20.glGetUniformLocation(stickerProgram, "uMVPMatrix")

        stickers.forEach { drawSticker(it) }
        GLES20.glDisable(GLES20.GL_BLEND)
    }

    private fun drawSticker(sticker: Sticker) {
        val coords = floatArrayOf(
            sticker.x, sticker.y, 0f,
            sticker.x + sticker.width, sticker.y, 0f,
            sticker.x, sticker.y + sticker.height, 0f,
            sticker.x + sticker.width, sticker.y + sticker.height, 0f
        )
        val sb = ByteBuffer.allocateDirect(coords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply { put(coords); position(0) }
        }
        val r = ((sticker.color shr 16) and 0xFF) / 255f
        val g = ((sticker.color shr 8) and 0xFF) / 255f
        val b = (sticker.color and 0xFF) / 255f
        val a = ((sticker.color shr 24) and 0xFF).let { if(it==0) 255 else it } / 255f

        GLES20.glEnableVertexAttribArray(stickerPosHandle)
        GLES20.glVertexAttribPointer(stickerPosHandle, 3, GLES20.GL_FLOAT, false, 0, sb)
        GLES20.glUniform4f(stickerColorHandle, r, g, b, a)
        GLES20.glUniformMatrix4fv(stickerMvpHandle, 1, false, projectionMatrix, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {}

    private fun createProgram(vSource: String, fSource: String): Int {
        val vs = loadShader(GLES20.GL_VERTEX_SHADER, vSource)
        val fs = loadShader(GLES20.GL_FRAGMENT_SHADER, fSource)
        return GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vs)
            GLES20.glAttachShader(it, fs)
            GLES20.glLinkProgram(it)
        }
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also {
            GLES20.glShaderSource(it, shaderCode)
            GLES20.glCompileShader(it)
        }
    }
}
