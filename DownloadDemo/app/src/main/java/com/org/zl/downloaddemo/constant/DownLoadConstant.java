package com.org.zl.downloaddemo.constant;

import android.os.Environment;

import java.io.File;

/**
 * 作者：朱亮 on 2017/1/17 15:36
 * 邮箱：171422696@qq.com
 * ${enclosing_method}(这里用一句话描述这个方法的作用)
 */

public class DownLoadConstant {
    public static String PATH = getSDPath()+"/ZLdownDemo/";
    //权限sd卡写权限key常量
    public static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 100;
    //权限录音权限key常量
    public static int RECORD_AUDIO = 101;
    //相册权限key常量
    public static int RESULT_CODE_STARTCAMERA = 102;
    /**
     * 获取sd卡根目录
     *
     * @return
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }
}
