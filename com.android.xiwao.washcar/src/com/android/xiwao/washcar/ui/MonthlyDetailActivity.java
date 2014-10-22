package com.android.xiwao.washcar.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.AppLog;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.CarInfo;
import com.android.xiwao.washcar.data.MonthlyCarData;
import com.android.xiwao.washcar.utils.FileUtil;


public class MonthlyDetailActivity extends Activity{
	private Context mContext;
	private MonthlyCarData monthlyCarData;
	private Bitmap userHeadBitMap;
	private ImageView carPic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		setContentView(R.layout.monthly_detail);
		try{
			monthlyCarData = getIntent().getParcelableExtra("choice_monthly_car");
		}catch(Exception e){
			e.printStackTrace();
			monthlyCarData = null;
		}
		initContentView();
		setHwView();
	}
	
	private void initContentView(){
		TextView buffsTime = (TextView) findViewById(R.id.buffs_time);
		TextView washTime = (TextView) findViewById(R.id.wash_time);
		TextView cleanInTime = (TextView) findViewById(R.id.clean_in_time);
		TextView monthlyTime = (TextView) findViewById(R.id.monthly_time);
		TextView carCode = (TextView) findViewById(R.id.car_code);
		TextView carColor = (TextView) findViewById(R.id.car_color);
		TextView carBrand = (TextView) findViewById(R.id.car_brand);
		carPic = (ImageView) findViewById(R.id.car_img);
		Button monthlyBtn = (Button) findViewById(R.id.monthly_btn);
		TextView startTime = (TextView) findViewById(R.id.start_time);
		TextView endTime = (TextView) findViewById(R.id.end_time);
		TextView endTimeBottom = (TextView) findViewById(R.id.end_time_bottom);
		
		buffsTime.setText(monthlyCarData.getBuffTimes() + "次");
		washTime.setText(monthlyCarData.getWashTimes() + "次");
		cleanInTime.setText(monthlyCarData.getInteriorTimes() + "次");
		monthlyTime.setText(monthlyCarData.getStartDate().substring(0, 10) + "—" + monthlyCarData.getEndDate().substring(0, 10));
		startTime.setText(monthlyCarData.getStartDate().substring(0, 10));
		endTime.setText(monthlyCarData.getEndDate().substring(0, 10));
		endTimeBottom.setText("包月服务截止：" + monthlyCarData.getFinalDate().substring(0, 10));
		carCode.setText(monthlyCarData.getCarCode());
		carColor.setText(monthlyCarData.getCarColor());
		carBrand.setText(monthlyCarData.getCarBrand());
		
		String userHeadBase64 = monthlyCarData.getCarPic();
		if(!userHeadBase64.equals("") && userHeadBase64 != null && !userHeadBase64.equals("null")){
			AppLog.v("TAG11", userHeadBase64);
			userHeadBitMap = FileUtil.base64ToBitmap(userHeadBase64);
			Drawable drawable = new BitmapDrawable(userHeadBitMap);
			carPic.setBackgroundDrawable(drawable);
		}
		
		monthlyBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CarInfo carInfo = new CarInfo();
				carInfo.setCarBrand(monthlyCarData.getCarBrand());
				carInfo.setCarCode(monthlyCarData.getCarCode());
				carInfo.setCarColor(monthlyCarData.getCarColor());
				carInfo.setCarId(monthlyCarData.getCarId());
				carInfo.setCustomerId(monthlyCarData.getCustomerId());
				Intent intent = new Intent(mContext, CarInfoEditActivity.class);
				intent.putExtra("service_type", MainActivity.monthlyServiceList.get(0).getFeeType());
				intent.putExtra("server_cls", (Parcelable)MainActivity.monthlyServiceList.get(0));
				intent.putExtra("choice_car", (Parcelable)carInfo);
				intent.putExtra("is_need_last_car", true);
				startActivity(intent);
				finish();
			}
		});
		
		Button backBtn = (Button) findViewById(R.id.backbtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	/**
	 * 设置控件的宽高度
	 */
	public void setHwView() {
		int displayHeight = ((XiwaoApplication) getApplication())
				.getDisplayHeight();
		int displayWidth = ((XiwaoApplication) getApplication())
				.getDisplayWidth();
		// title高度
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		carPic.setImageBitmap(null);	//此处将ImageView的背景bitmap设置为空，断开setImageBitmap对bitmap的引用，然后才能回收bitmap，否则后面回收方法将不起效果
		if(userHeadBitMap != null && !userHeadBitMap.isRecycled()){
			userHeadBitMap.recycle();
			userHeadBitMap = null;
		}
		System.gc();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
