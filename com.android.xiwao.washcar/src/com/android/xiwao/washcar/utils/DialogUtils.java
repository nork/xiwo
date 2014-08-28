package com.android.xiwao.washcar.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
//		mProgressDlg = new ProgressDialog(mContext, "", mContext.getString(R.string.wait_in_progress));
		mProgressDlg = new ProgressDialog(mContext);
		mProgressDlg.setCancelable(true);
		mProgressDlg.setCanceledOnTouchOutside(false);
		mProgressDlg.setMessage(mContext.getString(R.string.wait_in_progress));
		mProgressDlg.show();
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
	
	public void showExitDialog(){
		new AlertDialog.Builder(mContext)
		.setTitle(mContext.getString(R.string.remind))
		.setMessage(mContext.getString(R.string.sure_exit))
		.setPositiveButton(mContext.getString(R.string.sure),
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				ActivityManage.getInstance().exit();
			}
		})
		.setNegativeButton(mContext.getString(R.string.no),
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).show();
	}
}
