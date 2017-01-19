package com.org.zl.downloaddemo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.org.zl.downloaddemo.R;
import com.org.zl.downloaddemo.adapter.MyFragmentPagerAdapter;
import com.org.zl.downloaddemo.base.BaseActivity;
import com.org.zl.downloaddemo.fragment.DownLoadFinishFragment;
import com.org.zl.downloaddemo.fragment.DownLoadFragment;

import java.util.ArrayList;

/**
 * 作者：朱亮 on 2017/1/17 16:30
 * 邮箱：171422696@qq.com
 * ${enclosing_method}(这里用一句话描述这个方法的作用)
 */

public class DownLoadActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private DownLoadFinishFragment finishFragment;
    private DownLoadFragment loadFragment;
    private ArrayList<Fragment> fragmentList;
    private TextView loading, loaded;

    @Override
    public void onPressBackButton() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down);
        initTitle();
        setTitle("下载列表", View.VISIBLE, "", View.GONE);
        init();
    }

    private void init() {
        fragmentList = new ArrayList<Fragment>();
        loadFragment = new DownLoadFragment();
        fragmentList.add(loadFragment);
        finishFragment = new DownLoadFinishFragment();
        fragmentList.add(finishFragment);
        viewPager = (ViewPager) findViewById(R.id.download_viewPager);
        //给ViewPager设置适配器
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());//页面变化时的监听器
        loading = (TextView) findViewById(R.id.loading);
        loading.setOnClickListener(this);
        loaded = (TextView) findViewById(R.id.loaded);
        loaded.setOnClickListener(this);
        setLoadingWhite();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loading:
                setLoadingWhite();
                break;
            case R.id.loaded:
                setLoadedWhite();
                break;
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {        }
        @Override
        public void onPageScrollStateChanged(int arg0) {        }
        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    setLoadingWhite();
                    break;
                case 1:
                    setLoadedWhite();
                    break;
            }
        }
    }
    private void setLoadingWhite() {
        viewPager.setCurrentItem(0);//设置当前显示标签页为第一页
        loading.setTextColor(getResources().getColor(R.color.colorWhite));
        loaded.setTextColor(getResources().getColor(R.color.color_black));
    }

    private void setLoadedWhite() {
        viewPager.setCurrentItem(1);//设置当前显示标签页为第二页
        loading.setTextColor(getResources().getColor(R.color.color_black));
        loaded.setTextColor(getResources().getColor(R.color.colorWhite));
    }
}

