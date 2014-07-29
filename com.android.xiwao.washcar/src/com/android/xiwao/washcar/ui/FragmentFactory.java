package com.android.xiwao.washcar.ui;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;

import com.android.xiwao.washcar.R;

/**
 * Created by admin on 13-11-23.
 */
public class FragmentFactory {
	public static Fragment homePageFragment;
	public static Fragment carInfoFragment;
	public static Fragment orderManageFragment;
	public static Fragment moreFragment;
	
	/**
	 * 初始化4个fragment模块
	 */
	public static void initFragment(){
		homePageFragment = new HomePageFragment();
		carInfoFragment = new CarInfoFragment();
		orderManageFragment = new OrderManageFragment();
		moreFragment = new MoreFragment();
	}
    @SuppressLint({ "NewApi", "NewApi", "NewApi" })
	public static Fragment getInstanceByIndex(int index) {
        Fragment fragment = null;
        switch (index) {
            case R.id.home_page:
            	return homePageFragment;
            case R.id.car_info:
                return carInfoFragment;
            case R.id.order_manager:
                return orderManageFragment;
            case R.id.more:
                return moreFragment;
        }
        return fragment;
    }
}
