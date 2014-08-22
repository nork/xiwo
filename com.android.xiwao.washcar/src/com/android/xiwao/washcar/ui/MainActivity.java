package com.android.xiwao.washcar.ui;

import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.AppLog;
import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.utils.FragmentUtils;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private final static String TAG = "MainActivity";
    private FragmentManager fragmentManager;
    private RadioGroup radioGroup;
    
	private FragmentTransaction transaction; 
	private Fragment fragment;
	
	private boolean ifOrderReturn = false;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getDisHw();//获取屏幕分辨率，供后期使用
        requestWindowFeature(Window.FEATURE_NO_TITLE);
      
        ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		
		FragmentFactory.initFragment();
		
        setContentView(R.layout.activity_main);
        initContentView();
//      setHwView();
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
                fragment = FragmentFactory.getInstanceByIndex(checkedId);
                
//                if(fragment.isAdded()){
//                	transaction.hide(fragmentManage).show(fragment).commit();
//                }
//                transaction.replace(R.id.content, fragment).addToBackStack(null);
//                transaction.commit();
                if((((XiwaoApplication)getApplication()).isIfNeedRefreshOrder()) && checkedId == R.id.order_manager){
                	FragmentUtils.removeFragment(fragment, transaction);
                	FragmentFactory.orderManageFragment = new OrderManageFragment();
                	fragment = FragmentFactory.getInstanceByIndex(checkedId);;
                	FragmentUtils.refershContent(fragment, transaction);
                	((XiwaoApplication)getApplication()).setIfNeedRefreshOrder(false);
                }else{
                	FragmentUtils.switchContent(fragment, transaction);
                }
             
            }
        });
    }
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
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
