package com.android.xiwao.washcar.ui;

import android.annotation.SuppressLint;
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

import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.R;

@SuppressLint({ "ParserError", "NewApi", "NewApi", "NewApi" })
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
        getDisHw();//��ȡ��Ļ�ֱ��ʣ�������ʹ��
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initContentView();
//        setHwView();
    }
    /**
     * ��ʼ���ؼ�
     */
    @SuppressLint("ParserError")
	private void initContentView(){
    	fragmentManager = this.getSupportFragmentManager();
        radioGroup = (RadioGroup) findViewById(R.id.rg_tab);
        /**
         * ��ʼ����һ��Fragment
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
		LinearLayout.LayoutParams tabParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT
				, (int)(Constants.displayHeight * 0.08f + 0.5f));
		radioGroup.setLayoutParams(tabParams);
		
		tabParams = new LinearLayout.LayoutParams(0, (int)(Constants.displayHeight * 0.08f + 0.5f), 1);
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
	 * ��ȡ��Ļ�Ŀ��
	 */
	public void getDisHw(){
		DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Constants.displayWidth = displayMetrics.widthPixels;
        Constants.displayHeight = displayMetrics.heightPixels;
	}
}
