package com.android.xiwao.washcar;
/**
 * Ӧ�ý��������
 */
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;

public class ActivityManage{
	private List<Activity> activityList = new ArrayList<Activity>();
	private AppLog appLog = new AppLog();
	private static ActivityManage instance;
	private Context curContext;
	private int updateState = 0;
	
	public int getUpdateState() {
		return updateState;
	}

	public void setUpdateState(int updateState) {
		this.updateState = updateState;
	}

	private ActivityManage() {
	}

	// ����ģʽ�л�ȡΨһ��ExitApplicationʵ��
	public static ActivityManage getInstance() {
		if (null == instance) {
			instance = new ActivityManage();
		}
		return instance;

	}
	
	public void setCurContext(Context mContext){
		this.curContext = mContext;
	}
	
	public Context getCurContext(){
		return curContext;
	}
	
	public AppLog getAppLog(){
		if(appLog == null){
			appLog = new AppLog();
		}
		return appLog;
	}
	// ���Activity��������
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// ��������Activity��finish

	public void exit() {

		for (Activity activity : activityList) {
			activity.finish();
		}

		System.exit(0);
	}
	
	public void exitInError(){
		for (Activity activity : activityList) {
			activity.finish();
		}
	}
}
