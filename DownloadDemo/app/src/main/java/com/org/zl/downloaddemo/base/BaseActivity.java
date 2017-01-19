package com.org.zl.downloaddemo.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.org.zl.downloaddemo.R;
import com.org.zl.downloaddemo.activity.DownLoadActivity;
import com.org.zl.downloaddemo.activity.MainActivity;
import com.org.zl.downloaddemo.utils.LogUtils;

/**
 * @product: QCY_Sport
 * @Description: activtiy基础类(用一句话描述该文件做什么)
 * @author: 朱亮(171422696@qq.com)
 * Date: 2016-11-22
 * Time: 13:43
 * @company:蓝米科技 version        V1.0
 */
public abstract class BaseActivity extends AppCompatActivity {
    public Context mContext;
    /** 手势检测 */
    private GestureDetectorCompat mDetector = null;
    private LinearLayout title;//标题
    private TextView tv_base_back,base_title,base_name;//左边
    public abstract void onPressBackButton();//左
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }
    public Context getContext() {
        return mContext;
    }

    //跳转activity
    public void IntentActivity(Class<?> cls) {
        startActivity(new Intent(getContext(), cls));
    }

    /**
     * 初始化标题
     * 要有layout_title布局
     */
    public void initTitle() {
        tv_base_back = (TextView) findViewById(R.id.image_base_back);
        tv_base_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPressBackButton();
            }
        });

    }
    public void setTitle(String str1,int id1,String str2,int id2){
        title = (LinearLayout) findViewById(R.id.tv_base_title);
        title.setVisibility(View.VISIBLE);
        base_title = (TextView)findViewById(R.id.base_title);
        base_title.setVisibility(id1);
        base_name = (TextView)findViewById(R.id.base_name);
        base_name.setVisibility(id2);

        base_title.setText(str1);
        base_name.setText(str2);
    }

    //i   有两种 0 和 1 ，0为左向右滑动检测,1为右向左滑动检测
    public void initGestureDetector(View view, final int i) {
        view.setOnTouchListener(new View.OnTouchListener() {//最底部的控件设置触摸监控
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });
        // 设置滑动手势
        mDetector = new GestureDetectorCompat(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                LogUtils.e("-----------------------------------onDown  ");
                return false;
            }
            @Override
            public void onShowPress(MotionEvent e) {
                LogUtils.e("-----------------------------------onShowPress  ");
            }
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                LogUtils.e("-----------------------------------onSingleTapUp  ");
                return false;
            }
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }
            @Override
            public void onLongPress(MotionEvent e) {
                LogUtils.e("-----------------------------------onLongPress  ");
            }
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                LogUtils.e("-----------------------------------onFling  ");
                switch (i){
                    case 0:
                        // 从左向右滑动
                        if (e1 != null && e2 != null) {
                            if (e2.getX()- e1.getX()  > 100) {
                                switchToMain();
                                return true;
                            }
                        }
                        break;
                    case 1:
                        // 从右向左滑动
                        if (e1 != null && e2 != null) {
                            if (e1.getX()- e2.getX()  > 100) {
                                switchToPlayer();
                                return true;
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }

    public void switchToMain() {
        LogUtils.e("-----------------------------------返回主界面");
        Intent intent = new Intent(this,
                MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        this.finish();
    }

    public void switchToPlayer() {
        startActivity(new Intent(mContext, DownLoadActivity.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
