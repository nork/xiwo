package com.android.xiwao.washcar.ui;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;

import com.android.xiwao.washcar.R;

/**
 * Created by admin on 13-11-23.
 */
public class FragmentFactory {
    @SuppressLint({ "NewApi", "NewApi", "NewApi" })
	public static Fragment getInstanceByIndex(int index) {
        Fragment fragment = null;
        switch (index) {
            case R.id.home_page:
            	fragment = new HomePageFragment();
                break;
            case R.id.car_info:
                fragment = new CarInfoFragment();
                break;
            case R.id.order_manager:
                fragment = new OrderManageFragment();
                break;
            case R.id.more:
                fragment = new MoreFragment();
                break;
        }
        return fragment;
    }
}
