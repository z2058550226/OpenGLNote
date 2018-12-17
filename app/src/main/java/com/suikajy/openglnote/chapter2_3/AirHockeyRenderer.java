package com.suikajy.openglnote.chapter2_3;

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
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.suikajy.openglnote.global.Config.DEBUG;

/**
 * Created by suikajy on 2018.12.13
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4; // 一个java float占32位，也就是4个字节
    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private int uColorLocation;
    private int aPositionLocation;
    private final FloatBuffer vertexData;
    // 存储链接程序ID
    private int program;
    private Context mContext;


    // 定义三角形的时候总是以逆时针的顺序排列顶点，这称为卷曲顺序（winding order）。
    // 在任何地方都使用这种顺序可以优化性能
    float[] tableVerticesWithTriangles = {
            // Triangle 1
            -0.5f, -0.5f,
            0.5f, 0.5f,
            -0.5f, 0.5f,

            // Triangle 2
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f, 0.5f,

            // Line 1
            -0.5f, 0f,
            0.5f, 0f,

            // Mallets
            0f, -0.25f,
            0f, 0.25f,

            // Border backRect
            -0.55f, -0.55f,
            0.55f, 0.55f,
            -0.55f, 0.55f,

            -0.55f, -0.55f,
            0.55f, -0.55f,
            0.55f, 0.55f
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
                .readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(mContext, R.raw.simple_fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        // 通过链接程序把着色器链接起来
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        if (DEBUG) {
            ShaderHelper.validateProgram(program);
        }
        glUseProgram(program);

        // 调用glGetUniformLocation获取uniform位置。
        uColorLocation = glGetUniformLocation(program, U_COLOR);
        // 获取属性位置
        aPositionLocation = glGetAttribLocation(program, A_POSITION);

        // 关联属性与顶点数据的数组
        // 移动缓冲区指针的位置到开头处
        vertexData.position(0);
        // 告诉OpenGL到vertexData里面去寻找顶点数据。
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, 0, vertexData);

        // 使能顶点数组
        glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // 设置视口（ViewPort）尺寸
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清空屏幕，用来擦除屏幕上所有的颜色，并使用glClearColor来填充整个屏幕
        glClear(GL_COLOR_BUFFER_BIT);

        // 绘制桌子背景边框
        glUniform4f(uColorLocation, 0.375f, 0.75f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 10, 6);

        // 绘制桌子
        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 0, 6);

        // 绘制分割线
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);

        // 绘制第一个蓝色木槌
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);

        // 绘制第二个红色木槌
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);

    }
}
