package com.android.xiwao.washcar;

import android.app.Application;

public class XiwaoApplication extends Application{
	private LocalSharePreference mLocalSharePref;
	private int displayWidth;
	private int displayHeight;
	private boolean ifNeedRefreshOrder;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mLocalSharePref = new LocalSharePreference(getApplicationContext());
	}
	public LocalSharePreference getmLocalSharePref() {
		return mLocalSharePref;
	}
	public void setmLocalSharePref(LocalSharePreference mLocalSharePref) {
		this.mLocalSharePref = mLocalSharePref;
	}
	public int getDisplayWidth() {
		return displayWidth;
	}
	public void setDisplayWidth(int displayWidth) {
		this.displayWidth = displayWidth;
	}
	public int getDisplayHeight() {
		return displayHeight;
	}
	public void setDisplayHeight(int displayHeight) {
		this.displayHeight = displayHeight;
	}
	public boolean isIfNeedRefreshOrder() {
		return ifNeedRefreshOrder;
	}
	public void setIfNeedRefreshOrder(boolean ifNeedRefreshOrder) {
		this.ifNeedRefreshOrder = ifNeedRefreshOrder;
	}
}
