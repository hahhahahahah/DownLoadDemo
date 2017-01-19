package com.org.zl.downloaddemo.Inteface;

import com.org.zl.downloaddemo.entity.FileInfo;

import java.util.ArrayList;

/**
 * 作者：朱亮 on 2017/1/17 16:16
 * 邮箱：171422696@qq.com
 * 传递下载进度接口(这里用一句话描述这个方法的作用)
 */

public interface OnDownLoadBackListener {
    public void onDownloadSize(ArrayList<FileInfo> list);
}
