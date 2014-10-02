package com.will.classinfo;

import java.util.ArrayList;
import java.util.List;

import com.will.classinfo.model.Class;
import com.will.classinfo.model.ClassTime;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClassInfoFragment extends Fragment {

	private static final int TEXTHEIGHT = 200;
	private List<Class> nofinishedClasses = null;
	private int mornClassNum = 0;
	private int afterClassNum = 0;
	private int evenClassNum = 0;
	private LinearLayout[] layouts = null;
	private int totalClassNum = 0;
	private Object[][] classesInOrder = null;
	private ClassApplication myApplication=null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View fragmentView = inflater.inflate(R.layout.fragment_class_info, container, false);
		myApplication = (ClassApplication) this.getActivity().getApplication();
		nofinishedClasses = myApplication.getNofinishedClasses();
		mornClassNum = myApplication.getMornClassNum();
		afterClassNum = myApplication.getAfterClassNum();
		evenClassNum = myApplication.getEvenClassNum();
		layouts = new LinearLayout[] { (LinearLayout) fragmentView.findViewById(R.id.classnum_layout), (LinearLayout) fragmentView.findViewById(R.id.monday_layout),
				(LinearLayout) fragmentView.findViewById(R.id.thursday_layout), (LinearLayout) fragmentView.findViewById(R.id.wednesday_layout),
				(LinearLayout) fragmentView.findViewById(R.id.thirseday_layout), (LinearLayout) fragmentView.findViewById(R.id.friday_layout),
				(LinearLayout) fragmentView.findViewById(R.id.saturday_layout), (LinearLayout) fragmentView.findViewById(R.id.sunday_layout) };
		totalClassNum = mornClassNum + afterClassNum + evenClassNum;
		onInitTable();
		return fragmentView;
	}

	private void onInitTable() {
		// TODO Auto-generated method stub
		// 排列好课程
		OrderClasses();
		TextView textView = null;
		for (int j = 0; j < mornClassNum; j++) {
			textView = new TextView(this.getActivity());
			textView.setText("上午第" + (j + 1) + "节");
			textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, TEXTHEIGHT));
			textView.setBackgroundResource(R.drawable.table_text);
			textView.setGravity(Gravity.CENTER);
			layouts[0].addView(textView);
		}
		for (int j = 0; j < afterClassNum; j++) {
			textView = new TextView(this.getActivity());
			textView.setText("下午第" + (j + 1) + "节");
			textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, TEXTHEIGHT));
			textView.setBackgroundResource(R.drawable.table_text);
			textView.setGravity(Gravity.CENTER);
			layouts[0].addView(textView);
		}
		for (int j = 0; j < evenClassNum; j++) {
			textView = new TextView(this.getActivity());
			textView.setText("晚上第" + (j + 1) + "节");
			textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, TEXTHEIGHT));
			textView.setBackgroundResource(R.drawable.table_text);
			textView.setGravity(Gravity.CENTER);
			layouts[0].addView(textView);
		}

		for (int i = 1; i < 8; i++) {
			for (int j = 0; j < totalClassNum; j++) {
				textView=new TextView(this.getActivity());
				textView.setBackgroundResource(R.drawable.table_text);
				textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, TEXTHEIGHT));
				textView.setGravity(Gravity.CENTER);
				if(classesInOrder[j][i-1]!=null){
					List<Class> classes=(List<Class>) classesInOrder[j][i-1];
					StringBuffer sb=new StringBuffer();
					for(Class mClass:classes){
						sb.append(mClass.getClassName()+" "+mClass.getTeacher()+" "+mClass.getTime(0).getLocation()+
					isSingleOrDouble(mClass.getTime(0).getSingleOrDouble())+"\n");
					}
					sb.deleteCharAt(sb.length()-1);
					textView.setText(sb.toString());
				}
				layouts[i].addView(textView);
			}
		}
	}

	private void OrderClasses() {
		classesInOrder = new Object[totalClassNum][7];
		for (int k = 0; k < nofinishedClasses.size(); k++) {
			List<Class> classes = null;
			Class mClass = nofinishedClasses.get(k);
			for (int i = 0; i < mClass.getTimeNum(); i++) {
				Class classTemp = new Class(mClass);
				ClassTime timeOfClass = mClass.getTime(i);
				classTemp.removeAllTimes();
				classTemp.addClassTime(timeOfClass);
				if ((classes = (List<Class>) classesInOrder[timeOfClass.getTime() - 1][timeOfClass.getDay() - 1]) != null) {
					classes.add(classTemp);
				} else {
					classes = new ArrayList<Class>();
					classes.add(classTemp);
					classesInOrder[timeOfClass.getTime() - 1][timeOfClass.getDay() - 1] = classes;
				}
			}
		}
	}
	
	private String isSingleOrDouble(int singleOrDouble){
		if(singleOrDouble==1)
			return new String("(单周)");
		else if(singleOrDouble==2)
			return new String("(双周)");
		return new String("");
	}
	
}
