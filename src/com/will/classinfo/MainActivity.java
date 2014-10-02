package com.will.classinfo;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.will.classinfo.R;
import com.will.classinfo.manager.ShareManager;
import com.will.classinfo.model.Class;
import com.will.classinfo.util.SmartBarUtils;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;

public class MainActivity extends Activity {
	
	private ShareManager shareManager = null;
	private ClassApplication myApplication = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onInit();

		final ActionBar bar = getActionBar();
		bar.addTab(bar.newTab().setIcon(R.drawable.ic_tab_main).setTabListener(new MyTabListener<ClassListFragment>(this, "课程列表", ClassListFragment.class)));
		bar.addTab(bar.newTab().setIcon(R.drawable.ic_tab_dialer).setTabListener(new MyTabListener<ClassInfoFragment>(this, "课程一览", ClassInfoFragment.class)));

		// 如果是用户自定义的View，可以像下面这样操作
		// bar.addTab(bar.newTab().setCustomView(R.layout.tab_widget_indicator)
		// .setTabListener(new MyTabListener<ContactsFragment>(this, "contacts",
		// ContactsFragment.class)));

		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// 设置ActionBar Tab显示在底栏
		SmartBarUtils.setActionBarTabsShowAtBottom(bar, true);
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
	}

	private void onInit() {
		// TODO Auto-generated method stub
		myApplication = (ClassApplication) getApplication();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (item.getItemId()) {
		case R.id.action_settings:
			intent.setClass(MainActivity.this, SettingActivity.class);
			break;
		case R.id.item_add:
			intent.setClass(MainActivity.this, AddClassActivity.class);
			break;
		default:
			break;
		}
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ShareManager shareManager=new ShareManager(this);
		shareManager.saveClass(myApplication.getNofinishedClasses(),R.string.no_finished_class);
		shareManager.saveClass(myApplication.getFinishedClasses(), R.string.finished_class);
		super.onDestroy();
	}
	
	
	public static class MyTabListener<T extends Fragment> implements ActionBar.TabListener {
		private final Activity mActivity;
		private final String mTag;
		private final java.lang.Class<T> mClass;
		private final Bundle mArgs;
		private Fragment mFragment;

		public MyTabListener(Activity activity, String tag, java.lang.Class<T> clz) {
			this(activity, tag, clz, null);
		}

		public MyTabListener(Activity activity, String tag, java.lang.Class<T> clz, Bundle args) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
			mArgs = args;

			// Check to see if we already have a fragment for this tab, probably
			// from a previously saved state. If so, deactivate it, because our
			// initial state is that a tab isn't shown.
			mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
			if (mFragment != null && !mFragment.isDetached()) {
				FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
				ft.detach(mFragment);
				ft.commit();
			}
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (mFragment == null) {
				mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				ft.attach(mFragment);
			}

			mActivity.getActionBar().setTitle(mTag);
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				ft.detach(mFragment);
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
	}

}
