package com.android.xiwao.washcar.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.AppLog;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.FeeData;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.RateQuery;
import com.android.xiwao.washcar.httpconnection.VIPInfoQuery;
import com.android.xiwao.washcar.update.UpdateApp;
import com.android.xiwao.washcar.utils.FragmentUtils;

public class MainActivity extends FragmentActivity{
	private final static String TAG = "MainActivity";
    private FragmentManager fragmentManager;
    private RadioGroup radioGroup;
    
	private FragmentTransaction transaction; 
	private Fragment fragment;
	
	private boolean ifOrderReturn = false;
	
	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;
	private LocalSharePreference mLocalSharePref;
	
	// 工具
//	private DialogUtils dialogUtils;
	
	public static List<FeeData> feeDataList = new ArrayList<FeeData>();
	private long mExitTime;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getDisHw();//获取屏幕分辨率，供后期使用
        requestWindowFeature(Window.FEATURE_NO_TITLE);
      
        ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		UpdateApp updateApp = new UpdateApp(this);
		updateApp.checkVersion();
		getDisHw();
		mLocalSharePref = new LocalSharePreference(this);
		setContentView(R.layout.activity_main);
		FragmentFactory.initFragment();
		
        initContentView();
        initExecuter();
        rateQuery();
        if(mLocalSharePref.getUserType().equals("01") && mLocalSharePref.getLoginState()){
//        	Intent intent = new Intent(this, MonthlyActivity.class);
//			startActivity(intent);
        	getCarListData();
        }
    }
    
    /**
     * 初始化控件
     */
	private void initContentView(){
    	fragmentManager = this.getSupportFragmentManager();
        radioGroup = (RadioGroup) findViewById(R.id.rg_tab);
        /**
         * 初始化第一个Fragment
         */
        fragment = FragmentFactory.homePageFragment;
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content, fragment).show(fragment);
        transaction.commit();
        FragmentUtils.curFragment = fragment;
        
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction = fragmentManager.beginTransaction();
        		
        		if(!mLocalSharePref.getLoginState() && (checkedId == R.id.order_manager
        				|| checkedId == R.id.car_info)){
        			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        			startActivity(intent);
        			return;
        		}
                fragment = FragmentFactory.getInstanceByIndex(checkedId);

                if((((XiwaoApplication)getApplication()).isIfNeedRefreshOrder()) && checkedId == R.id.order_manager){
                	FragmentUtils.removeFragment(fragment, transaction);
                	FragmentFactory.orderManageFragment = new OrderManageFragment();
                	fragment = FragmentFactory.getInstanceByIndex(checkedId);;
                	FragmentUtils.refershContent(fragment, transaction);
                	((XiwaoApplication)getApplication()).setIfNeedRefreshOrder(false);
                }else if((((XiwaoApplication)getApplication()).isIfNeedRefreshHeadImg()) && checkedId == R.id.car_info){
                	FragmentUtils.switchContent(fragment, transaction);
                	fragment.onActivityResult(Constants.REFRESH_HEAD_IMG, RESULT_OK, null);
                	((XiwaoApplication)getApplication()).setIfNeedRefreshHeadImg(false);
                }else{
                	FragmentUtils.switchContent(fragment, transaction);
                }  
            }
        });
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ActivityManage.getInstance().setCurContext(this);
		try{
			ifOrderReturn = getIntent().getBooleanExtra(Constants.IF_ORDER_RETURN, false);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(ifOrderReturn){
			fragment = FragmentFactory.orderManageFragment;
			if(fragment.isAdded()){//如果已经加载订单fragment，则刷新页面并跳转到订单fragment
				 FragmentUtils.switchContent(fragment, transaction);
				 fragment.onActivityResult(Constants.ADD_ORDER_RESULT_CODE, RESULT_OK, null);
			}else{//如果没有添加订单fragment,则直接跳转过去即可
				FragmentUtils.switchContent(fragment, transaction);
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		AppLog.v(TAG, "收到反馈requestCode:" + requestCode);
		
		switch(requestCode){
		case Constants.ADD_CAR_RESULT_CODE:		//添加车辆返回
			if(resultCode == RESULT_OK){
				fragment.onActivityResult(requestCode, resultCode, data);
			}
			break;
		case Constants.MODIFY_CAR_RESULT_CODE:	//修改车辆返回
			if(resultCode == RESULT_OK){
				fragment.onActivityResult(requestCode, resultCode, data);
			}
			break;
		case Constants.CHECK_ORDER_RESULT_CODE:
			AppLog.v(TAG, "更新订单列表1");
			if(resultCode == RESULT_OK){
				fragment.onActivityResult(requestCode, resultCode, data);
			}
			break;
		}
	}
	/**
	 * 查询费用
	 */
	public void rateQuery(){
		BaseCommand login = ClientSession.getInstance().getCmdFactory()
				.getRateQuery();

		mExecuter.execute(login, mRespHandler);
	}

	/**
	 * 处理服务器返回的查询结果
	 * @param rsp 服务返回的登录信息
	 */
	private void onReceiveLoginResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
//			dialogUtils.showToast(error);
		} else {
			RateQuery.Response rateQueryRsp = (RateQuery.Response) rsp;
			if (rateQueryRsp.responseType.equals("N")) {
				feeDataList = rateQueryRsp.briefs;
			} else {
//				dialogUtils.showToast(loginRsp.errorMessage);
			}
		}
	}

	private CommandExecuter.ResponseHandler mRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveLoginResponse(rsp);
		}

		public void handleException(IOException e) {
//			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
//			dialogUtils.dismissProgress();
		}
	};
	
	/**
	 * 处理服务器返回的登录结果
	 * @param rsp 服务返回的登录信息
	 */
	private void onReceiveCarListResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
//			dialogUtils.showToast(error);
		} else {
			VIPInfoQuery.Response vipInfoQuery = (VIPInfoQuery.Response) rsp;
			if (vipInfoQuery.responseType.equals("N")) {
//				monthlyCarDataList = vipInfoQuery.monthlyCarDataList;
				if(vipInfoQuery.monthlyCarDataList.size() == 1){
					Intent intent = new Intent(this, MonthlyDetailActivity.class);
					intent.putExtra("service_type", 0);
					intent.putExtra("choice_monthly_car", (Parcelable)vipInfoQuery.monthlyCarDataList.get(0));
					startActivity(intent);
				}else{
					Intent intent = new Intent(this, MonthlyActivity.class);
					startActivity(intent);
				}
			} else {
//				dialogUtils.showToast(vipInfoQuery.errorMessage);
			}
//			fetchList();
		}
//		dialogUtils.dismissProgress();
	}
	
	private CommandExecuter.ResponseHandler mRespMonthlyCarHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveCarListResponse(rsp);
		}

		public void handleException(IOException e) {
//			dialogUtils.showToast(getString(R.string.connection_error));
//			fetchList();
		}

		public void onEnd() {
//			dialogUtils.dismissProgress();
		}
	};
	
	private void getCarListData(){
		BaseCommand login = ClientSession.getInstance().getCmdFactory()
				.getVIPInfoQuery(mLocalSharePref.getUserId());

		mExecuter.execute(login, mRespMonthlyCarHandler);

//		dialogUtils.showProgress();
	}
	
	private void initExecuter() {

		mHandler = new Handler();

		mExecuter = new CommandExecuter();
		mExecuter.setHandler(mHandler);
	}
	
	public void setHwView(){
		int displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
//		int displayWidth = ((XiwaoApplication)getApplication()).getDisplayWidth();
		LinearLayout.LayoutParams tabParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT
				, (int)(displayHeight * 0.08f + 0.5f));
		radioGroup.setLayoutParams(tabParams); 
		
		tabParams = new LinearLayout.LayoutParams(0, (int)(displayHeight * 0.08f + 0.5f), 1); 
		RadioButton homePage = (RadioButton) findViewById(R.id.home_page);
		RadioButton carInfo = (RadioButton) findViewById(R.id.car_info);
		RadioButton orderManage = (RadioButton) findViewById(R.id.order_manager);
		RadioButton more = (RadioButton) findViewById(R.id.more);
		homePage.setLayoutParams(tabParams);
		carInfo.setLayoutParams(tabParams);
		orderManage.setLayoutParams(tabParams);
		more.setLayoutParams(tabParams);		
	}
	
	/**
	 * 获取屏幕的宽高
	 */
	public void getDisHw(){
		DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ((XiwaoApplication)getApplication()).setDisplayWidth(displayMetrics.widthPixels);
        ((XiwaoApplication)getApplication()).setDisplayHeight(displayMetrics.heightPixels);
	}
}
