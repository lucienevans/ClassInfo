package com.will.classinfo.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.will.classinfo.ClassApplication;
import com.will.classinfo.R;
import com.will.classinfo.config.Config;
import com.will.classinfo.model.Class;
import com.will.classinfo.model.ClassTime;
import com.will.classinfo.widget.WidgetProvider_4_1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Tools {
	/**
	 * 构造一个listActivity所需的SimpleAdapter
	 * 
	 * @param context
	 *            listActivity
	 * @param content
	 *            list的各项txt
	 * @param layout
	 *            item的layout布局文件
	 * @param txtId
	 *            txt显示的布局文件
	 * @return
	 */
	public static SimpleAdapter buildSimpleAdapterOfString(Context context, List<String> content, List<String> describe, int layout, int componentId, int describeId) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < content.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("Item", content.get(i));
			if (describe.get(i) != null) {
				map.put("Describe", describe.get(i));
			} else {
				map.put("Describe", "");
			}
			list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(context, list, layout, new String[] { "Item", "Describe" }, new int[] { componentId, describeId });
		// *************************************************
		return adapter;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public static SimpleExpandableListAdapter buildExpandableListAdapter(Context context, List<Class> finishedClasses, List<Class> noFinishedClasses, int mornClassNum,
			int afterClassNum, int evenClassNum) {
		List<HashMap<String, String>> grouplist = new ArrayList<HashMap<String, String>>();
		List<List<HashMap<String, String>>> children = new ArrayList<List<HashMap<String, String>>>();
		HashMap<String, String> map = null;
		List<HashMap<String, String>> child = null;
		if (!noFinishedClasses.isEmpty()) {
			map = new HashMap<String, String>();
			map.put("group", "未完成课程");
			grouplist.add(map);
			child = new ArrayList<HashMap<String, String>>();
			for (Class mClass : noFinishedClasses) {
				HashMap<String, String> childMap = new HashMap<String, String>();
				childMap.put("Item", mClass.getClassName() + " " + mClass.getTeacher());
				StringBuffer sb = new StringBuffer("");
				for (int i = 0; i < mClass.getTimeNum(); i++) {
					ClassTime time = mClass.getTime(i);
					sb.append(time.getLocation() + " "+context.getResources().getStringArray(R.array.day_week)[time.getDay()-1] + "-" + time.getTimeStr(mornClassNum, afterClassNum, evenClassNum) + "\n");
				}
				sb.deleteCharAt(sb.length()-1);
				childMap.put("Describe", sb.toString());
				child.add(childMap);
			}
			children.add(child);
		}
		if (!finishedClasses.isEmpty()) {
			map = new HashMap<String, String>();
			map.put("group", "已完成课程");
			grouplist.add(map);
			child = new ArrayList<HashMap<String, String>>();
			for (Class mClass : finishedClasses) {
				HashMap<String, String> childMap = new HashMap<String, String>();
				childMap.put("Item", mClass.getClassName() + " " + mClass.getTeacher());
				StringBuffer sb = new StringBuffer("");
				for (int i = 0; i < mClass.getTimeNum(); i++) {
					ClassTime time = mClass.getTime(i);
					sb.append(time.getLocation() + " "+context.getResources().getStringArray(R.array.day_week)[time.getDay()-1] + "-" + time.getTimeStr(mornClassNum, afterClassNum, evenClassNum) + "\n");
				}
				sb.deleteCharAt(sb.length()-1);
				childMap.put("Describe", sb.toString());
				child.add(childMap);
			}
			children.add(child);
		}
		return new SimpleExpandableListAdapter(context, grouplist, R.layout.group_item, new String[] { "group" }, new int[] { R.id.group_textview }, children,
				R.layout.twoline_list_item, new String[] { "Item", "Describe" }, new int[] { R.id.list_info, R.id.list_describe });
	}

	@SuppressLint("ResourceAsColor")
	public static View getView(Context context, String location, int day, int timeOfDay, int singleOrDouble, int mornClassNum, int afterClassNum, int evenClassNum, int classNum) {
		LinearLayout linearLayout = new LinearLayout(context);
		LinearLayout childLayout = new LinearLayout(context);
		TextView classTimeTextView = new TextView(context);
		Spinner classTimeSpinner = new Spinner(context);
		Spinner classDaySpinner = new Spinner(context);
		TextView singleOrDoubleTextView = new TextView(context);
		Spinner singleOrDoubleSpinner = new Spinner(context);
		TextView locationTextView = new TextView(context);
		EditText locationEditText = new EditText(context);
		TextView lineTextView = new TextView(context);

		linearLayout.setPadding(35, 20, 35, 20);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		linearLayout.setLayoutParams(layoutParams);
		childLayout.setOrientation(LinearLayout.HORIZONTAL);
		childLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		classTimeTextView.setText("上课时间：");
		singleOrDoubleTextView.setText("单周双周：");
		locationTextView.setText("上课地点：");
		lineTextView.setText("第" + classNum + "节");
		classTimeTextView.setTextSize(16);
		singleOrDoubleTextView.setTextSize(16);
		locationTextView.setTextSize(16);
		lineTextView.setTextSize(16);
		locationEditText.setTextSize(16);
		classTimeTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		classTimeSpinner.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		singleOrDoubleTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		locationEditText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		lineTextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		classDaySpinner.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		classDaySpinner.setBackgroundColor(android.R.color.white);

		List<String> adapterContent = new ArrayList<String>();
		for (int i = 0; i < mornClassNum; i++) {
			adapterContent.add("早上第" + (i + 1) + "节");
		}
		for (int i = 0; i < afterClassNum; i++) {
			adapterContent.add("下午第" + (i + 1) + "节");
		}
		for (int i = 0; i < evenClassNum; i++) {
			adapterContent.add("晚上第" + (i + 1) + "节");
		}
		classTimeSpinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, adapterContent));
		classDaySpinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,
				new String[] { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日" }));
		singleOrDoubleSpinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, new String[] { "无单双周", "单周", "双周" }));
		locationTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		locationEditText.setInputType(InputType.TYPE_CLASS_TEXT);

		locationEditText.setText(location);
		classDaySpinner.setSelection(day - 1);
		classTimeSpinner.setSelection(timeOfDay - 1);
		singleOrDoubleSpinner.setSelection(singleOrDouble);

		childLayout.addView(classDaySpinner);
		childLayout.addView(classTimeSpinner);

		linearLayout.addView(lineTextView);
		linearLayout.addView(locationTextView);
		linearLayout.addView(locationEditText);
		linearLayout.addView(classTimeTextView);
		linearLayout.addView(childLayout);
		linearLayout.addView(singleOrDoubleTextView);
		linearLayout.addView(singleOrDoubleSpinner);

		return linearLayout;
	}

	public static int convertDay(int day) {
		if (day == 1)
			day = 8;
		day--;
		return day;
	}

	public static String getDayOfWeek(int day) {
		switch (day) {
		case 1:
			return new String("星期日");
		case 2:
			return new String("星期一");
		case 3:
			return new String("星期二");
		case 4:
			return new String("星期三");
		case 5:
			return new String("星期四");
		case 6:
			return new String("星期五");
		case 7:
			return new String("星期六");
		default:
			return new String("ERROR!");
		}
	}

	public static void sendUpdateBroadcast(Context context) {
		Intent intent = new Intent();
		intent.setAction(context.getString(R.string.classinfo_widget_datachange_action));
		context.sendBroadcast(intent);
	}

	/**
	 * 
	 * @param noFinishedClasses
	 * @param week
	 * @param now
	 * @return
	 */
	public static List<Class> getTodayClasses(List<Class> noFinishedClasses, int week, Calendar now) {
		List<Class> todayClasses = new ArrayList<Class>();
		for (int k = 0; k < noFinishedClasses.size(); k++) {
			Class mClass = noFinishedClasses.get(k);
			if (mClass.getStartWeek() <= week) {
				for (int i = 0; i < mClass.getTimeNum(); i++) {
					ClassTime time = mClass.getTime(i);
					// 如果不是单双周
					if (time.getSingleOrDouble() == Config.NONE) {
						if (time.getDay() == Tools.convertDay(now.get(Calendar.DAY_OF_WEEK))) {
							Class TimeClass = new Class(mClass);
							TimeClass.removeAllTimes();
							TimeClass.addClassTime(time.getDay(), time.getTime(), time.getLocation(), time.getSingleOrDouble());
							TimeClass.position=k;
							todayClasses.add(TimeClass);
						}
					} else if (time.getSingleOrDouble() == Config.SINGLE && week % 2 == 1) { // 单周
						if (time.getDay() == Tools.convertDay(now.get(Calendar.DAY_OF_WEEK))) {
							Class TimeClass = new Class(mClass);
							TimeClass.removeAllTimes();
							TimeClass.addClassTime(time.getDay(), time.getTime(), time.getLocation(), time.getSingleOrDouble());
							TimeClass.position=k;
							todayClasses.add(TimeClass);
						}
					} else if (time.getSingleOrDouble() == Config.DATEPICKER && week % 2 == 0) { // 双周

						if (time.getDay() == Tools.convertDay(now.get(Calendar.DAY_OF_WEEK))) {
							Class TimeClass = new Class(mClass);
							TimeClass.removeAllTimes();
							TimeClass.addClassTime(time.getDay(), time.getTime(), time.getLocation(), time.getSingleOrDouble());
							TimeClass.position=k;
							todayClasses.add(TimeClass);
						}
					}
				}
			}
		}
		ClassComparator classComparator = new ClassComparator();
		Collections.sort(todayClasses, classComparator);
		return todayClasses;
	}

	public static boolean isServiceRunning(Context context, String className) {

		boolean isRunning = false;

		ActivityManager activityManager =

		(ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);

		List<ActivityManager.RunningServiceInfo> serviceList

		= activityManager.getRunningServices(Integer.MAX_VALUE);

		if (!(serviceList.size() > 0)) {

			return false;

		}

		for (int i = 0; i < serviceList.size(); i++) {

			if (serviceList.get(i).service.getClassName().equals(className) == true) {

				isRunning = true;

				break;

			}

		}


		return isRunning;

	}
}
