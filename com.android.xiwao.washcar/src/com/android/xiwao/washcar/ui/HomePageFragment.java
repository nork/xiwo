package com.android.xiwao.washcar.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;

public class HomePageFragment extends BaseFragment{
	private Context mContext;
	private Button washCarBtn;
	private Button washCarWaxBtn;
	private TextView title;
	private RadioGroup radioGroup;
	private RadioButton radioButton;
		
	private View view;

	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		mContext = getActivity();
        view = inflater.inflate(R.layout.home_page, null); 
        initContentView();
        setHwView();
        return view;  
    }
	
	@Override
	public void initContentView() {
		// TODO Auto-generated method stub
		radioGroup = (RadioGroup) getActivity().findViewById(R.id.rg_tab);
		radioButton = (RadioButton) getActivity().findViewById(R.id.car_info);
		washCarBtn = (Button)view.findViewById(R.id.wash_car);
		washCarWaxBtn = (Button)view.findViewById(R.id.wash_car_wax);
		title = (TextView)view.findViewById(R.id.title); 
		
		title.setText("首页");
		washCarBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				radioButton.setChecked(true);
//				Fragment newFragment = new CarInfoEditFragment();
//				FragmentTransaction transaction =getFragmentManager().beginTransaction();
//				// Replace whatever is in thefragment_container view with this fragment,
//				// and add the transaction to the backstack
//				transaction.replace(R.id.content,newFragment);
//				transaction.addToBackStack(null);
//				//提交修改
//				transaction.commit();
//				FragmentUtils.switchContent(FragmentFactory, transaction);
				Intent intent = new Intent(mContext, CarInfoEditActivity.class);
				intent.putExtra("service_type", 0);
				startActivity(intent);
			}
		});
		
		washCarWaxBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, CarInfoEditActivity.class);
				intent.putExtra("service_type", 2);
				startActivity(intent);
			}
		});
	}
	
	public void setHwView(){
		int displayHeight = ((XiwaoApplication)getActivity().getApplication()).getDisplayHeight();
//		int displayWidth = ((XiwaoApplication)getActivity().getApplication()).getDisplayWidth();
		RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT
				, LayoutParams.WRAP_CONTENT);
//		btnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		btnParams.setMargins(0, 0, 0, (int)(displayHeight * 0.1f + 0.5f));
		washCarBtn.setLayoutParams(btnParams);
		
		btnParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT
				, LayoutParams.WRAP_CONTENT);
//		btnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		btnParams.setMargins((int)(displayHeight * 0.1f + 0.5f), 0
				, 0, (int)(displayHeight * 0.1f + 0.5f));
		btnParams.addRule(RelativeLayout.RIGHT_OF, R.id.wash_car);
		washCarWaxBtn.setLayoutParams(btnParams);
		
		RelativeLayout title = (RelativeLayout) view.findViewById(R.id.header);
		RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT
				, (int)(displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
	}
}
