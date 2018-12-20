package com.suikajy.openglnote.chapter5;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.suikajy.openglnote.R;
import com.suikajy.openglnote.util.ShaderHelper;
import com.suikajy.openglnote.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.suikajy.openglnote.global.Config.DEBUG;

/**
 * Created by suikajy on 2018.12.13
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int BYTES_PER_FLOAT = 4; // 一个java float占32位，也就是4个字节
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private static final String A_COLOR = "a_Color";
    private static final String A_POSITION = "a_Position";
    private int aColorLocation;
    private int aPositionLocation;
    private final FloatBuffer vertexData;
    // 存储链接程序ID
    private int program;
    private Context mContext;

    // 引入三角形扇，重用顶点坐标
    float[] tableVerticesWithTriangles = {
            // Order of coordinates: X, Y, R, G, B

            // Triangle Fan
            0, 0, 1f, 1f, 1f,
            -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
            0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
            -0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,

            // Line 1
            -0.5f, 0f, 1f, 0f, 0f,
            0.5f, 0f, 1f, 0f, 0f,

            // Mallets
            0f, -0.25f, 0f, 0f, 1f,
            0f, 0.25f, 1f, 0f, 0f
    };

    public AirHockeyRenderer(Context context) {
        this.mContext = context;
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
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        String vertexShaderSource = TextResourceReader
                .readTextFileFromResource(mContext, R.raw.simple_vertex_shader2);
        String fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(mContext, R.raw.simple_fragment_shader2);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        // 通过链接程序把着色器链接起来
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        if (DEBUG) {
            ShaderHelper.validateProgram(program);
        }
        glUseProgram(program);

        aColorLocation = glGetAttribLocation(program, A_COLOR);
        // 获取属性位置
        aPositionLocation = glGetAttribLocation(program, A_POSITION);

        // 关联属性与顶点数据的数组
        // 移动缓冲区指针的位置到开头处
        vertexData.position(0);
        // 告诉OpenGL到vertexData里面去寻找顶点数据，并加入跨距
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);

        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);

        // 使能顶点位置属性
        glEnableVertexAttribArray(aPositionLocation);
        // 使能颜色属性
        glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // 设置视口（ViewPort）尺寸
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        // 绘制桌子
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        // 绘制分割线
        glDrawArrays(GL_LINES, 6, 2);

        // 绘制第一个蓝色木槌
        glDrawArrays(GL_POINTS, 8, 1);

        // 绘制第二个红色木槌
        glDrawArrays(GL_POINTS, 9, 1);
    }
}
