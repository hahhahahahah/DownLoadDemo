package com.org.zl.downloaddemo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.org.zl.downloaddemo.R;
import com.org.zl.downloaddemo.base.BaseActivity;
import com.org.zl.downloaddemo.constant.DownLoadConstant;
import com.org.zl.downloaddemo.entity.FileInfo;
import com.org.zl.downloaddemo.service.DownloadService;
import com.org.zl.downloaddemo.utils.CheckSelfPermission;
import com.org.zl.downloaddemo.utils.LogUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private String urlone = "http://abv.cn/music/千千阙歌.mp3";
    private String urltwo = "http://abv.cn/music/光辉岁月.mp3";
    private String urlthree = "http://abv.cn/music/红豆.mp3";
    private DownloadService.MyBinder mBinder;
    private Button button1,button2,button3,button6;

    @Override
    public void onPressBackButton() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initService();
        initCol();//初始化控件
        initCheckSelfPermission();//判断SD卡权限
    }
    private void initService() {
        Intent intent = new Intent(getContext(), DownloadService.class);
        bindService(intent,mDownLoadConnection, Context.BIND_AUTO_CREATE);
    }

    /** 与Service连接时交互的类 */
    // （未完成）
    private ServiceConnection mDownLoadConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            //            LogUtils.e("DownLoad    绑定成功，获取service实例");
            mBinder = (DownloadService.MyBinder)service;
        }
        // 与服务端连接异常丢失时才调用，调用unBindService不调用此方法哎
        public void onServiceDisconnected(ComponentName className) {
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                goToDown(urlone);
                break;
            case R.id.button2:
                goToDown(urltwo);
                break;
            case R.id.button3:
                goToDown(urlthree);
                break;
            case R.id.button6:
                IntentActivity(DownLoadActivity.class);
                break;
        }
    }
    private void goToDown(String url){
        if(mBinder == null){
            Toast.makeText(getContext(),"Service 初始化失败",Toast.LENGTH_SHORT).show();
            return;
        }
        mBinder.appendToCurrentList(new FileInfo(mBinder.getCurrentListSize(),url,getfileName(url),0,0,0));
        IntentActivity(DownLoadActivity.class);
    }

    // 從URL地址中獲取文件名，即/後面的字符（这个根据情况来命名，可以更改）
    private String getfileName(String url) {//根据下载地址给下载文件命名
        try{
            return url.substring(url.lastIndexOf("/") + 1);
        }catch (Exception ex){
            return mBinder.getCurrentListSize()+"";//返回一个数字
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(getBaseContext(),"已获取SD卡权限",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getBaseContext(),"SD卡权限未开启",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //判断权限方法
    private void initCheckSelfPermission() {
        //判断是否有内存卡权限
        CheckSelfPermission.getSDCard(this);
    }
    //动态权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }
    //动态权限回调，yes和no点击
    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == DownLoadConstant.WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LogUtils.d("权限获取成功");
                handler.sendEmptyMessage(0);
            } else {
                handler.sendEmptyMessage(1);
            }
        }
    }
    private void initCol() {
        button1 = (Button) findViewById(R.id.button1);button1.setOnClickListener(this);
        button2 = (Button) findViewById(R.id.button2);button2.setOnClickListener(this);
        button3 = (Button) findViewById(R.id.button3);button3.setOnClickListener(this);
        button6 = (Button) findViewById(R.id.button6);button6.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        unbindService(mDownLoadConnection);
        super.onDestroy();
    }
}
