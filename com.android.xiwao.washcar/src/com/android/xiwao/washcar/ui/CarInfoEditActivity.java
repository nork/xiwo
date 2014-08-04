package com.android.xiwao.washcar.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;

public class CarInfoEditActivity extends Activity {
	private Context mContext;
	private RelativeLayout serverType;
	private RelativeLayout carNum;
	private RelativeLayout website;
	private RelativeLayout contactNum;
	private Button submitBtn;
	private Button backBtn;
	private Spinner spinnerServerType;
	private TextView carNumEdt;
	private EditText contactEdt;

	private ArrayAdapter typeAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);
		
		mContext = this;
		setContentView(R.layout.car_info_edit);
		initContentView();
		setHwView();
		initAdapter();
	}

	public void initContentView() {
		// TODO Auto-generated method stub
		serverType = (RelativeLayout) findViewById(R.id.server_type);
		carNum = (RelativeLayout) findViewById(R.id.car_num);
		website = (RelativeLayout) findViewById(R.id.website);
		contactNum = (RelativeLayout) findViewById(R.id.contact);
		submitBtn = (Button) findViewById(R.id.submit);
		spinnerServerType = (Spinner) findViewById(R.id.spinner_server_type);
		carNumEdt = (TextView) findViewById(R.id.car_num_edt);
		contactEdt = (EditText) findViewById(R.id.contact_edt);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.car_info);
		
		submitBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				radioButton.setChecked(true);
				Intent intent = new Intent(CarInfoEditActivity.this, PayDetailActivity.class);
				startActivity(intent);
			}
		});
		
		backBtn = (Button)findViewById(R.id.backbtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		carNum.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CarInfoEditActivity.this, AddCarActivity.class);
				startActivity(intent);
			}
		});
		
		website.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CarInfoEditActivity.this, AddressActivity.class);
				startActivity(intent);
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
		serverType.setLayoutParams(params);
		// 车牌号码
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.001f + 0.5f),
				0, 0);
		carNum.setLayoutParams(params);
		// 所在网点
		website.setLayoutParams(params);
		// 联系电话
		contactNum.setLayoutParams(params);

		params = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.94f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (displayWidth * 0.03f + 0.5f),
				(int) (displayHeight * 0.1f + 0.5f),
				(int) (displayWidth * 0.03f + 0.5f), 0);
		submitBtn.setLayoutParams(params);
	}

	private void initAdapter() {
		typeAdapter = ArrayAdapter.createFromResource(mContext, R.array.server_types,
				android.R.layout.simple_spinner_item);
		// 设置下拉列表的风格
		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter2 添加到spinner中
		spinnerServerType.setAdapter(typeAdapter);

		// 添加事件Spinner事件监听
		spinnerServerType
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
