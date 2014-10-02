package com.will.classinfo.service;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.will.classinfo.ClassApplication;
import com.will.classinfo.ClassInfoFragment;
import com.will.classinfo.R;
import com.will.classinfo.model.Class;
import com.will.classinfo.util.Tools;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.os.IBinder;
import android.widget.RemoteViews;

public class DateChangeService extends Service {

	public static Context context;
	public static AppWidgetManager manager;
	public static RemoteViews views;
	private List<Class> nofinishedClasses = null;
	private List<Class> finishedClasses = null;
	private ClassApplication myApplication = null;
	private Calendar firstWeek = null;
	private Calendar now = null;
	private int week = 0;
	private boolean isRun=true;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("关闭服务");
		this.isRun=false;
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		now=Calendar.getInstance(Locale.CHINA);
		myApplication = (ClassApplication) getApplication();
		nofinishedClasses = myApplication.getNofinishedClasses();
		finishedClasses = myApplication.getFinishedClasses();
		firstWeek = myApplication.getCalendar();
		
		this.isRun=true;
		new TimeThread().start();
		return super.onStartCommand(intent, flags, startId);
	}

	private void update() {
		if (now.compareTo(firstWeek) >= 0) {
			if (now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				week = now.get(Calendar.WEEK_OF_YEAR) - firstWeek.get(Calendar.WEEK_OF_YEAR);
			} else {
				week = now.get(Calendar.WEEK_OF_YEAR) - firstWeek.get(Calendar.WEEK_OF_YEAR) + 1;
			}
			for (int i = 0; i < nofinishedClasses.size(); i++) {
				Class mClass = nofinishedClasses.get(i);
				if (mClass.getEndWeek() < week) {
					nofinishedClasses.remove(i);
					i--;
					finishedClasses.add(mClass);
				}
			}
			for (int i = 0; i < finishedClasses.size(); i++) {
				Class mClass = finishedClasses.get(i);
				if (mClass.getEndWeek() >= week) {
					finishedClasses.remove(i);
					i--;
					nofinishedClasses.add(mClass);
				}
			}
		} else {
			for (Class mClass : finishedClasses) {
				nofinishedClasses.add(mClass);
			}
			finishedClasses.clear();
		}
		myApplication.setNofinishedClasses(nofinishedClasses);
		myApplication.setFinishedClasses(finishedClasses);
		myApplication.setWeek(week);
		myApplication.setNow(now);
		Tools.sendUpdateBroadcast(this);
	}
	
	private class TimeThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Calendar temp=null;
			while(isRun){
				temp=Calendar.getInstance(Locale.CHINA);
				if(now.get(Calendar.YEAR)!=temp.get(Calendar.YEAR)||now.get(Calendar.MONTH)!=temp.get(Calendar.MONTH)
						||now.get(Calendar.DAY_OF_MONTH)!=temp.get(Calendar.DAY_OF_MONTH)){
					System.out.println("时间改变");
					now=temp;
					update();
				}
				try {
					Thread.sleep(1000*5);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}
	
}
