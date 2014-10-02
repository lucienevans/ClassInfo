package com.will.classinfo.component;

import java.util.Calendar;
import java.util.Locale;

import com.will.classinfo.R;
import com.will.classinfo.manager.ShareManager;

import android.content.Context;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

public class DatePickerPreference extends DialogPreference {

	private DatePicker picker = null;
	private Calendar calendar = null;

	public DatePickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.date_picker_preference);
		calendar = Calendar.getInstance(Locale.CHINA);
		this.setSummary(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONDAY)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH));
		// TODO Auto-generated constructor stub
	}

	public void setDefaultDate(Calendar date) {
		this.calendar = date;
		this.setSummary(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONDAY)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH));
	}

	@Override
	protected void onBindDialogView(View view) {
		// TODO Auto-generated method stub
		super.onBindDialogView(view);
		picker = (DatePicker) view.findViewById(R.id.date);
		picker.setCalendarViewShown(false);
		picker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH));
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		// TODO Auto-generated method stub
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			calendar.set(picker.getYear(), picker.getMonth(),picker.getDayOfMonth());
			this.setSummary(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONDAY)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH));
			ShareManager shareManager=new ShareManager(getContext());
			shareManager.saveDate(calendar);
		}
	}
}
