package com.suikajy.openglnote.ch1_prepare

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.toast
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Ch1Activity : AppCompatActivity() {

    private lateinit var glSurfaceView: GLSurfaceView
    private var renderSet = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glSurfaceView = GLSurfaceView(this)

        if (checkSupportOes()) {
            // Request an OpenGL ES 2.0 compatible context.
            glSurfaceView.setEGLContextClientVersion(2)

            // Assign our renderer
            glSurfaceView.setRenderer(FirstOpenGlProjectRender())
            renderSet = true
        } else {
            toast("This device does not support OpenGL ES 2.0.")
            return
        }
        setContentView(glSurfaceView)
    }

    // check if the device system support openGL ES 2.0
    private fun checkSupportOes(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val configurationInfo = activityManager.deviceConfigurationInfo

        return configurationInfo.reqGlEsVersion >= 0x20000
    }

    class FirstOpenGlProjectRender : GLSurfaceView.Renderer {
        override fun onDrawFrame(gl: GL10?) {
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        }
    }
}
