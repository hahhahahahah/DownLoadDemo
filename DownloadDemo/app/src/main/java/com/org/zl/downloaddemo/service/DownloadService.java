package com.org.zl.downloaddemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.org.zl.downloaddemo.Inteface.OnDownLoadBackListener;
import com.org.zl.downloaddemo.base.BaseApplication;
import com.org.zl.downloaddemo.constant.SPKey;
import com.org.zl.downloaddemo.entity.FileInfo;
import com.org.zl.downloaddemo.utils.DownloanThread;
import com.org.zl.downloaddemo.utils.GetFileSharePreance;
import com.org.zl.downloaddemo.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 作者：朱亮 on 2017/1/17 15:36
 * 邮箱：171422696@qq.com
 * 下载服务类，执行下载任务，并将进度传递到Activity中(这里用一句话描述这个方法的作用)
 */
public class DownloadService extends Service {

	public static final String ACTION_START = "ACTION_START";
	public static final String ACTION_STOP = "ACTION_STOP";
	public static final String ACTION_DELETE = "ACTION_DELETE";
	private List<OnDownLoadBackListener> loadBackListeners = new ArrayList<OnDownLoadBackListener>();//注册一个下载观察者
	private static final int DOWNLOADSERVICEID = 1;//下载监听传递值
	private FileInfo fileInfo;//下载对象
	private MyBinder myBinder = new MyBinder();
	private ArrayList<FileInfo> mCurretList;//当前服务中的正在下载列表
	private ArrayList<FileInfo> mCurretListOut;//输出的下载列表
	private GetFileSharePreance preance;
	private HashMap<Integer,DownloanThread> map;//创建动态对象
	@Override
	public IBinder onBind(Intent intent) {
		return myBinder;
	}

	@Override
	public void onCreate() {
		mCurretList = new ArrayList<>();
		mCurretListOut = new ArrayList<>();
		map = new HashMap<Integer,DownloanThread>();
		preance = BaseApplication.getFileSharePreance();//获取缓存实例，这里的缓存主要用在APP关闭后，重新进入下载列表后显示使用
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			// 获得Activity穿来的参数
			fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
			if (ACTION_START.equals(intent.getAction())) {
				startDownLoad(fileInfo);//开始下载
			} else if (ACTION_STOP.equals(intent.getAction())) {
				stopDownLoad(fileInfo);//停止下载
			} else if (ACTION_DELETE.equals(intent.getAction())) {
				deleteDownLoad(fileInfo);//删除下载
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	//开始下载逻辑处理方法
	private void startDownLoad(FileInfo fileInfo) {
		if (fileInfo != null && !TextUtils.isEmpty(fileInfo.getUrl())) {
			preance.setUrlList(mCurretList);//从缓存地址集合中移除这一条（保存最新的地址集合）
			map.put(fileInfo.getId(),new DownloanThread());
			map.get(fileInfo.getId()).Download(fileInfo);//开始下载
			SPUtils.setLong(getApplicationContext(), SPKey.CURRENTLIST_SIZE, fileInfo.getId());//设置下载ID自增
			sendHandler();
		}
	}
	//停止下载逻辑处理方法
	private void stopDownLoad(FileInfo fileInfo) {
		if (fileInfo != null && !TextUtils.isEmpty(fileInfo.getUrl())) {
			if (map.get(fileInfo.getId()) != null) {
				map.get(fileInfo.getId()).downLoadCancel();//下载线程停止
			}
			sendHandler();
		}
	}

	//删除下载逻辑处理方法
	private void deleteDownLoad(FileInfo fileInfo) {
		if (fileInfo != null && !TextUtils.isEmpty(fileInfo.getUrl())) {
			for (int x = 0; x < mCurretList.size(); x++) {//如果是正在下载的列表，先暂停下载。
				if (mCurretList.get(x).getUrl().equals(fileInfo.getUrl())) {//如果匹配到了
					preance.delete(mCurretList.get(x).getUrl());//删除下载缓存
					if(map.get(fileInfo.getId()) != null){
						map.get(fileInfo.getId()).downLoadCancel();//从下载线程停止
					}
					mCurretList.remove(mCurretList.get(x));//从下载列表移除这条
					preance.setUrlList(mCurretList);//从缓存地址集合中移除这一条（保存最新的地址集合）
					preance.delete(fileInfo.getUrl());//删除缓存数据
				}
			}
			sendHandler();
		}
	}


	public class MyBinder extends Binder {
		//注册一个下载观察者
		public void registDownLoadListener(OnDownLoadBackListener loadBackListener) {
			loadBackListeners.add(loadBackListener);
		}
		//取消一个观察者
		public void unregistDownLoadListener(OnDownLoadBackListener loadBackListener) {
			loadBackListeners.remove(loadBackListener);
		}
		//设置当前的下载列表（会清空当前的下载列表哦，慎用，如果需要请用  appendToCurrentList（））
		public void setCurrentList(ArrayList<FileInfo> list) {
			mCurretList.clear();
			if (list != null) {
				mCurretList.addAll(list);
			}
		}
		//获取当前的正在下载的所有下载信息列表
		public ArrayList<FileInfo> getCurrentList() {
			return mCurretList;
		}
		//从缓存中获取下载信息
		public ArrayList<FileInfo> getCurrentListFShareP(){
			ArrayList<FileInfo> infos = new ArrayList<>();
			ArrayList<String> list = preance.getUrlList();//先获取下载url集合
			for(int x = 0 ; x < list.size() ; x++){//根据url地址查询下载的缓存数据
				infos.add(preance.getFilePre(list.get(x)));
			}
			setCurrentList(infos);
			return infos;
		}
		//下载次数自增
		public int getCurrentListSize() {
			long l = SPUtils.getLong(getApplicationContext(), SPKey.CURRENTLIST_SIZE);
			return (int) l + 1;
		}
		//添加当前的下载列表(相同下载地址文件过滤)
		public void appendToCurrentList(FileInfo info) {
			if (info != null) {
				// 只添加当前下载列表中没有的
				boolean existed = false;
				for (int j = 0; j < mCurretList.size(); j++) {
					if ((mCurretList.get(j).getUrl()).equals(info.getUrl())) {
						existed = true;
					}
				}
				if (!existed) {
					mCurretList.add(info);
					startDownLoad(info);//开始下载
				}
			}
		}
		//停止下载
		public void stopDownLoad(FileInfo fileInfo) {
			if (fileInfo != null && !TextUtils.isEmpty(fileInfo.getUrl())) {
				stopDownLoad(fileInfo);
			}
		}
		//删除当前的某一个列表
		public void deleyeCurrentList(FileInfo fileInfo) {
			deleteDownLoad(fileInfo);
		}
		//删除所有的下载列表信息（未编写）
		public void deleteAll(){

		}
	}


	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case DOWNLOADSERVICEID:
					for (int x = 0; x < loadBackListeners.size(); x++) {//有多少个界面在观察下载数据
						mCurretListOut.clear();
						for(int j = 0 ; j < mCurretList.size() ;j++){//正在下载列表中的数据
							if(map.get(mCurretList.get(j).getId()) == null){//假如下载线程没有开启
								mCurretListOut.add(mCurretList.get(j));
							}else {//从下载线程中取数据
								mCurretListOut.add(map.get(mCurretList.get(j).getId()).getFileInfo());
							}
						}
						loadBackListeners.get(x).onDownloadSize(mCurretListOut);//传递给不同的观察者
					}
					//当下载的时候，延迟不断执行这句话，让所有的观察者不至于失去下载的各种信息，这里控制视图更新频率
					handler.sendEmptyMessageDelayed(
							DOWNLOADSERVICEID, 1000);
					break;
				default:
					super.handleMessage(msg);
			}
		}
	};
	//发送hanlde
	private void sendHandler(){
		if (!handler.hasMessages(DOWNLOADSERVICEID)) {//如果hanlde的观察者没有了，重新发送一个
			handler.sendEmptyMessage(DOWNLOADSERVICEID);
		}
	}
}