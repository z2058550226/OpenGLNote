package com.suikajy.openglnote.util;

import timber.log.Timber;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

/**
 * Created by suikajy on 2018.12.14
 */
public class ShaderHelper {
    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    // 编译着色器
    private static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = glCreateShader(type);
        if (shaderObjectId == 0) {
            Timber.e("Could not create new shader");
            return 0;
        }
        // 有了着色器对象之后就可以让它读入shaderCode
        glShaderSource(shaderObjectId, shaderCode);
        // 读入代码之后就可以编译这个着色器了
        glCompileShader(shaderObjectId);
        // 读取编译状态：
        final int[] compileStatus = new int[1];
        // 0表示在compileStatus数组的第0个位置存储GL_COMPILE_STATUS信息
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);
        // 获取着色信息
        Timber.e("Results of compiling source:\n" + shaderCode + "\n:" +
                glGetShaderInfoLog(shaderObjectId));
        // 验证编译状态：
        if (compileStatus[0] == 0) {
            // If it failed, delete the shader object
            glDeleteShader(shaderObjectId);
            Timber.e("Compilation of shader failed.");
            return 0;
        }
        // 如果没问题，就把这个id返回回来
        return shaderObjectId;
    }

    // 链接到程序
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        // 新建程序对象
        final int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            Timber.e("Could not create new program");
            return 0;
        }
        // 附上着色器
        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);

        // 链接程序
        glLinkProgram(programObjectId);
        // 检查链接是否成功
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
        Timber.e("Results of linking program:\n" + glGetProgramInfoLog(programObjectId));

        // 验证链接状态并返回程序对象ID
        if (linkStatus[0] == 0) {
            // if it failed, delete the program object
            Timber.e("Linking of program failed.");
            return 0;
        }

        return programObjectId;
    }

    // 验证OpenGL程序对象
    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        Timber.e("Results of validating program: " + validateStatus[0] + "\nLog:" + glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }
}
