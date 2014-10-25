package com.android.xiwao.washcar.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;

public class PageUtil {
	/**是否循环*/
	public static final boolean isCycle = false;
	
	/**
	 * 获取ViewPage适配器数据
	 * @param context
	 * @return
	 */
	public static List<View> getPageList(Context context){
		List<View> pageList = new ArrayList<View>();
		pageList.add(getPageView(context,R.drawable.app_guide00));
		pageList.add(getPageView(context,R.drawable.app_guide01));
		pageList.add(getPageView(context,R.drawable.app_guide02));
		return pageList;
	}
	
	/**
	 * 构造ViewPage页面
	 * @param context
	 * @param imgResId
	 * @return
	 */
	private static View getPageView(Context context,int imgResId) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View pageView = inflater.inflate(R.layout.page_item, null);
		ImageView imgPage = (ImageView) pageView.findViewById(R.id.imgPage);
		imgPage.setBackgroundResource(imgResId);
		final int pos = imgResId;
		final Context mContext = context;
		imgPage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(pos == R.drawable.app_guide02){
					LocalSharePreference mLocalSharePref = new LocalSharePreference(mContext);
					mLocalSharePref.putBooleanPref(LocalSharePreference.IF_FIRST_USE, true);
					((Activity)mContext).finish();
				}
			}
		});
		return pageView;
	}
}
