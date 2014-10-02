package com.will.classinfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.will.classinfo.model.Class;
import com.will.classinfo.model.ClassTime;
import com.will.classinfo.util.Tools;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ListView;

/**
 * @author will
 * 
 */
public class ClassListFragment extends Fragment {

	private ClassApplication myApplication = null;
	private int mornClassNum = 1;
	private int afterClassNum = 1;
	private int evenClassNum = 1;
	private ExpandableListView listView = null;
	private ExpandableListAdapter adapter = null;

	// private ListView noFinishedListView = null;
	// private ListView FinishedListView = null;
	// 存储各class在classes中的位置
	private List<Class> noFinishedClasses = null;
	private List<Class> finishedClasses = null;
	private int selectClassPosition = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View fragmentView = inflater.inflate(R.layout.fragment_class_list, container, false);
		listView = (ExpandableListView) fragmentView.findViewById(R.id.class_list);
		onInit();
		return fragmentView;
	}

	private void onInit() {
		// TODO Auto-generated method stub
		myApplication = (ClassApplication) getActivity().getApplication();

		noFinishedClasses = myApplication.getNofinishedClasses();
		finishedClasses = myApplication.getFinishedClasses();

		mornClassNum = myApplication.getMornClassNum();
		afterClassNum = myApplication.getAfterClassNum();
		evenClassNum = myApplication.getEvenClassNum();
		listView.setOnGroupClickListener(onGroupClickListener);
		listView.setOnChildClickListener(onChildClickListener);
		listView.setOnItemLongClickListener(onItemLongClickListener);
		onInitList();
	}

	private void onInitList() {
		adapter = Tools.buildExpandableListAdapter(this.getActivity(), finishedClasses, noFinishedClasses, mornClassNum, afterClassNum, evenClassNum);
		listView.setAdapter(adapter);
		listView.setGroupIndicator(null);
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			listView.expandGroup(i);
		}
	}

	private OnChildClickListener onChildClickListener = new OnChildClickListener() {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(ClassListFragment.this.getActivity(), ModifyClassActivity.class);
			intent.putExtra(getString(R.string.class_position), childPosition);
			if (groupPosition == 1) {
				intent.putExtra(getString(R.string.isfinished), true);
			} else {
				if (!noFinishedClasses.isEmpty()) {
					intent.putExtra(getString(R.string.isfinished), false);
				} else {
					intent.putExtra(getString(R.string.isfinished), true);
				}
			}
			ClassListFragment.this.startActivity(intent);
			return true;
		}
	};
	private OnGroupClickListener onGroupClickListener = new OnGroupClickListener() {
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
			// TODO Auto-generated method stub
			return true;
		}
	};

	private OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int location, long arg3) {
			// TODO Auto-generated method stub
			Builder builder = new Builder(ClassListFragment.this.getActivity());
			if (location != 0 && location != noFinishedClasses.size() + 1) {
				if (location <= noFinishedClasses.size()) {
					selectClassPosition = location - 1;
					Class selectClass = noFinishedClasses.get(selectClassPosition);
					builder.setMessage("您确定删除《" + selectClass.getClassName() + "》吗？");
					builder.setTitle("提示");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							// TODO Auto-generated method stub
							myApplication.removeNofinishedClassAt(selectClassPosition);
							dialog.dismiss();
							onInitList();
							Tools.sendUpdateBroadcast(ClassListFragment.this.getActivity());
						}
					});
				} else {
					selectClassPosition = location - noFinishedClasses.size() - 2;
					Class selectClass = finishedClasses.get(selectClassPosition);
					builder.setMessage("您确定删除《" + selectClass.getClassName() + "》吗？");
					builder.setTitle("提示");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							// TODO Auto-generated method stub
							myApplication.removeFinishedClassAt(selectClassPosition);
							dialog.dismiss();
							onInitList();
						}
					});
				}
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						arg0.dismiss();
					}
				});
				builder.create().show();
			}
			return true;
		}
	};

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		this.noFinishedClasses = myApplication.getNofinishedClasses();
		this.finishedClasses = myApplication.getFinishedClasses();
		onInitList();
		super.onResume();
	}
}
