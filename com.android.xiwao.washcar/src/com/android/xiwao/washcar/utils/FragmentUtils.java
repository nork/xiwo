package com.android.xiwao.washcar.utils;

import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.ui.OrderManageFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtils {
	public static Fragment curFragment;
	
	/**
	 * 切换fragment
	 * @param to
	 * @param transaction
	 */
	public static void switchContent(Fragment to, FragmentTransaction transaction) {
	
        if (curFragment != to) {
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(curFragment).add(R.id.content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(curFragment).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            curFragment = to;
        }
    }
	
	public static void refershContent(Fragment to,FragmentTransaction transaction){

//		curFragment = null;
		if (curFragment != to) {
//            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(curFragment).add(R.id.content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
//            } else {
//                transaction.hide(curFragment).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
//            }
            curFragment = to;
        }
	}
	
	public static void removeFragment(Fragment fragment,FragmentTransaction transaction){
		transaction.remove(fragment);
	}
}
