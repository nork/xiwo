package com.android.xiwao.washcar.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;

public class UserProtocalActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ActivityManage.getInstance().addActivity(this);
		ActivityManage.getInstance().setCurContext(this);
		setContentView(R.layout.user_server_protocal);
		initContentView();
		setHwView();
	}
	
	private void initContentView(){		
		Button backBtn = (Button) findViewById(R.id.backbtn);
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	private void setHwView() {
		int displayHeight = ((XiwaoApplication) getApplication())
				.getDisplayHeight();
		int displayWidth = ((XiwaoApplication) getApplication())
				.getDisplayWidth();
		// title¸ß¶È
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
	}
}
