package com.android.xiwao.washcar.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.Constants;
import com.android.xiwao.washcar.R;

public class LoginActivity extends Activity{
	private Button loginBtn;
	private Button registerBtn;
	private Button forgetBtn;
	private TextView title;
	private EditText phoneEdt;
	private EditText pwdEdt;
	private LinearLayout phoneView;
	private LinearLayout passwordView;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDisHw();//获取屏幕分辨率，供后期使用
        setContentView(R.layout.login);
        initContentView();
        setHwView();
    } 
	
	public void initContentView() {
		// TODO Auto-generated method stub	
		loginBtn = (Button)findViewById(R.id.login_btn);
		registerBtn = (Button)findViewById(R.id.register_btn);
		forgetBtn = (Button)findViewById(R.id.forget_pwd);
		title = (TextView)findViewById(R.id.title);
		phoneEdt = (EditText)findViewById(R.id.phone_num);
		pwdEdt = (EditText)findViewById(R.id.password);
		phoneView = (LinearLayout)findViewById(R.id.phone_view);
		passwordView = (LinearLayout)findViewById(R.id.password_view);
		
		title.setText(this.getResources().getString(R.string.login));
		
		loginBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		registerBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
		
		forgetBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
				startActivity(intent);
			}
		});
	}
	/**
	 * 设置控件的宽高度
	 */
	public void setHwView(){
		//title高度
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT
				, (int)(Constants.displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);
		
		//登录按钮
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( (int) (Constants.displayWidth * 0.94f + 0.5f),
                (int) (Constants.displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (Constants.displayWidth * 0.03f + 0.5f), (int) (Constants.displayHeight * 0.06f + 0.5f)
				, (int) (Constants.displayWidth * 0.05f + 0.3f), 0);
		loginBtn.setLayoutParams(params);
		//注册按钮
		params = new LinearLayout.LayoutParams( (int) (Constants.displayWidth * 0.94f + 0.5f),
                (int) (Constants.displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (Constants.displayWidth * 0.03f + 0.5f), (int) (Constants.displayHeight * 0.03f + 0.5f)
				, (int) (Constants.displayWidth * 0.05f + 0.3f), 0);
		registerBtn.setLayoutParams(params);
		//手机号码输入框
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                (int) (Constants.displayHeight * 0.08f + 0.5f));
		phoneView.setLayoutParams(params);
		phoneView.setPadding((int) (Constants.displayWidth * 0.03f + 0.5f)
				, 0, 0, 0);
		//密码输入框
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                (int) (Constants.displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (Constants.displayWidth * 0.002f + 0.5f), 0, 0);
		passwordView.setLayoutParams(params);
		passwordView.setPadding((int) (Constants.displayWidth * 0.03f + 0.5f)
				, 0, 0, 0);
		//忘记密码按钮
		params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.setMargins((int) (Constants.displayWidth * 0.03f + 0.5f), (int) (Constants.displayHeight * 0.02f + 0.5f), 0, 0);
		forgetBtn.setLayoutParams(params);
	}
	/**
	 * 获取屏幕的宽高
	 */
	public void getDisHw(){
		DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Constants.displayWidth = displayMetrics.widthPixels;
        Constants.displayHeight = displayMetrics.heightPixels;
	}
}
