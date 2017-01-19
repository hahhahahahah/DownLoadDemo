package com.org.zl.downloaddemo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.org.zl.downloaddemo.Inteface.DialogLinster;
import com.org.zl.downloaddemo.R;
import com.org.zl.downloaddemo.dialog.Dialog;
import com.org.zl.downloaddemo.entity.FileInfo;
import com.org.zl.downloaddemo.service.DownloadService;
import com.org.zl.downloaddemo.utils.FileUtil;
import com.org.zl.downloaddemo.utils.LogUtils;
import com.org.zl.downloaddemo.utils.NetUtil;

import java.util.ArrayList;

public class FileAdapter extends BaseAdapter {

	private Context mContext = null;
	private ArrayList<FileInfo> mFilelist = null;
	private LayoutInflater layoutInflater;
	private int[] currentSize ;
	private AlertDialog dialog;

	public FileAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		layoutInflater = LayoutInflater.from(mContext);
		mFilelist = new ArrayList<FileInfo>();
	}
	public void setData(ArrayList<FileInfo> list){
		if(list == null){
			return;
		}
		mFilelist = list;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return mFilelist.size();
	}

	@Override
	public Object getItem(int position) {
		return mFilelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null) {
			convertView = layoutInflater.inflate(R.layout.music_down_item, null);
			viewHolder = new ViewHolder();
			currentSize = new int[mFilelist.size()];
			viewHolder.textview = (TextView) convertView.findViewById(R.id.file_textview);
			viewHolder.startButton = (ImageView) convertView.findViewById(R.id.start_button);
			viewHolder.stop_button = (ImageView) convertView.findViewById(R.id.stop_button);
			viewHolder.deleteButton = (ImageView) convertView.findViewById(R.id.delete_button);
			viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar2);
			viewHolder.netSpeed = (TextView) convertView.findViewById(R.id.netSpeed);
			viewHolder.netSizw = (TextView) convertView.findViewById(R.id.netSizw);

			final ViewHolder finalViewHolder = viewHolder;

			viewHolder.startButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finalViewHolder.startButton.setEnabled(false);//互相设置点击事件可不可以点击
					finalViewHolder.stop_button.setEnabled(true);
					//通知service开始命令
					Intent intent = new Intent(mContext, DownloadService.class);
					intent.setAction(DownloadService.ACTION_START);
					intent.putExtra("fileInfo", mFilelist.get(position));
					LogUtils.e("------------------发送开始 = "+mFilelist.get(position).toString());
					mContext.startService(intent);
				}
			});

			viewHolder.stop_button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finalViewHolder.stop_button.setEnabled(false);//互相设置点击事件可不可以点击
					finalViewHolder.startButton.setEnabled(true);
					//通知service停止下载
					Intent intent = new Intent(mContext, DownloadService.class);
					intent.setAction(DownloadService.ACTION_STOP);
					intent.putExtra("fileInfo", mFilelist.get(position));
					mContext.startService(intent);
					LogUtils.e("------------------发送停止 = "+mFilelist.get(position).toString());
				}
			});

			viewHolder.deleteButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					LogUtils.e("观测当前对象状态 = "+mFilelist.get(position).toString());
					dialog = Dialog.getDownLoadDialog(mContext, mFilelist.get(position).getFileName()+"吗？", new DialogLinster() {
						@Override
						public void Success() {
							dialog.dismiss();
							//通知service停止下载
							Intent intent = new Intent(mContext, DownloadService.class);
							intent.setAction(DownloadService.ACTION_DELETE);
							intent.putExtra("fileInfo", mFilelist.get(position));
							mContext.startService(intent);
							LogUtils.e("------------------发送删除命令 = "+mFilelist.get(position).toString());
						}
						@Override
						public void Failed() {
							dialog.dismiss();
						}
					});
					dialog.show();
				}
			});
			convertView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.textview.setText(mFilelist.get(position).getFileName());

		switch (mFilelist.get(position).getDownType()){
			case 0:
				viewHolder.netSpeed.setTextColor(Color.WHITE);
				viewHolder.netSpeed.setText("准备下载...");
				break;
			case 1:
				viewHolder.startButton.setVisibility(View.GONE);
				viewHolder.stop_button.setVisibility(View.VISIBLE);
				viewHolder.netSpeed.setTextColor(Color.GREEN);
				viewHolder.netSpeed.setText("开始下载");
				break;
			case 2:
				viewHolder.startButton.setVisibility(View.GONE);
				viewHolder.stop_button.setVisibility(View.VISIBLE);
				if(currentSize[position] == 0){
					currentSize[position] = mFilelist.get(position).getDownlenth();
				}else if(mFilelist.get(position).getDownlenth() - currentSize[position] <= 0){
					viewHolder.netSpeed.setVisibility(View.GONE);
				}else{
					viewHolder.netSpeed.setVisibility(View.VISIBLE);
					viewHolder.netSpeed.setText(NetUtil.FormetFileSize(mFilelist.get(position).getDownlenth() - currentSize[position])+"/s");
					viewHolder.netSpeed.setTextColor(Color.GREEN);
					currentSize[position] = mFilelist.get(position).getDownlenth();
				}
				break;
			case 3:
				//通知service停止下载
				Intent intent = new Intent(mContext, DownloadService.class);
				intent.setAction(DownloadService.ACTION_DELETE);
				intent.putExtra("fileInfo", mFilelist.get(position));
				mContext.startService(intent);
				break;
			case 4:
				viewHolder.startButton.setEnabled(true);;
				viewHolder.startButton.setVisibility(View.VISIBLE);
				viewHolder.stop_button.setEnabled(false);;
				viewHolder.stop_button.setVisibility(View.GONE);
				viewHolder.netSpeed.setTextColor(Color.RED);
				viewHolder.netSpeed.setText("下载失败");
				break;
			case 5:
				viewHolder.startButton.setEnabled(true);;
				viewHolder.startButton.setVisibility(View.VISIBLE);
				viewHolder.stop_button.setEnabled(false);;
				viewHolder.stop_button.setVisibility(View.GONE);
				viewHolder.netSpeed.setTextColor(Color.YELLOW);
				viewHolder.netSpeed.setText("取消成功");
				break;
		}
	viewHolder.netSizw.setText((FileUtil.getFormatSize(mFilelist.get(position).getDownlenth())+"/"+FileUtil.getFormatSize(mFilelist.get(position).getLength())));
	viewHolder.progressBar.setMax(mFilelist.get(position).getLength());
	viewHolder.progressBar.setProgress(mFilelist.get(position).getDownlenth());
	return convertView;
}

	static class ViewHolder{
		TextView textview;
		ImageView startButton;
		ImageView stop_button;
		ImageView deleteButton;
		ProgressBar progressBar;
		TextView netSpeed;
		TextView netSizw;
	}
}