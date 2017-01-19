package com.org.zl.downloaddemo.utils;

import com.org.zl.downloaddemo.base.BaseApplication;
import com.org.zl.downloaddemo.constant.DownLoadConstant;
import com.org.zl.downloaddemo.entity.FileInfo;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * 作者：朱亮 on 2017/1/18 10:47
 * 邮箱：171422696@qq.com
 * ${enclosing_method}(这里用一句话描述这个方法的作用)
 */

public class DownloanThread {
    private Callback.Cancelable cancelable;
    private FileInfo fileInfo;
    private int length;//总长度
    private int downlenth;//下载的长度
    private int downType = 0;//下载的状态(0、等待  1、开始下载  2、下载中  3、下载成功，4、下载失败，5、下载取消)
    private GetFileSharePreance preance;
    public DownloanThread(){
        preance = BaseApplication.getFileSharePreance();
    }
    //调用xutils的下载方法（开始下载）
    public void Download(final FileInfo info){
        this.fileInfo = info;
        final RequestParams params = new RequestParams(info.getUrl());
        params.setSaveFilePath(DownLoadConstant.PATH+info.getFileName());
        params.setCacheMaxAge(3600*1000*24*7); // 默认缓存存活时间(1小时*24小时*7天), 单位:毫秒.(如果服务没有返回有效的max-age或Expires)
        params.setAutoResume(true);     // 是否使用断点下载
        params.setAutoRename(false);    // 下载后是否根据文件的属性自动命名

        cancelable = x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
                LogUtils.e("等待中...");
                downType = 0;
            }
            @Override
            public void onStarted() {
                LogUtils.e("开始下载。。。");
                downType = 1;
            }
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                LogUtils.e("tital = "+total + "   current = "+current + "  isDownLoading = "+isDownloading);
                length = (int) total;
                downlenth = (int) current;
                downType = 2;
                preance.update(new FileInfo(info.getId(),info.getUrl(),info.getFileName(),(int)total,(int)current,downType));
            }

            @Override
            public void onSuccess(File result) {
                LogUtils.e("下载成功");//在这里下载成功的数据写进数据库里即可
                downType = 3;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.e("下载失败");
                downType = 4;
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.e("下载取消");
                downType = 5;
            }

            @Override
            public void onFinished() {
                LogUtils.e("下载完成");
            }
        });

    }
    //获取下载中的文件信息
    public FileInfo getFileInfo(){
        return new FileInfo(fileInfo.getId(),fileInfo.getUrl(),fileInfo.getFileName(),length,downlenth,downType);
    }
    //下载取消（暂停）
    public void downLoadCancel(){
        cancelable.cancel();
    }
}
