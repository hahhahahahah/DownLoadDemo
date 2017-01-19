package com.org.zl.downloaddemo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.org.zl.downloaddemo.constant.DownLoadConstant;


/**
 * @product: QCY_Sport
 * @Description: 权限检测(用一句话描述该文件做什么)
 * @author: 朱亮(171422696@qq.com)
 * Date: 2016-11-23
 * Time: 18:16
 * @company:蓝米科技 version        V1.0
 */
public class CheckSelfPermission {

    //判断是否有内外置sd卡写权限
    public static void getSDCard(Activity context ){

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                //申请SD卡权限
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION},
                        DownLoadConstant.WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }
    //判断是否有应用录音权限
    public static boolean getRecordAudio(Activity context ){

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ) {
            //提示用户开户权限音频
            String[] perms = {"android.permission.RECORD_AUDIO"};
            ActivityCompat.requestPermissions(context,perms, DownLoadConstant.RECORD_AUDIO);
            return false;
        }else{
            return true;
        }
    }


    //判断是否应用拍照权限
    public static void getCamera(Activity context){

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED  ) {
            //提示用户开户权限
            String[] perms = {"android.permission.CAMERA"};
            ActivityCompat.requestPermissions(context,perms, DownLoadConstant.RESULT_CODE_STARTCAMERA);
        }
    }


}
