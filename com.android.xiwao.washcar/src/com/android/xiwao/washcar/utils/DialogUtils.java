package com.android.xiwao.washcar.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.R;

/**
 * 弹出框工具类
 * @author hpq
 *
 */
public class DialogUtils {
	private Context mContext;
	private ProgressDialog mProgressDlg;
	
	public DialogUtils(){
		mContext = ActivityManage.getInstance().getCurContext();
	}
	
	public void showProgress(){
		mProgressDlg = ProgressDialog.show(mContext, "", mContext.getString(R.string.wait_in_progress));
	}
	
	public void dismissProgress(){
		if(mProgressDlg != null){
			mProgressDlg.dismiss();
			mProgressDlg = null;
		}
	}
	
	public void showToast(String toastContent){
		Toast.makeText(mContext, toastContent, Toast.LENGTH_LONG).show();
	}
}
