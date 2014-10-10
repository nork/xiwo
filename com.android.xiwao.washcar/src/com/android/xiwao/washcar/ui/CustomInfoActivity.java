package com.android.xiwao.washcar.ui;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xiwao.washcar.ActivityManage;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.httpconnection.AccountQuery;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.httpconnection.CustomerModify;
import com.android.xiwao.washcar.utils.DialogUtils;
import com.android.xiwao.washcar.utils.FileUtil;
import com.android.xiwao.washcar.utils.StringUtils;

@SuppressLint("NewApi")
public class CustomInfoActivity extends Activity {
	private EditText userTxt;
	private EditText emailTxt;
	private TextView phoneTxt;

	private Button backBtn;
	private Button modifyBtn;
//	private Button addBtn;
	private ImageView headImg;
	private RelativeLayout accountMoneyPart;
	private TextView accountMoneyTxt;

	// 工具
	private DialogUtils dialogUtils;

	// Preference数据存储对象
	private LocalSharePreference mLocalSharePref;

	// 网络访问相关对象
	private Handler mHandler;
	private CommandExecuter mExecuter;

	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	// 创建一个以当前时间为名称的文件
	private File tempFile;
	private String headImgBase64;
	
	private Bitmap userHeadBitMap;
	private int accountBalance = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManage.getInstance().setCurContext(this);
		ActivityManage.getInstance().addActivity(this);

		mLocalSharePref = new LocalSharePreference(this);

		setContentView(R.layout.customer_info);
		initExecuter();
		initUtils();
		initContentView();
		setViewHw();
	}

	public void initContentView() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.custom_info);

		userTxt = (EditText) findViewById(R.id.user_name_txt);
		emailTxt = (EditText) findViewById(R.id.email_txt);
		phoneTxt = (TextView) findViewById(R.id.phone_txt);
		modifyBtn = (Button) findViewById(R.id.modify_btn);
//		addBtn = (Button) findViewById(R.id.add);
		headImg = (ImageView) findViewById(R.id.head);
		accountMoneyPart = (RelativeLayout) findViewById(R.id.account_money);
		accountMoneyTxt = (TextView) findViewById(R.id.account_money_txt);

		String nickName = mLocalSharePref.getNickName();
		if(nickName.equals("null") || nickName == null || nickName.equals("")){
			nickName = "";
		}
		userTxt.setText(nickName);
		String email = mLocalSharePref.getUserEmail();
		if(email.equals("null") || email == null || email.equals("")){
			email = "";
		}
		emailTxt.setText(email);
		phoneTxt.setText(mLocalSharePref.getUserName());

		backBtn = (Button) findViewById(R.id.backbtn);
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		modifyBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String btnTxt = modifyBtn.getText().toString();
				if (btnTxt.equals(getString(R.string.modify_info))) {
					emailTxt.setFocusable(true);
					emailTxt.setFocusableInTouchMode(true);
					userTxt.setFocusable(true);
					userTxt.setFocusableInTouchMode(true);
					userTxt.requestFocus();
					userTxt.requestFocusFromTouch();
					modifyBtn.setText(getString(R.string.submit));
//					addBtn.setVisibility(View.VISIBLE);
					headImg.setClickable(true);
				} else if (btnTxt.equals(getString(R.string.submit))) {
					modifyUserInfo(userTxt.getText().toString(), emailTxt
							.getText().toString(), headImgBase64);
				}
			}
		});

		headImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});
		headImg.setClickable(false);
		
		String userHeadBase64 = mLocalSharePref.getUserHead();
		if(!userHeadBase64.equals("") && userHeadBase64 != null && !userHeadBase64.equals("null")){
			userHeadBitMap = FileUtil.base64ToBitmap(userHeadBase64);
			Drawable drawable = new BitmapDrawable(userHeadBitMap);
            headImg.setBackgroundDrawable(drawable);
//			headImg.setBackground(drawable);
		}
		
		accountMoneyPart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CustomInfoActivity.this, RechargeActivity.class);
				startActivity(intent);
			}
		});
	}

	private void modifyUserInfo(String userName, String email, String headImg) {
		BaseCommand carRegister = ClientSession.getInstance().getCmdFactory()
				.getCustomerModify(mLocalSharePref.getUserId(), userName, email, headImg);

		mExecuter.execute(carRegister, mCustomerModifyRespHandler);

		dialogUtils.showProgress();
	}

	public void onCustomerModifySuccess() {
		modifyBtn.setText(getString(R.string.modify_info));
		mLocalSharePref.setNickName(userTxt.getText().toString());
		mLocalSharePref.setUserEmail(emailTxt.getText().toString());
		emailTxt.setFocusable(false);
		userTxt.setFocusable(false);
//		addBtn.setVisibility(View.GONE);
		headImg.setClickable(false);
	}

	/**
	 * 处理服务器返回的车辆注册结果
	 * 
	 * @param rsp
	 *            服务返回的车辆注册结果信息
	 */
	private void onReceiveCustomerModifyResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			CustomerModify.Response customerModify = (CustomerModify.Response) rsp;
			if (customerModify.responseType.equals("N")) {
				onCustomerModifySuccess();
				dialogUtils.showToast(customerModify.errorMessage);
			} else {
				dialogUtils.showToast(customerModify.errorMessage);
			}
		}
	}

	
	private CommandExecuter.ResponseHandler mCustomerModifyRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveCustomerModifyResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};
	
	/**
	 * 账户余额查询
	 */
	private void getBalance(){
		BaseCommand accountQuery = ClientSession.getInstance().getCmdFactory()
				.getAccountQuery(mLocalSharePref.getUserId());

		mExecuter.execute(accountQuery, mAccountQueryRespHandler);

		dialogUtils.showProgress();
	}
	
	public void onAccountQuerySuccess(long accountInfo){
		accountBalance = (int) accountInfo;
//		String accountMoneyStr = Integer.toString(accountBalance);
		String accountMoneyStr = StringUtils.getPriceStr(accountBalance);
		if(accountBalance > 0){
//			accountMoneyTxt.setText(accountMoneyStr.substring(0, accountMoneyStr.length() - 2)
//					+ "." + accountMoneyStr.substring(accountMoneyStr.length() - 2));
			accountMoneyTxt.setText(accountMoneyStr);
		}
	}
	
	/**
	 * 处理服务器返回的车辆注册结果
	 * @param rsp 服务返回的车辆注册结果信息
	 */
	private void onReceiveAccountQueryResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			AccountQuery.Response accountQueryRsp = (AccountQuery.Response) rsp;
			if (accountQueryRsp.responseType.equals("N")) {
				onAccountQuerySuccess(accountQueryRsp.accountInfo);
//				dialogUtils.showToast(accountQueryRsp.errorMessage);
			} else {
//				dialogUtils.showToast(accountQueryRsp.errorMessage);
			}
		}
	}
	
	private CommandExecuter.ResponseHandler mAccountQueryRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveAccountQueryResponse(rsp);
		}

		public void handleException(IOException e) {
//			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};

	/**
	 * 
	 */
	public void setViewHw() {
		int displayHeight = ((XiwaoApplication) getApplication())
				.getDisplayHeight();
		int displayWidth = ((XiwaoApplication) getApplication())
				.getDisplayWidth();
		// title高度
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);

		// 头像部分
		RelativeLayout head = (RelativeLayout) findViewById(R.id.head_img);
		RelativeLayout name = (RelativeLayout) findViewById(R.id.name);
		RelativeLayout user = (RelativeLayout) findViewById(R.id.user);
		RelativeLayout email = (RelativeLayout) findViewById(R.id.email);
		RelativeLayout phone = (RelativeLayout) findViewById(R.id.phone);
		LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (displayHeight * 0.2f + 0.5f));
		listParams.setMargins(0, (int) (displayHeight * 0.04f + 0.5f), 0, 0);
		head.setLayoutParams(listParams);
		listParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		listParams.setMargins(0, (int) (displayHeight * 0.001f + 0.5f), 0, 0);
		name.setLayoutParams(listParams);
		user.setLayoutParams(listParams);
		email.setLayoutParams(listParams);
		phone.setLayoutParams(listParams);
		accountMoneyPart.setLayoutParams(listParams);

		LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.94f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f));
		btnParams.setMargins((int) (displayWidth * 0.03f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f),
				(int) (displayWidth * 0.03f + 0.5f), 0);
		modifyBtn.setLayoutParams(btnParams);

		// 头像和添加头像按钮的宽高度设置
		RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(
				(int) (displayHeight * 0.12f + 0.5f),
				(int) (displayHeight * 0.12f + 0.5f));
		imgParams.addRule(RelativeLayout.CENTER_VERTICAL);
		imgParams.addRule(RelativeLayout.RIGHT_OF, R.id.head_title);
		headImg.setLayoutParams(imgParams);

//		imgParams = new RelativeLayout.LayoutParams(
//				(int) (displayHeight * 0.1f + 0.5f),
//				(int) (displayHeight * 0.1f + 0.5f));
//		imgParams.setMargins((int) (displayWidth * 0.04f + 0.5f), 0, 0, 0);
//		imgParams.addRule(RelativeLayout.CENTER_VERTICAL);
//		imgParams.addRule(RelativeLayout.RIGHT_OF, R.id.head);
//		addBtn.setLayoutParams(imgParams);
	}

	/**
	 * 初始化需要的工具
	 */
	public void initUtils() {
		dialogUtils = new DialogUtils();
	}

	private void initExecuter() {

		mHandler = new Handler();

		mExecuter = new CommandExecuter();
		mExecuter.setHandler(mHandler);
	}

	// 提示对话框方法
	private void showDialog() {
		new AlertDialog.Builder(this)
				.setTitle("头像设置")
				.setPositiveButton("拍照", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						// 调用系统的拍照功能
						tempFile = new File(FileUtil.createFileOnSD("/xiwao/img/", getPhotoFileName()));
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						// 指定调用相机拍照后照片的储存路径
						intent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(tempFile));
						startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
					}
				})
				.setNegativeButton("相册", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Intent intent = new Intent(Intent.ACTION_PICK, null);
						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
					}
				}).show();
	}

	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'yyyyMMddHHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	
	private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
	
	private void setPicToViewAndSave(Intent picdata){
		Bundle bundle = picdata.getExtras();
        if (bundle != null) {
        	userHeadBitMap = bundle.getParcelable("data");
            headImgBase64 = FileUtil.bitmapToBase64(userHeadBitMap);
            mLocalSharePref.setUserHead(headImgBase64);
            
            ((XiwaoApplication)getApplication()).setIfNeedRefreshHeadImg(true);
            Drawable drawable = new BitmapDrawable(userHeadBitMap);
            headImg.setBackgroundDrawable(drawable);
//            headImg.setBackground(drawable);
        }
	}
	
	@SuppressLint("NewApi")
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {
        case PHOTO_REQUEST_TAKEPHOTO:
            startPhotoZoom(Uri.fromFile(tempFile), 100);
            break;

        case PHOTO_REQUEST_GALLERY:
            if (data != null)
                startPhotoZoom(data.getData(), 100);
            break;

        case PHOTO_REQUEST_CUT:
            if (data != null) 
                setPicToViewAndSave(data);
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ActivityManage.getInstance().setCurContext(this);
		getBalance();
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		headImg.setImageBitmap(null);	//此处将ImageView的背景bitmap设置为空，断开setImageBitmap对bitmap的引用，然后才能回收bitmap，否则后面回收方法将不起效果
		if(userHeadBitMap != null && !userHeadBitMap.isRecycled()){
			userHeadBitMap.recycle();
			userHeadBitMap = null;
		}
		System.gc();
	}
}
