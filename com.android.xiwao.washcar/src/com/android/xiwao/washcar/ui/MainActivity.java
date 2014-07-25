package com.android.xiwao.washcar.ui;

import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private final static String TAG = "MainActivity";
    private FragmentManager fragmentManager;
    private RadioGroup radioGroup;
    
	private FragmentTransaction transaction; 
	private Fragment fragment;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getDisHw();//获取屏幕分辨率，供后期使用
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		
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
        fragment = new HomePageFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
        
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            	Log.v(TAG, "checkedId:" + checkedId);
                transaction = fragmentManager.beginTransaction();
                fragment = FragmentFactory.getInstanceByIndex(checkedId);
                transaction.replace(R.id.content, fragment);
                transaction.commit();
            }
        });
    }
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	

	public void setHwView(){
		int displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
		int displayWidth = ((XiwaoApplication)getApplication()).getDisplayWidth();
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
