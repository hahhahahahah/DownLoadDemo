package com.org.zl.downloaddemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.org.zl.downloaddemo.constant.SPKey;


/**
 * 缓存工具类
 * 
 * @author Aaron
 * 
 */
public class SPUtils {
	private static SharedPreferences mSp;
	public static final String SP_NAME = SPKey.SHARED_USSER;

	public static SharedPreferences getSp(Context context) {
		if (mSp == null) {
			mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		return mSp;
	}
	public static Editor getSpEd(Context context) {
		if (mSp == null) {
			mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		return mSp.edit();
	}
//
//	public SPUtils putLong(String key, long value) {
//		mSp.edit().putLong(key, value).apply();
//		return this;
//	}
//
//	public static long getLong(String key, Long dValue) {
//		return mSp.getLong(key, dValue);
//	}
	/**
	 * 获取boolean类型的值
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            对应的键
	 * @param defValue
	 *            如果没有对应的值，
	 * @return
	 */
	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		SharedPreferences sp = getSp(context);
		return sp.getBoolean(key, defValue);
	}

	/**
	 * 获取boolean类型的值,如果没有对应的值，默认值返回false
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            对应的键
	 * @return
	 */
	public static boolean getBoolean(Context context, String key) {
		return getBoolean(context, key, false);
	}

	/**
	 * 设置boolean类型的值
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setBoolean(Context context, String key, boolean value) {
		SharedPreferences sp = getSp(context);
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 获取String类型的值
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            对应的键
	 * @param defValue
	 *            如果没有对应的值，
	 * @return
	 */
	public static String getString(Context context, String key, String defValue) {
		SharedPreferences sp = getSp(context);
		return sp.getString(key, defValue);
	}

	/**
	 * 获取String类型的值,如果没有对应的值，默认值返回null
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            对应的键
	 * @return
	 */
	public static String getString(Context context, String key) {
		return getString(context, key, "");
	}

	/**
	 * 设置String类型的值
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setString(Context context, String key, String value) {
		SharedPreferences sp = getSp(context);
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 获取long类型的值
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            对应的键
	 * @param defValue
	 *            如果没有对应的值，
	 * @return
	 */
	public static long getLong(Context context, String key, long defValue) {
		SharedPreferences sp = getSp(context);
		return sp.getLong(key, defValue);
	}

	/**
	 * 获取long类型的值,如果没有对应的值，默认值返回0
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            对应的键
	 * @return
	 */
	public static Long getLong(Context context, String key) {
		return getLong(context, key, -1L);
	}

	/**
	 * 设置Long类型的值
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setLong(Context context, String key, long value) {
		SharedPreferences sp = getSp(context);
		Editor editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();
	}


	
	/**
	 * 获取long类型的值,如果没有对应的值，默认值返回0
	 * @param <T>
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            对应的键
	 * @return
	 */
//	public static <T> T getObj(Context context,String key, Class<T> objClass) {
//		String strObj=getString(context, key);
//		Gson gson = new Gson();
//		if(strObj!=null)
//		{
//			return gson.fromJson(strObj, objClass);
//		}
//		return null;
//	}

	/**
	 * 设置Long类型的值
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
//	public static void setObj(Context context, String key, Object value) {
//
//		Gson gson = new Gson();
//		SharedPreferences sp = getSp(context);
//		Editor editor = sp.edit();
//		editor.putString(key, gson.toJson(value));
//		editor.commit();
//	}
//

}
