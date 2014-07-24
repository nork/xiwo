package com.android.xiwao.washcar.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.data.CarInfo;
import com.android.xiwao.washcar.listadapter.CarInfoListAdapter;

public class CarInfoFragment extends BaseFragment {
	private Context mContext;
	private View view;
	private ListView carList;
	private CarInfoListAdapter carInfoListAdapter;
	private List<CarInfo> carInfoListData = new ArrayList<CarInfo>();

	private TextView title;
	private Button backBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.car_info_list, null);
		initContentView();
		initAdapter();
		fetchList();
		setHwView();
		return view;
	}

	@Override
	public void initContentView() {
		// TODO Auto-generated method stub
		mContext = this.getActivity();
		carList = (ListView) view.findViewById(R.id.car_list);
		backBtn = (Button) view.findViewById(R.id.backbtn);
		title = (TextView) view.findViewById(R.id.title);
		title.setText(getString(R.string.car_info));
	}

	/**
	 * 初始化Adapter
	 */
	private void initAdapter() {
		if (carInfoListData.size() > 0) {
			carInfoListData.clear();
		}
		carInfoListAdapter = new CarInfoListAdapter(mContext, false,
				R.layout.car_info_list_adapter);
		carList.setAdapter(carInfoListAdapter);
	}

	/**
	 * 填充数据
	 */
	private void fetchList() {
		CarInfo sigleCar = new CarInfo();
		for (int i = 0; i < 5; i++) {
			sigleCar.setCarAddr("银都西路申北三路97号");
			sigleCar.setCarNum("沪A888888");
			sigleCar.setWashFlag("洗");
			sigleCar.setWaxFlag("蜡");
			carInfoListData.add(sigleCar);
		}
		carInfoListAdapter.addBriefs(carInfoListData);
	}

	public void setHwView() {
		int displayHeight = ((XiwaoApplication)getActivity().getApplication()).getDisplayHeight();
//		int displayWidth = ((XiwaoApplication)getActivity().getApplication()).getDisplayWidth();
		// title高度
		RelativeLayout title = (RelativeLayout) view.findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
	}
}
