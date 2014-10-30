package com.android.xiwao.washcar.utils;

import java.text.DecimalFormat;

import com.android.xiwao.washcar.AppLog;

public class StringUtils {
	public static boolean isChinese(char a) {
		int v = (int) a;
		return (v >= 19968 && v <= 171941);
	}

	public static boolean containsChinese(String s) {
		if (null == s || "".equals(s.trim()))
			return false;
		for (int i = 0; i < s.length(); i++) {
			if (isChinese(s.charAt(i)))
				return true;
		}
		return false;
	}
	
	public static String getPriceStr(int price){
		DecimalFormat df = new DecimalFormat("###.00");
		double priceDouble = ((double)price) / 100;
		String priceStr = df.format(priceDouble);
		AppLog.v("TAG", "价格字串：" + priceStr);
		if(priceDouble < 1){
			priceStr = "0" + priceStr; 
		}
		return priceStr;
	}
	
	public static String getPriceIntStr(int price){
		DecimalFormat df = new DecimalFormat("###.00");
		double priceDouble = ((double)price) / 100;
		AppLog.v("TAG", "价格字串1：" + priceDouble);
		String priceStr = df.format(priceDouble);
		AppLog.v("TAG", "价格字串：" + priceStr);
		if(priceDouble < 1){
			priceStr = "0" + priceStr; 
		}
		return priceStr;
	}
}
