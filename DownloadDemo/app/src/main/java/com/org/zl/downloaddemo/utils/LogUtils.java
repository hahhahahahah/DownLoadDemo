package com.org.zl.downloaddemo.utils;

import android.util.Log;

import com.org.zl.downloaddemo.base.BaseApplication;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * @author Aaron
 * @Description: 日志工具类
 * @date Nov 13, 2014 9:28:55 AM
 */

public class LogUtils {

    public static final String LOG_TAG = BaseApplication.getApplicationInstance().getPackageName();

    public static Boolean DEBUG = null;

    public static boolean isShowLogCat() {
        if (DEBUG == null) {
            DEBUG = true;
        }

        return DEBUG;
    }

    public static String formatLog(String message) {
        return "[" + Thread.currentThread().getId() + "] " + message;
    }

    public static boolean d() {
        return isShowLogCat();
    }

    public static void v(String message) {
        v(LOG_TAG, message);
    }

    public static void v(String tag, String message) {
        if (d()) {
            Log.v(tag, formatLog(message));
        }
    }

    public static void d(String message) {
        d(LOG_TAG, message);
    }

    public static void d(String tag, String message) {
        if (d()) {
            String text = formatLog(message);
            Log.d(tag, text);
        }
    }

    public static void i(String message) {
        int maxLogSize = 1000;
        if (d()) {
            String text = formatLog(message);
            for (int i = 0; i <= text.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > text.length() ? text.length() : end;
                Log.i(LOG_TAG, text.substring(start, end));
            }
//            Log.i(LOG_TAG, text);
        }

    }

    public static void i(String tag, String message) {
        int maxLogSize = 1000;
        if (d()) {
            String text = formatLog(message);
            for (int i = 0; i <= text.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > text.length() ? text.length() : end;
                Log.i(tag, text.substring(start, end));
            }
//            Log.i(LOG_TAG, text);
        }
    }

    public static void w(String message) {
        if (d()) {
            String text = formatLog(message);
            Log.w(LOG_TAG, text);
        }
    }

    public static void w(String tag, String message) {
        if (d()) {
            String text = formatLog(message);
            Log.w(tag, text);
        }
    }

    public static void w(Throwable e) {
        w(getStringFromThrowable(e));
    }

    public static void e(String message) {
        if (d()) {
            String text = formatLog(message);
            Log.e(LOG_TAG, text);
        }
    }

    public static void e(String tag, String message) {
        if (d()) {
            String text = formatLog(message);
            Log.e(tag, text);
        }
    }

    public static void e(Throwable e) {
        e(getStringFromThrowable(e));
    }

    public static void e(Throwable e, String message) {
        e(message + "\n" + getStringFromThrowable(e));
    }

    public static String getStringFromThrowable(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}

