package com.will.classinfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.R.anim;
import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract.Contacts.Data;
import android.view.ViewStub;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;

import com.will.classinfo.manager.ShareManager;
import com.will.classinfo.model.Class;
import com.will.classinfo.service.DateChangeService;
import com.will.classinfo.util.Tools;
import com.will.classinfo.widget.WidgetProvider_4_1;

public class ClassApplication extends Application {
	/**
	 * 公共区
	 */
	private Calendar firstWeek=null;
	private Calendar now = null;
	private List<Class> nofinishedClasses = null;
	private List<Class> finishedClasses = null;
	private int mornClassNum = 0;
	private int afterClassNum = 0;
	private int evenClassNum = 0;
	private int totalWeekNum = 0;

	private int week = 0;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ShareManager shareManager = new ShareManager(this);
		if ((firstWeek = shareManager.readDate()) == null)
			firstWeek = Calendar.getInstance(Locale.CHINA);
		this.setCalendar(firstWeek);
		nofinishedClasses = shareManager.readClass(R.string.no_finished_class);
		if (nofinishedClasses == null)
			nofinishedClasses = new ArrayList<Class>();
		finishedClasses = shareManager.readClass(R.string.finished_class);
		if (finishedClasses == null)
			finishedClasses = new ArrayList<Class>();
		this.setMornClassNum(shareManager.readMornClassNum());
		this.setAfterClassNum(shareManager.readAfterClassNum());
		this.setEvenClassNum(shareManager.readEvenClassNum());
		this.setTotalWeekNum(shareManager.readTotalWeekNum());
		this.now = Calendar.getInstance(Locale.CHINA);
		updateClasses();
		//启动service监听时间改变
		if(!Tools.isServiceRunning(this,"com.will.classinfo.service.DateChangeService")){
			Intent intent=new Intent(this, DateChangeService.class);
			this.startService(intent);
		}
	}

	public Calendar getCalendar() {
		return firstWeek;
	}

	public void setCalendar(Calendar calendar) {
		this.firstWeek = calendar;
	}

	public int getMornClassNum() {
		return mornClassNum;
	}

	public void setMornClassNum(int mornClassNum) {
		this.mornClassNum = mornClassNum;
	}

	public int getAfterClassNum() {
		return afterClassNum;
	}

	public void setAfterClassNum(int afterClassNum) {
		this.afterClassNum = afterClassNum;
	}

	public int getEvenClassNum() {
		return evenClassNum;
	}

	public void setEvenClassNum(int evenClassNum) {
		this.evenClassNum = evenClassNum;
	}

	public void addClass(Class mClass) {
		if (mClass.getEndWeek() < week) {
			this.finishedClasses.add(mClass);
		} else {
			this.nofinishedClasses.add(mClass);
		}
	}

	public void modifyNofiniedClass(int position, Class mClass) {
		if (mClass.getEndWeek() < week) {
			this.finishedClasses.add(mClass);
			this.nofinishedClasses.remove(position);
		} else {
			this.nofinishedClasses.set(position, mClass);
		}
	}

	public void modifyFiniedClass(int position, Class mClass) {
		if (mClass.getEndWeek() >= week) {
			this.nofinishedClasses.add(mClass);
			this.finishedClasses.remove(position);
		} else {
			this.finishedClasses.set(position, mClass);
		}
	}

	public Class getNofiniedClass(int position) {
		return this.nofinishedClasses.get(position);
	}

	public Class getfiniedClass(int position) {
		return this.finishedClasses.get(position);
	}

	public List<Class> getNofinishedClasses() {
		return nofinishedClasses;
	}

	public void setNofinishedClasses(List<Class> nofinishedClasses) {
		this.nofinishedClasses = nofinishedClasses;
	}

	public List<Class> getFinishedClasses() {
		return finishedClasses;
	}

	public void setFinishedClasses(List<Class> finishedClasses) {
		this.finishedClasses = finishedClasses;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public int getDayClassNum() {
		return this.mornClassNum + this.afterClassNum + this.evenClassNum;
	}

	public int getTotalWeekNum() {
		return totalWeekNum;
	}

	public void removeNofinishedClassAt(int position) {
		this.nofinishedClasses.remove(position);
	}

	public void removeFinishedClassAt(int position) {
		this.finishedClasses.remove(position);
	}

	public void setTotalWeekNum(int totalWeekNum) {
		this.totalWeekNum = totalWeekNum;
	}

	public Calendar getNow() {
		return now;
	}

	public void setNow(Calendar now) {
		this.now = now;
	}

	public void updateClasses() {
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
	}

}
