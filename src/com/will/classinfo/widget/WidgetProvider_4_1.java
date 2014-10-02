package com.will.classinfo.widget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.will.classinfo.config.Config;
import com.will.classinfo.model.Class;
import com.will.classinfo.model.ClassTime;
import com.will.classinfo.service.DateChangeService;
import com.will.classinfo.util.ClassComparator;
import com.will.classinfo.util.Tools;
import com.will.classinfo.ClassApplication;
import com.will.classinfo.ModifyClassActivity;
import com.will.classinfo.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

public class WidgetProvider_4_1 extends AppWidgetProvider {
	private List<Class> noFinishedClasses = null;
	private List<Class> finishedClasses = null;
	private ClassApplication myApplication = null;
	private Calendar firstWeek = null;
	private Calendar now = null;
	private int week = 0;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		final int N = appWidgetIds.length;
		RemoteViews views = getUpdateView(context);
		for (int i = 0; i < N; i++) {
			appWidgetManager.updateAppWidget(appWidgetIds[i], views);
		}
		//启动service监听时间改变
		if(!Tools.isServiceRunning(context,"com.will.classinfo.service.DateChangeService")){
			Intent intent=new Intent(context, DateChangeService.class);
			context.startService(intent);
		}else{
			System.out.println("服务已启动");
		}
	}

	private RemoteViews getUpdateView(Context context) {
		myApplication = (ClassApplication) context.getApplicationContext();
		noFinishedClasses = myApplication.getNofinishedClasses();
		this.firstWeek = myApplication.getCalendar();
		week = myApplication.getWeek();
		now = myApplication.getNow();
		noFinishedClasses = myApplication.getNofinishedClasses();

		RemoteViews views = getWidgetViews(myApplication.getMornClassNum(), myApplication.getAfterClassNum(), myApplication.getEvenClassNum(), context, now,
				Tools.getTodayClasses(noFinishedClasses, week, now));
		return views;
	}
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		//停止service
		Intent intent=new Intent(context,DateChangeService.class);
		context.stopService(intent);
		super.onEnabled(context);
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(context.getString(R.string.classinfo_widget_datachange_action))) {
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			ComponentName name = new ComponentName(context, WidgetProvider_4_1.class);
			manager.updateAppWidget(name, getUpdateView(context));
		}
		super.onReceive(context, intent);
	}
	

	private RemoteViews getWidgetViews(int mornClassNum, int afterClassNum, int evenClassNum, Context context, Calendar time, List<Class> classes) {
		int classNum = 0;
		RemoteViews widgetViews = new RemoteViews(context.getPackageName(), R.layout.widget_provider4_1);
		int[] timeInfoIds = { R.id.time_info1, R.id.time_info2, R.id.time_info3, R.id.time_info4 };
		int[] classInfoIds = { R.id.class_info1, R.id.class_info2, R.id.class_info3, R.id.class_info4 };
		int[] classLayoutIds = { R.id.classinfo_layout1, R.id.classinfo_layout2, R.id.classinfo_layout3, R.id.classinfo_layout4 };
		widgetViews.setTextViewText(R.id.time, (time.get(Calendar.MONTH) + 1) + "月" + time.get(Calendar.DAY_OF_MONTH) + " " + Tools.getDayOfWeek(time.get(Calendar.DAY_OF_WEEK)));
		for (int i = 0; (i < classes.size() && i < timeInfoIds.length); i++) {
			classNum++;
			widgetViews.setViewVisibility(classLayoutIds[i], View.VISIBLE);
			widgetViews.setTextViewText(timeInfoIds[i], classes.get(i).getTime(0).getTimeStr(mornClassNum, afterClassNum, evenClassNum));
			widgetViews.setTextViewText(classInfoIds[i], classes.get(i).getClassName() + " " + classes.get(i).getTime(0).getLocation());
			
			Intent intent=new Intent(context, ModifyClassActivity.class);
			intent.putExtra(context.getString(R.string.isfinished), false);
			intent.putExtra(context.getString(R.string.class_position), classes.get(i).position);
			
			PendingIntent pendingIntent=PendingIntent.getActivity(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			widgetViews.setOnClickPendingIntent(classInfoIds[i], pendingIntent);
		}
		for (int i = classNum; i < timeInfoIds.length; i++) {
			widgetViews.setViewVisibility(classLayoutIds[i], View.INVISIBLE);
		}
		return widgetViews;
	}

}
