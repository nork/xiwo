package com.android.xiwao.washcar;
/**
 * 应用界面管理类
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

	// 单例模式中获取唯一的ExitApplication实例
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
	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish

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
