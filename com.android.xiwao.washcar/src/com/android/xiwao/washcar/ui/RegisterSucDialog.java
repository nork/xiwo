package com.android.xiwao.washcar.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.R;

public class RegisterSucDialog extends Activity{
	private Button sureBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_suc_dialog);
		initContentView();
		setHwView();
	}
	
	private void initContentView(){
		sureBtn = (Button)findViewById(R.id.sure_btn);
		sureBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	private void setHwView(){
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (Constants.displayWidth * 0.8f + 0.5f),
                (int) (Constants.displayHeight * 0.06f + 0.5f));
		sureBtn.setLayoutParams(params);
	}
}
