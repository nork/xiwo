/**
 * 应用升级类
 */
package com.android.xiwao.washcar.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.xiwao.washcar.R;

public class UpdateApp {
	private String TAG = "UPDATEAPP";
	private VersionInfo oldVerInfo = new VersionInfo();
	private VersionInfo newVerInfo = new VersionInfo();
	
	public ProgressDialog pBar;
	private Handler handler = new Handler();
	private Context context;
	private boolean ifCheckVersion = false;
	private String updateUrl = null;
	
	public UpdateApp(Context context)
	{
		this.context = context;
	}
	
	public VersionInfo getNewVersion(){
		return this.newVerInfo;
	}
	
	public VersionInfo getOldVersion(){
		return this.oldVerInfo;
	}
	public boolean getUpdateFlag()
	{
		return this.ifCheckVersion;
	}
	
	public Handler updateHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 1:
				
				String newVerName = newVerInfo.getVerName().replace(".", ""); 
				String oldVername = oldVerInfo.getVerName().replace(".", "");
				
				int difflength = newVerName.length() - oldVername.length();
				if(difflength>0){
					for(int i=0; i<difflength; i++){
						oldVername = oldVername+"0";
					}
				}else{
					for(int i=0; i<Math.abs(difflength); i++){  
						newVerName = newVerName+"0";
					}
				}
				
				int newversion;
				int oldversion;
				Log.i(TAG, "oldVername:" + oldVername+ " newVerName:" + newVerName);
				if(newVerName.equals("") || newVerName == null || newVerName.length() == 0){
					newversion = 0;
				}else{
					newversion = Integer.parseInt(newVerName);
				}
				oldversion = Integer.parseInt(oldVername);
						
				if(newversion > oldversion){
					Log.i(TAG, "--------------doUpdateVersion----------");
					doUpdateVersion();
				}				
//				if(!newVerInfo.getVerName().equals(oldVerInfo.getVerName())){
//	    			doUpdateVersion();
//	    		}
				else{
	    			ifCheckVersion = true;
	    		}
				break;
			case 2://更新下载进度
				Bundle data = new Bundle();
				data = msg.getData();
				int progress = data.getInt("progress");
//				Log.i(TAG, "progress:" + progress);
				pBar.setProgress(progress);
				pBar.setSecondaryProgress(progress);
				break;
			default:
				break;
			}
		}
	};
	/**
	 * 获取当前应用的版本信息
	 */
	public void checkVersion() {
		// TODO Auto-generated method stub   	
    	Log.v(TAG, "begin to checkVersion");
    	oldVerInfo.setVerCode(getOldVerCode(this.context));
    	oldVerInfo.setVerName(getOldVerName(this.context));
        
//    	Log.v(TAG, "old verInfo:" + oldVerInfo.getVerCode());
//    	Log.v(TAG, "new verInfo:" + newVerInfo.getVerCode());
    	getServerVer();
	}
	
	/**
	 * 获取网络上的应用版本信息
	 * @return boolean
	 */
	public void getServerVer() {   
			new Thread() {
				public void run() {
					try {  
						Properties props = new Properties(); 
						InputStream in = context.getResources().openRawResource(R.raw.prometheus);  //new FileInputStream(getSettingFile());  
						props.load(in);  
						updateUrl= props.getProperty("Update_changeUrl");
						
						newVerInfo = NetworkTool.getContent(context, updateUrl);  
//						Log.v(TAG, "version url:" + verjson);											
//						JSONArray array = new JSONArray(verjson);   
//						if (array.length() > 0) {   
//							JSONObject obj = array.getJSONObject(0);   
//							try {   
//								newVerInfo.setVerCode(Integer.parseInt(obj.getString("verCode")));
//								Log.v(TAG, "new Version:" + Integer.parseInt(obj.getString("verCode")));
//								newVerInfo.setVerName(obj.getString("verName"));
//								newVerInfo.setApkName(obj.getString("apkname"));
//							} catch (Exception e) {   
//								newVerInfo.setVerCode(-1);   
//								newVerInfo.setVerName("");   
//								updateHandler.sendEmptyMessage(1);
//							}   
//						}   
					} catch(Exception e) {  
						e.printStackTrace();
						newVerInfo.setVerCode(-1);   
						newVerInfo.setVerName(""); 
						updateHandler.sendEmptyMessage(1);
					}
					updateHandler.sendEmptyMessage(1);
				}
			}.start();
    }
	
	/**
	 * 获取旧的版本号
	 * @param context
	 * @return verCode
	 */
	public int getOldVerCode(Context context) {
		// TODO Auto-generated method stub
		int verCode = -1;
		try{
			verCode = context.getPackageManager().getPackageInfo(VersionInfo.PACKAGE_NAME, 0).versionCode;
			context.getPackageName();
		}catch (NameNotFoundException e){
		}
		return verCode;
	}
	
	/**
	 * 获取旧的版本名称
	 * @param context
	 * @return verName
	 */
	public String getOldVerName(Context context){
		String verName = "";
		try{
			verName = context.getPackageManager().getPackageInfo(VersionInfo.PACKAGE_NAME, 0).versionName;
		}catch (NameNotFoundException e){
			
		}
		return verName;
	}
	
	/**
	 * 提示版本更新
	 */
	public void doUpdateVersion() {
		// TODO Auto-generated method stub
		
		
		Properties props = new Properties(); 
		InputStream in = context.getResources().openRawResource(R.raw.prometheus);  //new FileInputStream(getSettingFile());  
		try {
			props.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		updateUrl= props.getProperty("Update_apkUrl");
		
		//int verCode = oldVerInfo.getVerCode();
		//this.ifCheckVersion = true;
		String verName = oldVerInfo.getVerName();
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本");
		sb.append(verName);
		sb.append(",最新版本");
		sb.append(newVerInfo.getVerName());
		sb.append("\n \n" + newVerInfo.getUpdateFunction());
		sb.append("\n是否升级？");
		Dialog dialog = null;
		if(newVerInfo.force){
			dialog = new AlertDialog.Builder(this.context)
			.setIcon(R.drawable.ic_launcher)
			.setTitle("升级提示")
			.setMessage("由于服务器产生变动，需要升级才能正常使用\n" + sb.toString())
			.setPositiveButton("立刻升级", 
				new DialogInterface.OnClickListener() {
							
					public void onClick(DialogInterface dialog, int whichButton) {
							// TODO Auto-generated method stub
							pBar = new ProgressDialog(context);
							pBar.setTitle("安装包下载中");
							pBar.setMessage("下载进度");
							pBar.setMax(100);
							pBar.setCancelable(false);
//							pBar.setProgressDrawable(context.getResources().getDrawable(R.color.ballbule));
//							pBar.set
							pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
							
							
							downApk(updateUrl);
							dialog.dismiss();
							//downApk("http://10.0.16.163:8081/cd/index.php?m=file&f=download&fileID=33&sid=e8aa19l1unij6fi692gafrcni1");
						}
					})
				.create();
		}else{
			dialog = new AlertDialog.Builder(this.context)
			.setIcon(R.drawable.ic_launcher)
			.setTitle("升级提示")
			.setMessage(sb.toString())
			.setPositiveButton("现在升级", 
					new DialogInterface.OnClickListener() {
								
						public void onClick(DialogInterface dialog, int whichButton) {
								// TODO Auto-generated method stub
								pBar = new ProgressDialog(context);
								pBar.setTitle("安装包下载中");
								pBar.setMessage("下载进度");
								pBar.setMax(100);
								pBar.setCancelable(false);
//								pBar.setProgressDrawable(context.getResources().getDrawable(R.color.ballbule));
//								pBar.set
								pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								
								downApk(updateUrl);
								dialog.dismiss();
								//downApk("http://10.0.16.163:8081/cd/index.php?m=file&f=download&fileID=33&sid=e8aa19l1unij6fi692gafrcni1");
							}
						})
					.setNegativeButton("暂不升级", 
						new DialogInterface.OnClickListener() {
								
							public void onClick(DialogInterface dialog, int whichButton) {
								// TODO Auto-generated method stub
								ifCheckVersion = true;
								dialog.dismiss();
							}
						})
					.create();
		}
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			 	public void onCancel(DialogInterface dialog) {
//			 		Constants.ifCancelProgress++;
			 		((Activity)context).finish();
			 	}
		});
		dialog.show();
	}
	
	/**
	 * 下载安装包
	 * @param url
	 */
	public void downApk(final String url) {
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(context, "未检测到SD卡，无法下载升级包！", Toast.LENGTH_LONG).show();
			return;
		}
		final String apkFilePath = Environment.getExternalStorageDirectory() + "/Prometheus/";
		File apkFile = new File(apkFilePath);
		if(!apkFile.exists()){
			apkFile.mkdir();
		}
		
		final String newFileName = apkFilePath + url.substring(url.lastIndexOf("/") + 1);
		final File newFile = new File(newFileName);
		if(newFile.exists()){
			newFile.delete();
		}
		
		pBar.show();
		Log.v(TAG, "down url:" + url);
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
//				CustomerHttpClient.enableSSL(client);
//				HttpPost get = new HttpPost(url);
				HttpGet get = new HttpGet(url);
				Log.v(TAG, "url:"+url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long contentLength = entity.getContentLength();
					InputStream is = entity.getContent();
//					URL url1 = new URL(url);
//					URLConnection con = url1.openConnection();
//					double contentLength = con.getContentLength();
//					Log.v("长度 :",contentLength + "");
//					InputStream is = con.getInputStream();

					FileOutputStream fileOutputStream = new FileOutputStream(newFile);
					
					if (is != null) {
						byte[] buf = new byte[1024];
						int ch = -1;
						double count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							Message msg = new Message();
							Bundle data = new Bundle();
//							Log.i(TAG, "count:" + count + "contentLen:" + contentLength + "count/len" + (double)(count/contentLength));
							data.putInt("progress", (int) ((count/contentLength) * 100));
							msg.setData(data);
							msg.what = 2;
							updateHandler.sendMessage(msg);

						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down(newFile);
				} catch (ClientProtocolException e) {
					ifCheckVersion = true;
					e.printStackTrace();
				} catch (IOException e) {
					ifCheckVersion = true;
					e.printStackTrace();
				}
			}

		}.start();

	}
	/**
	 * 下载完成操作
	 */
	public void down(final File file) {
		handler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update(file);
			}
		});
	}
	/**
	 * 更新安装包
	 */
	public void update(File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
		//System.exit(0);
		((Activity) context).finish();
	}
}
