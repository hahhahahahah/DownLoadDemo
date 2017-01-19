package com.org.zl.downloaddemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.org.zl.downloaddemo.R;
import com.org.zl.downloaddemo.base.BaseFragment;

/**
 * Created by QCY on 2016/12/24.
 * 下载完成显示界面
 */

public class DownLoadFinishFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_down, container, false);//关联布局文件
        return rootView;
    }
}
