package com.android.xiwao.washcar.utils;

import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.ui.OrderManageFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtils {
	public static Fragment curFragment;
	
	/**
	 * �л�fragment
	 * @param to
	 * @param transaction
	 */
	public static void switchContent(Fragment to, FragmentTransaction transaction) {
	
        if (curFragment != to) {
            if (!to.isAdded()) {    // ���ж��Ƿ�add��
                transaction.hide(curFragment).add(R.id.content, to).commit(); // ���ص�ǰ��fragment��add��һ����Activity��
            } else {
                transaction.hide(curFragment).show(to).commit(); // ���ص�ǰ��fragment����ʾ��һ��
            }
            curFragment = to;
        }
    }
	
	public static void refershContent(Fragment to,FragmentTransaction transaction){

//		curFragment = null;
		if (curFragment != to) {
//            if (!to.isAdded()) {    // ���ж��Ƿ�add��
                transaction.hide(curFragment).add(R.id.content, to).commit(); // ���ص�ǰ��fragment��add��һ����Activity��
//            } else {
//                transaction.hide(curFragment).show(to).commitAllowingStateLoss(); // ���ص�ǰ��fragment����ʾ��һ��
//            }
            curFragment = to;
        }
	}
	
	public static void removeFragment(Fragment fragment,FragmentTransaction transaction){
		transaction.remove(fragment);
	}
}
