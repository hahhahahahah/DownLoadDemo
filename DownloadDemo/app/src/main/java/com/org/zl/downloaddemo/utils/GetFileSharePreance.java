package com.org.zl.downloaddemo.utils;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.org.zl.downloaddemo.constant.SPKey;
import com.org.zl.downloaddemo.entity.FileInfo;

import java.util.ArrayList;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.org.zl.downloaddemo.utils.SPUtils.getString;


/**
 * 获取下载进度的缓存工具类
 *
 */
public class GetFileSharePreance {
	private Context context;
	public GetFileSharePreance(Context context) {
		this.context = context;
	}
	//设置未下载完成的数据的所有路径，并缓存起来
	public void setUrlList(ArrayList<FileInfo> list){
		JSONObject object = new JSONObject();
		JSONArray array = new JSONArray();
		for(int x = 0 ; x < list.size() ; x++){
			array.add(x,list.get(x).getUrl());
		}
		object.put("urls",array);
		SPUtils.setString(context, SPKey.URLS,object.toJSONString());
	}
	//获取未下载的数据的所有路径
	public ArrayList<String> getUrlList(){
		String jsons = SPUtils.getString(context,SPKey.URLS);
		if(TextUtils.isEmpty(jsons)){
			setUrlList(new ArrayList<FileInfo>());
		}else {
			JSONObject jsonObject = JSONObject.parseObject(jsons);
			JSONArray arrays = jsonObject.getJSONArray("urls");
			ArrayList<String> list = new ArrayList<>();
			for (int x = 0; x < arrays.size(); x++) {
				String str = arrays.getString(x);
				list.add(str);
			}
			return list;
		}
		return new ArrayList<String>();
	}
	/**
	 * 根据url获取线程的下载进度
	 *
	 * @param path
	 * @return
	 */
	public FileInfo getFilePre(String path) {
		String json = getString(context,path);
		if(TextUtils.isEmpty(json)){
			LogUtils.e("缓存为空");
		}else{
			JSONObject object = parseObject(json);
			int id = object.getInteger("id");
			String url = object.getString("url");
			String fileName = object.getString("fileName");
			int downloadSize = object.getInteger("downloadSize");
			int fileSize = object.getInteger("fileSize");
			return new FileInfo(id,url,fileName,fileSize,downloadSize,0);
		}
		return null;
	}


	/**
	 * 实时更新每条线程已经下载的文件长度
	 *
	 */
	public void update(FileInfo info) {
		JSONObject object = new JSONObject();
		object.put("id",info.getId());
		object.put("url",info.getUrl());
		object.put("downloadSize",info.getDownlenth());
		object.put("fileSize",info.getLength());
		object.put("fileName",info.getFileName());
		SPUtils.setString(context,info.getUrl() ,object.toJSONString());
	}

	/**
	 * 当文件下载完成后，删除对应的下载记录
	 *
	 * @param path
	 */
	public void delete(String path){
		SPUtils.setString(context,path,"");
		LogUtils.e("删除缓存数据 = "+path.toString());
	}
}