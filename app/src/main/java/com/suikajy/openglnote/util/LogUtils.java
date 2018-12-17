package com.suikajy.openglnote.util;

import android.util.Log;

import static com.suikajy.openglnote.global.Config.DEBUG;


/**
 * @author zjy
 * @date 2016/12/26
 */
public class LogUtils {
    private static String className;//类名
    private static String methodName;//方法名
    private static int lineNumber;//行数
    private static final String prefixFilter = "GLNoteLog ";

    private LogUtils() {
        /* Protect from instantiations */
    }

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[" + methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")").append("]--  ");
        buffer.append(log);
        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }


    public static void e(String message) {
        if (!DEBUG) return;
        String fMessage;
        if (message == null) {
            fMessage = "null";
        } else {
            fMessage = message;
        }
        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(prefixFilter + className, createLog(fMessage));
        //LogTools.i(prefixFilter + className + " : " + fMessage);
    }

    public static void e(String... args) {
        if (!DEBUG) {
            return;
        }
        if (args.length == 0) {
            getMethodNames(new Throwable().getStackTrace());
            Log.e(prefixFilter + className, createLog("test log"));
//            LogTools.i("test log");
            return;
        }
        StringBuilder msg = new StringBuilder(args[0]);
        String separator = " \n";
        for (int i = 1; i < args.length; i++) {
            msg.append(separator).append(args[i]);
        }
        getMethodNames(new Throwable().getStackTrace());

        String log = createLog(msg.toString());
        Log.e(prefixFilter + className, log);
//        LogTools.i(prefixFilter + className + " : " + log);
    }

    public static void i(String message) {
        if (DEBUG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.i(prefixFilter + className, createLog(message));
        }
    }

    public static void d(String message) {
        if (DEBUG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.d(prefixFilter + className, createLog(message));
        }
    }

    public static void v(String message) {
        if (DEBUG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.v(prefixFilter + className, createLog(message));
        }
    }

    public static void w(String message) {
        if (DEBUG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.w(prefixFilter + className, createLog(message));
        }
    }

    public static void wtf(String message) {
        if (DEBUG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.wtf(prefixFilter + className, createLog(message));
        }
    }
}
