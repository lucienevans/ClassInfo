package com.will.classinfo;

import java.util.ArrayList;
import java.util.List;

import com.will.classinfo.model.Class;
import com.will.classinfo.model.ClassTime;
import com.will.classinfo.util.Tools;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class ModifyClassActivity extends Activity {

	private int totalWeekNum = 0;
	private int classNum = 1;
	private int position = 0;
	private boolean isFinished = false;

	private EditText classNameEditText = null;
	private EditText teacherNameEditText = null;
	private Spinner startWeekSpinner = null;
	private Spinner endWeekSpinner = null;
	private Spinner examWeek = null;
	private Button addTimeButton = null;
	private Button deleteButton = null;
	private LinearLayout layout = null;

	private Class mClass = null;
	private ClassApplication myApplication = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_modify_class);
		onInit();
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
	}

	private void onInit() {
		// TODO Auto-generated method stub
		this.getActionBar().setTitle("修改课程");

		myApplication = (ClassApplication) getApplication();
		Intent intent = getIntent();
		position = intent.getExtras().getInt(getString(R.string.class_position));
		isFinished = intent.getExtras().getBoolean(getString(R.string.isfinished));
		if (isFinished) {
			mClass = myApplication.getfiniedClass(position);
		}else{
			mClass=myApplication.getNofiniedClass(position);
		}
		totalWeekNum = myApplication.getTotalWeekNum();
		classNameEditText = (EditText) findViewById(R.id.class_name_edittext);
		teacherNameEditText = (EditText) findViewById(R.id.teacher_name_edittext);
		startWeekSpinner = (Spinner) findViewById(R.id.start_week_spinnner);
		endWeekSpinner = (Spinner) findViewById(R.id.end_week_spinner);
		examWeek = (Spinner) findViewById(R.id.exam_spinner);
		addTimeButton = (Button) findViewById(R.id.add_time_button);
		addTimeButton.setOnClickListener(addTimeListener);
		deleteButton = (Button) findViewById(R.id.delete_time_button);
		deleteButton.setOnClickListener(deleteTimeListener);
		List<String> listContent = new ArrayList<String>();
		for (int i = 0; i < totalWeekNum; i++) {
			listContent.add((i + 1) + "周");
		}
		startWeekSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listContent));
		endWeekSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listContent));
		examWeek.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listContent));
		onInitLayout();
	}

	private void onInitLayout() {
		// TODO Auto-generated method stub
		layout = (LinearLayout) findViewById(R.id.third_layout);
		classNameEditText.setText(mClass.getClassName());
		teacherNameEditText.setText(mClass.getTeacher());
		startWeekSpinner.setSelection(mClass.getStartWeek() - 1);
		endWeekSpinner.setSelection(mClass.getEndWeek() - 1);
		examWeek.setSelection(mClass.getExamWeek() - 1);
		for (int i = 0; i < mClass.getTimeNum(); i++) {
			ClassTime time = mClass.getTime(i);
			layout.addView(Tools.getView(this,time.getLocation(), time.getDay(), time.getTime(), time.getSingleOrDouble()
					,myApplication.getMornClassNum(),myApplication.getAfterClassNum(),myApplication.getEvenClassNum(),classNum));
			classNum++;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.add, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		mClass = new Class();
		mClass.setClassName(classNameEditText.getText().toString());
		mClass.setTeacher(teacherNameEditText.getText().toString());
		mClass.setStartWeek(startWeekSpinner.getSelectedItemPosition() + 1);
		mClass.setEndWeek(endWeekSpinner.getSelectedItemPosition() + 1);
		mClass.setExamWeek(examWeek.getSelectedItemPosition() + 1);
		setClassLocationAndTime();
		if(isFinished){
			myApplication.modifyFiniedClass(position, mClass);
		}else{
			myApplication.modifyNofiniedClass(position, mClass);
		}
		//通知桌面widget
		Tools.sendUpdateBroadcast(this);
		this.finish();
		return super.onOptionsItemSelected(item);
	}

	private void setClassLocationAndTime() {
		// TODO Auto-generated method stub
		for (int i = 0; i < classNum - 1; i++) {
			LinearLayout linearLayout = (LinearLayout) layout.getChildAt(i);
			EditText locationEditText = (EditText) linearLayout.getChildAt(2);
			LinearLayout childLayout = (LinearLayout) linearLayout.getChildAt(4);
			Spinner daySpinner = (Spinner) childLayout.getChildAt(0);
			Spinner timeSpinner = (Spinner) childLayout.getChildAt(1);
			Spinner singleOrDoubleSpinner = (Spinner) linearLayout.getChildAt(6);
			mClass.addClassTime(daySpinner.getSelectedItemPosition() + 1, timeSpinner.getSelectedItemPosition() + 1, locationEditText.getText().toString(),
					singleOrDoubleSpinner.getSelectedItemPosition());
		}
	}

	private OnClickListener addTimeListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			layout.setVisibility(layout.VISIBLE);
			layout.addView(Tools.getView(ModifyClassActivity.this,"", 1, 1, 0
					,myApplication.getMornClassNum(),myApplication.getAfterClassNum(),myApplication.getEvenClassNum(),classNum));
			classNum++;
		}
	};
	private OnClickListener deleteTimeListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (classNum > 1) {
				Builder builder = new Builder(ModifyClassActivity.this);
				builder.setTitle("提示");
				builder.setMessage("您确定删除该上课时间吗？");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						layout.removeViewAt(layout.getChildCount() - 1);
						classNum--;
						arg0.dismiss();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						arg0.dismiss();
					}
				});
				builder.create().show();
			}
		}
	};
}
