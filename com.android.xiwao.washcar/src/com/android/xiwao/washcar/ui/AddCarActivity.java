package com.android.xiwao.washcar.ui;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.ui.CarInfoEditFragment.SpinnerXMLSelectedListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddCarActivity extends Activity{
	private RelativeLayout carType;
	private RelativeLayout carNum;
	private RelativeLayout carBrand;
	private RelativeLayout carColor;
	private RelativeLayout carPic;
	
	private Button submitBtn;
	private Spinner spinnerCarType;
	private EditText carNumEdt;
	private EditText carBrandEdt;
	private EditText carColorEdt;
	
	private ArrayAdapter typeAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		
		setContentView(R.layout.add_car);
		initContentView();
		setHwView();
		initAdapter();
	}
	
	public void initContentView() {
		// TODO Auto-generated method stub
		carType = (RelativeLayout) findViewById(R.id.car_type);
		carNum = (RelativeLayout) findViewById(R.id.car_num);
		carBrand = (RelativeLayout) findViewById(R.id.car_brand);
		carColor = (RelativeLayout) findViewById(R.id.car_color);
		carPic = (RelativeLayout) findViewById(R.id.car_pic);
		submitBtn = (Button) findViewById(R.id.submit);
		spinnerCarType = (Spinner) findViewById(R.id.spinner_car_type);
		carNumEdt = (EditText) findViewById(R.id.car_num_edt);
		carBrandEdt = (EditText) findViewById(R.id.car_brand_edt);
		carColorEdt = (EditText) findViewById(R.id.car_color_edt);
		
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.car_info);
		
		submitBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				radioButton.setChecked(true);
				finish();
			}
		});
	}

	private void setHwView() {
		int displayHeight = ((XiwaoApplication)getApplication()).getDisplayHeight();
		int displayWidth = ((XiwaoApplication)getApplication()).getDisplayWidth();
		//title高度
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT
				, (int)(displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
		// 服务类型
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.04f + 0.5f), 0,
				0);
		carNum.setLayoutParams(params);
		// 车牌号码
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.001f + 0.5f),
				0, 0);
		
		carType.setLayoutParams(params);
		// 所在网点
		carColor.setLayoutParams(params);
		// 联系电话
		carBrand.setLayoutParams(params);
		//照片
		carPic.setLayoutParams(params);

		params = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.94f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (displayWidth * 0.03f + 0.5f),
				(int) (displayHeight * 0.1f + 0.5f),
				(int) (displayWidth * 0.03f + 0.5f), 0);
		submitBtn.setLayoutParams(params);
	}

	private void initAdapter() {
		typeAdapter = ArrayAdapter.createFromResource(this, R.array.car_types,
				android.R.layout.simple_spinner_item);
		// 设置下拉列表的风格
		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter2 添加到spinner中
		spinnerCarType.setAdapter(typeAdapter);

		// 添加事件Spinner事件监听
		spinnerCarType
				.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
	}

	class SpinnerXMLSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
//			view2.setText("你使用什么样的手机：" + adapter2.getItem(arg2));
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
