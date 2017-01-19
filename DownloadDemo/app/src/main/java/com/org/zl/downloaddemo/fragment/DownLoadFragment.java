package com.org.zl.downloaddemo.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.org.zl.downloaddemo.Inteface.OnDownLoadBackListener;
import com.org.zl.downloaddemo.R;
import com.org.zl.downloaddemo.adapter.FileAdapter;
import com.org.zl.downloaddemo.base.BaseFragment;
import com.org.zl.downloaddemo.entity.FileInfo;
import com.org.zl.downloaddemo.service.DownloadService;

import java.util.ArrayList;

/**
 * Created by QCY on 2016/12/24.
 * 实际下载界面
 */

public class DownLoadFragment extends BaseFragment {
    private ListView listView;
    private FileAdapter mAdapter;
    private DownloadService.MyBinder mBinder;
    private ArrayList<FileInfo> mFileList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_down, container, false);//关联布局文件
        // 初始化控件
        listView = (ListView) rootView.findViewById(R.id.list_view);
        mAdapter = new FileAdapter(getContext());
        listView.setAdapter(mAdapter);
        initService();//这个界面绑定service
        return rootView;
    }
    private void initService() {
        Intent intent = new Intent(getContext(), DownloadService.class);
        getActivity().bindService(intent,mDownLoadConnection, Context.BIND_AUTO_CREATE);
    }
    /** 与Service连接时交互的类 */
    private ServiceConnection mDownLoadConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBinder = (DownloadService.MyBinder)service;
            mBinder.registDownLoadListener(loadBackListener);//注册观察者
            mFileList = mBinder.getCurrentList();//先从service中获取当前列表
            if(mFileList.size() == 0){
                mFileList = mBinder.getCurrentListFShareP();
            }
            mAdapter.setData(mFileList);
        }
        // 与服务端连接异常丢失时才调用，调用unBindService不调用此方法哎
        public void onServiceDisconnected(ComponentName className) {
            mBinder.unregistDownLoadListener(loadBackListener);
        }
    };
    //下载列表状态回调
    private OnDownLoadBackListener loadBackListener = new OnDownLoadBackListener(){
        @Override
        public void onDownloadSize(ArrayList<FileInfo> list) {
            if(mAdapter != null){
                mAdapter.setData(list);
            }
        }
    };
    @Override
    public void onDestroy() {
        getActivity().unbindService(mDownLoadConnection);//解绑service
        if(mBinder != null){
            mBinder.unregistDownLoadListener(loadBackListener);//注销观察者
        }
        super.onDestroy();
    }
}
