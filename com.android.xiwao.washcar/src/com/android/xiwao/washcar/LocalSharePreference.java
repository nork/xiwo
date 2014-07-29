package com.android.xiwao.washcar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LocalSharePreference {
	private static final String USER_NAME = "user_name";
	private static final String USER_PASSWORD = "user_password";
	private static final String LOGIN_STATE = "login_state";
	private static final String USER_ID = "user_id";
	private static final String USER_NICK_NAME = "user_nick_name";
	private static final String USER_EMAIL = "user_email";
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
	public String getUserPassword(){
		return getStringPref(USER_PASSWORD, "");
	}
	
	/**
	 * 保存用户ID
	 * @param id
	 */
	public void setUserId(long id){
		putLongPref(USER_ID, id);
	}
	
	/**
	 * 获取用户ID
	 * @return
	 */
	public long getUserId(){
		return getLongPref(USER_ID, 0);
	}
	
	/**
	 * 保存用户昵称
	 * @param nickName
	 */
	public void setNickName(String nickName){
		putStringPref(USER_NICK_NAME, nickName);
	}
	
	/**
	 * 获取用户昵称
	 * @return
	 */
	public String getNickName(){
		return getStringPref(USER_NICK_NAME, "");
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
	
	/**
	 * 保存用户邮箱
	 * @param email
	 */
	public void setUserEmail(String email){
		mEditor.putString(USER_EMAIL, email);
		mEditor.commit();
	}
	
	/**
	 * 获取用户邮箱
	 * @return
	 */
	public String getUserEmail(){
		return mSp.getString(USER_EMAIL, "");
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
	
	private void putLongPref(String key, long value){
		mEditor.putLong(key, value);
		mEditor.commit();
	}
	
	private long getLongPref(String key, long defValue){
		return mSp.getLong(key, defValue);
	}
}
