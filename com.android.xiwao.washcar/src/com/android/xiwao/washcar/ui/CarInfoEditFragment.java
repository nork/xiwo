package com.android.xiwao.washcar.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;

public class CarInfoEditFragment extends BaseFragment {
	private Context mContext;
	private View view;
	private RelativeLayout serverType;
	private RelativeLayout carNum;
	private RelativeLayout website;
	private RelativeLayout contactNum;
	private Button submitBtn;
	private Spinner spinnerServerType;
	private EditText carNumEdt;
	private EditText contactEdt;

	private ArrayAdapter typeAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		view = inflater.inflate(R.layout.car_info_edit, null);
		initContentView();
		setHwView();
		initAdapter();
		return view;
	}

	@Override
	public void initContentView() {
		// TODO Auto-generated method stub
		serverType = (RelativeLayout) view.findViewById(R.id.server_type);
		carNum = (RelativeLayout) view.findViewById(R.id.car_num);
		website = (RelativeLayout) view.findViewById(R.id.website);
		contactNum = (RelativeLayout) view.findViewById(R.id.contact);
		submitBtn = (Button) view.findViewById(R.id.submit);
		spinnerServerType = (Spinner) view
				.findViewById(R.id.spinner_server_type);
		carNumEdt = (EditText) view.findViewById(R.id.car_num_edt);
		contactEdt = (EditText) view.findViewById(R.id.contact_edt);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(R.string.car_info);
		
		submitBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				radioButton.setChecked(true);
				Fragment newFragment = new PayDetailFragment();
				FragmentTransaction transaction =getFragmentManager().beginTransaction();
				// Replace whatever is in thefragment_container view with this fragment,
				// and add the transaction to the backstack
				transaction.replace(R.id.content,newFragment);
				transaction.addToBackStack(null);
				//提交修改
				transaction.commit();
			}
		});
	}

	private void setHwView() {
		int displayHeight = ((XiwaoApplication)getActivity().getApplication()).getDisplayHeight();
		int displayWidth = ((XiwaoApplication)getActivity().getApplication()).getDisplayWidth();
		//title高度
		RelativeLayout title = (RelativeLayout) view.findViewById(R.id.header);
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
		typeAdapter = ArrayAdapter.createFromResource(mContext, R.array.types,
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
