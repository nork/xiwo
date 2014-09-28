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
//	public static final String USER_LAST_CAR = "user_last_car";
//	public static final String USER_LAST_ADDR = "user_last_addr";
	public static final String USER_LAST_CAR_ID = "user_last_car_id";
	public static final String USER_LAST_CAR_NUM = "user_last_car_num";
	public static final String USER_LAST_DISTRACT_ID = "user_last_distract_id";
	public static final String USER_LAST_ADDRESS_ID = "user_last_address_id";
	public static final String USER_LAST_ADDRESS_DETAIL = "user_last_address_detail";
	public static final String USER_LAST_BRANCH_NAME = "user_last_branch_name";
	public static final String USER_LAST_CAR_TYPE = "user_last_car_type";
	public static final String USER_HEAD_BASE_64 = "user_head_base_64";
	public static final String USER_TYPE = "user_type";
	private Context mCtx;
	private SharedPreferences mSp;
	private SharedPreferences.Editor mEditor;

	public LocalSharePreference(Context ctx) {
		mCtx = ctx.getApplicationContext();
		mSp = PreferenceManager.getDefaultSharedPreferences(mCtx);
		mEditor = mSp.edit();
	}

	public void setUserName(String userName) {
		putStringPref(USER_NAME, userName);
	}

	public String getUserName() {
		return getStringPref(USER_NAME, "");
	}

	public void setUserPassword(String password) {
		putStringPref(USER_PASSWORD, password);
	}

	public String getUserPassword() {
		return getStringPref(USER_PASSWORD, "");
	}

	/**
	 * �����û�ID
	 * 
	 * @param id
	 */
	public void setUserId(long id) {
		putLongPref(USER_ID, id);
	}

	/**
	 * ��ȡ�û�ID
	 * 
	 * @return
	 */
	public long getUserId() {
		return getLongPref(USER_ID, 0);
	}

	/**
	 * �����û��ǳ�
	 * 
	 * @param nickName
	 */
	public void setNickName(String nickName) {
		putStringPref(USER_NICK_NAME, nickName);
	}

	/**
	 * ��ȡ�û��ǳ�
	 * 
	 * @return
	 */
	public String getNickName() {
		return getStringPref(USER_NICK_NAME, "");
	}

	/**
	 * �����û���¼״̬
	 * 
	 * @param state
	 */
	public void setLoginState(boolean state) {
		putBooleanPref(LOGIN_STATE, state);
	}

	/**
	 * ��ȡ�û���¼״̬
	 * 
	 * @return
	 */
	public boolean getLoginState() {
		return getBooleanPref(LOGIN_STATE, false);
	}

	/**
	 * �����û�����
	 * 
	 * @param email
	 */
	public void setUserEmail(String email) {
		mEditor.putString(USER_EMAIL, email);
		mEditor.commit();
	}

	/**
	 * ��ȡ�û�����
	 * 
	 * @return
	 */
	public String getUserEmail() {
		return mSp.getString(USER_EMAIL, "");
	}
	
	/**
	 * �����û�ͷ���base64��
	 * @param head
	 */
	public void setUserHead(String head){
		mEditor.putString(USER_HEAD_BASE_64, head);
		mEditor.commit();
	}
	/**
	 * ��ȡ�û�ͷ���base64
	 * @return
	 */
	public String getUserHead(){
		return mSp.getString(USER_HEAD_BASE_64, "");
	}

	/**
	 * �����û�����
	 * @param userType
	 */
	public void setUserType(String userType){
		mEditor.putString(USER_TYPE, userType);
		mEditor.commit();
	}
	/**
	 * ��ȡ�û�����
	 * @return
	 */
	public String getUserType(){
		return mSp.getString(USER_TYPE, "");
	}
	
	public void putStringPref(String key, String value) {
		mEditor.putString(key, value);
		mEditor.commit();
	}

	public String getStringPref(String key, String defValue) {
		return mSp.getString(key, defValue);
	}

	public void putBooleanPref(String key, boolean value) {
		mEditor.putBoolean(key, value);
		mEditor.commit();
	}

	public boolean getBooleanPref(String key, boolean defValue) {
		return mSp.getBoolean(key, defValue);
	}

	public void putLongPref(String key, long value) {
		mEditor.putLong(key, value);
		mEditor.commit();
	}

	public long getLongPref(String key, long defValue) {
		return mSp.getLong(key, defValue);
	}
}
