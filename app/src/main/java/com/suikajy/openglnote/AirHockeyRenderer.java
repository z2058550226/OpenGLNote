package com.suikajy.openglnote;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by suikajy on 2018.12.13
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4; // 一个java float占32位，也就是4个字节
    private final FloatBuffer vertexData;

    // 定义三角形的时候总是以逆时针的顺序排列顶点，这称为卷曲顺序（winding order）。
    // 在任何地方都使用这种顺序可以优化性能
    float[] tableVerticesWithTriangles = {
            // Triangle 1
            0f, 0f,
            9f, 14f,
            0f, 14f,

            // Triangle 2
            0f, 0f,
            9f, 0f,
            9f, 14f
    };

    float[] middleLine = {
            // Line 1
            0f, 7f,
            9f, 7f,

            // Mallets
            4.5f, 2f,
            4.5f, 12f
    };

    public AirHockeyRenderer() {
        float[] tableVertices = {
                0f, 0f,
                0f, 14f,
                9f, 14,
                9f, 0
        };
        vertexData = ByteBuffer
                .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        // 将Java数据复制到本地内存
        // 程序如果运行时间过长会创建很多ByteBuffer，需要学习一些碎片化和内存管理技术。
        vertexData.put(tableVerticesWithTriangles);
    }

    /**
     * 当Surface被创建的时候，GlSurfaceView 会调用这个方法，并且当Activity重新可见
     * 时候也会被调用。
     *
     * @param gl
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 清空屏幕颜色为红色
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // 设置视口（ViewPort）尺寸
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清空屏幕，用来擦除屏幕上所有的颜色，并使用glClearColor来填充整个屏幕
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }
}
