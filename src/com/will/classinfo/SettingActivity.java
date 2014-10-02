package com.will.classinfo;

import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.will.classinfo.component.DatePickerPreference;
import com.will.classinfo.config.Config;
import com.will.classinfo.util.Tools;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingActivity extends PreferenceActivity {


	private ClassApplication myApplication = null;
	private Calendar calendar = null;

	private DatePickerPreference firstTimePreference = null;
	private ListPreference totalWeekNumListPreference = null;
	private ListPreference mornListPreference = null;
	private ListPreference afterListPreference = null;
	private ListPreference evenListPreference = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.set_preference);
		onInit();
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
	}

	private void onInit() {
		// TODO Auto-generated method stub
		this.getActionBar().setTitle("…Ë÷√");
		
		myApplication = (ClassApplication) getApplication();
		calendar = myApplication.getCalendar();
		firstTimePreference = (DatePickerPreference) findPreference(this.getString(R.string.first_class_time));
		firstTimePreference.setDefaultDate(calendar);
		mornListPreference = (ListPreference) findPreference(this.getString(R.string.morn_class_num));
		afterListPreference = (ListPreference) findPreference(this.getString(R.string.after_class_num));
		evenListPreference = (ListPreference) findPreference(this.getString(R.string.even_class_num));
		totalWeekNumListPreference = (ListPreference) findPreference(this.getString(R.string.total_week_num));
		totalWeekNumListPreference.setSummary(getResources().getStringArray(R.array.preference_week_num_list_str)[myApplication.getTotalWeekNum()-10]);
		mornListPreference.setSummary(getResources().getStringArray(R.array.preference_class_time_list_str)[myApplication.getMornClassNum() - 1]);
		afterListPreference.setSummary(getResources().getStringArray(R.array.preference_class_time_list_str)[myApplication.getAfterClassNum() - 1]);
		evenListPreference.setSummary(getResources().getStringArray(R.array.preference_class_time_list_str)[myApplication.getEvenClassNum() - 1]);
		firstTimePreference.setDefaultDate(calendar);
		
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(onPreferenceChangeListener);
	}

	// ∏ƒ±‰summerary
	private OnSharedPreferenceChangeListener onPreferenceChangeListener = new OnSharedPreferenceChangeListener() {

		@Override
		public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
			// TODO Auto-generated method stub
			if(arg1.equals(getString(R.string.morn_class_num))){
				mornListPreference.setSummary(mornListPreference.getEntry());
			}else if(arg1.equals(getString(R.string.after_class_num))){
				afterListPreference.setSummary(afterListPreference.getEntry());
			}else if(arg1.equals(getString(R.string.even_class_num))){
				evenListPreference.setSummary(evenListPreference.getEntry());
			}else if(arg1.equals(getString(R.string.total_week_num))){
				totalWeekNumListPreference.setSummary(totalWeekNumListPreference.getEntry());
			}
		}
	};

	protected void onDestroy() {
		Tools.sendUpdateBroadcast(this);
		myApplication.setCalendar(calendar);
		myApplication.setMornClassNum(Integer.valueOf(mornListPreference.getValue()));
		myApplication.setAfterClassNum(Integer.valueOf(afterListPreference.getValue()));
		myApplication.setEvenClassNum(Integer.valueOf(evenListPreference.getValue()));
		myApplication.setTotalWeekNum(Integer.valueOf(totalWeekNumListPreference.getValue()));
		myApplication.updateClasses();
		super.onDestroy();
	};
}
