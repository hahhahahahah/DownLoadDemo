package com.org.zl.downloaddemo.base;

import android.app.Application;

import com.org.zl.downloaddemo.utils.GetFileSharePreance;

import org.xutils.x;


/**
 * 作者：朱亮 on 2017/1/17 15:15
 * 邮箱：171422696@qq.com
 * ${enclosing_method}(这里用一句话描述这个方法的作用)
 */

public class BaseApplication extends Application {
    private static BaseApplication mApplication;
    private GetFileSharePreance preance;
    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        x.Ext.init(this);
    }
    public synchronized static BaseApplication getApplicationInstance() {
        return mApplication;
    }
    public synchronized static GetFileSharePreance getFileSharePreance(){
        return new GetFileSharePreance(mApplication);
    }

}
