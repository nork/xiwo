package com.android.xiwao.washcar.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SimpleAdapter;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.ui.widget.MyGallery;

public class GuideActivity extends Activity {

	public Context mContext;
	public View view;
    private static final String TAG = "GuideActivity";

	private int[] imgids = { R.id.img01, R.id.img02, R.id.img03, R.id.img04 };
	private View[] imgviews = new View[5];
	
	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		// SysApplication.getInstance().addActivity(this);

		LayoutInflater inflater = LayoutInflater.from(mContext);
		view = inflater.inflate(R.layout.guideview, null);
		mLocalSharePref = new LocalSharePreference(this);
		setContentView(view); // R.layout.managemoneyview

		initContentView();
	}

	/**
	 * 初始化各组件并添加相应事件
	 */
	private void initContentView() {

		int[] images = { R.drawable.app_guide00, R.drawable.app_guide01,
				R.drawable.app_guide02};
		List<Map<String, Object>> gallerylist = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < images.length; i++) {
			Map values = new HashMap<String, Object>();
			values.put("icon", images[i]);
			gallerylist.add(values);
		}

		SimpleAdapter madapter = new SimpleAdapter(mContext, gallerylist,
				R.layout.guidegalleritem,// 布局
				new String[] { "icon" }, new int[] { R.id.icon });

		MyGallery mgallery = (MyGallery) view.findViewById(R.id.guidegallert);
		mgallery.setAdapter(madapter);

		mgallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == 2) {
					mLocalSharePref.putBooleanPref(LocalSharePreference.IF_FIRST_USE, true);
					finish();
				}
			}
		});

		for (int i = 0; i < imgids.length; i++) {
			imgviews[i] = findViewById(imgids[i]);
		}
		mgallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				// Toast.makeText(mcontext, ""+arg2, 0).show();
				for (int i = 0; i < imgids.length; i++) {
					imgviews[i].setBackgroundResource(R.drawable.guide_note_02);
				}
				imgviews[arg2].setBackgroundResource(R.drawable.guide_note_01);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}
}
