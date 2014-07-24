package com.android.xiwao.washcar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LocalSharePreference {
	private static final String USER_NAME = "user_name";
	private static final String USER_PASSWORD = "user_password";
	private static final String LOGIN_STATE = "login_state";
	private Context mCtx;
	private SharedPreferences mSp;
	private SharedPreferences.Editor mEditor;

	public LocalSharePreference(Context ctx) {
		mCtx = ctx.getApplicationContext();
		mSp = PreferenceManager.getDefaultSharedPreferences(mCtx);
		mEditor = mSp.edit();
	}

	public void setUserName(String userName){
		putStringPref(USER_NAME, userName);
	}
	
	public String getUserName(){
		return getStringPref(USER_NAME, "");
	}
	
	public void setUserPassword(String password){
		putStringPref(USER_PASSWORD, password);
	}
	
	/**
	 * 设置用户登录状态
	 * @param state
	 */
	public void setLoginState(boolean state){
		putBooleanPref(LOGIN_STATE, state);
	}
	
	/**
	 * 获取用户登录状态
	 * @return
	 */
	public boolean getLoginState(){
		return getBooleanPref(LOGIN_STATE, false);
	}
	
	public String getUserPassword(){
		return getStringPref(USER_PASSWORD, "");
	}
	
	private void putStringPref(String key, String value) {
		mEditor.putString(key, value);
		mEditor.commit();
	}

	private String getStringPref(String key, String defValue) {
		return mSp.getString(key, defValue);
	}
	
	private void putBooleanPref(String key, boolean value){
		mEditor.putBoolean(key, value);
		mEditor.commit();
	}
	
	private boolean getBooleanPref(String key, boolean defValue){
		return mSp.getBoolean(key, defValue);
	}
}
