package com.will.classinfo.manager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.will.classinfo.R;
import com.will.classinfo.config.Config;
import com.will.classinfo.model.Class;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ShareManager {

	private Context context = null;
	private SharedPreferences sharedPreferences = null;

	public ShareManager(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public void saveDate(Calendar calendar) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		if (calendar != null) {
			editor.putString(context.getString(R.string.first_class_time),
					calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DAY_OF_MONTH));
			editor.commit();
		}
	}

	public Calendar readDate() {
		String date = sharedPreferences.getString(context.getString(R.string.first_class_time), null);
		if (date == null)
			return null;
		String[] dates = date.split(" ");
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.set(Integer.valueOf(dates[0]), Integer.valueOf(dates[1]), Integer.valueOf(dates[2]));
		return calendar;
	}

	/**
	 * 课程格式： 课程名 教师 上课时间 上课地点
	 * 
	 * @param classes
	 */
	public void saveClass(List<Class> classes, int strSrc) {
		if (!classes.isEmpty()) {
			Gson gson = new Gson();
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(context.getString(strSrc), gson.toJson(classes));
			editor.commit();
		}
	}

	/**
	 * 课程格式： 课程名 教师 上课时间 上课地点
	 * 
	 * @param classes
	 */
	public List<Class> readClass(int strSrc) {
		Gson gson = new Gson();
		String str = sharedPreferences.getString(context.getString(strSrc), null);
		if (str == null)
			return null;
		Type listType = new TypeToken<LinkedList<Class>>() {
		}.getType();
		List<Class> classes = gson.fromJson(str, listType);
		return classes;
	}

	public int readMornClassNum() {
		return Integer.valueOf(sharedPreferences.getString(context.getString(R.string.morn_class_num), "1"));
	}

	public void writeClassNum(int mornNum, int afterNum, int evenNum) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(context.getString(R.string.morn_class_num), String.valueOf(mornNum));
		editor.putString(context.getString(R.string.after_class_num), String.valueOf(afterNum));
		editor.putString(context.getString(R.string.even_class_num), String.valueOf(evenNum));
		editor.commit();
	}

	public int readAfterClassNum() {
		return Integer.valueOf(sharedPreferences.getString(context.getString(R.string.after_class_num), "1"));
	}

	public int readEvenClassNum() {
		return Integer.valueOf(sharedPreferences.getString(context.getString(R.string.even_class_num), "1"));
	}

	public int readTotalWeekNum() {
		return Integer.valueOf(sharedPreferences.getString(context.getString(R.string.total_week_num), "10"));
	}

	public void writeTotalWeekNum(int num) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(context.getString(R.string.total_week_num), String.valueOf(num));
		editor.commit();
	}
}
