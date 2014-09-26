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
import android.text.method.ReplacementTransformationMethod;
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
import com.android.xiwao.washcar.AppLog;
import com.android.xiwao.washcar.ClientSession;
import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.XiwaoApplication;
import com.android.xiwao.washcar.httpconnection.BaseCommand;
import com.android.xiwao.washcar.httpconnection.BaseResponse;
import com.android.xiwao.washcar.httpconnection.CarRegister;
import com.android.xiwao.washcar.httpconnection.CommandExecuter;
import com.android.xiwao.washcar.utils.DialogUtils;
import com.android.xiwao.washcar.utils.FileUtil;
import com.android.xiwao.washcar.utils.StringUtils;

@SuppressLint("NewApi")
public class AddCarActivity extends Activity {
	private RelativeLayout carType;
	private RelativeLayout carNum;
	private RelativeLayout carBrand;
	private RelativeLayout carColor;
	private RelativeLayout carPic;

	private Button submitBtn;
	private Button backBtn;
	private Spinner spinnerCarType;
	private EditText carNumEdt;
	private EditText carBrandEdt;
	private EditText carColorEdt;
	private Button addImgBtn;

	@SuppressWarnings("rawtypes")
	private ArrayAdapter typeAdapter;

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
	private String carPicBase64;	//汽车照片的BASE64字串

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ActivityManage.getInstance().addActivity(this);
		ActivityManage.getInstance().setCurContext(this);

		mLocalSharePref = new LocalSharePreference(this);
		setContentView(R.layout.add_car);

		initExecuter();
		initUtils();
		initContentView();
		setHwView();
		initAdapter();
	}

	/**
	 * 
	 */
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
		backBtn = (Button) findViewById(R.id.backbtn);
		addImgBtn = (Button) findViewById(R.id.add);

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.car_info);

		addImgBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});

		submitBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addCar(carNumEdt.getText().toString().trim(), carBrandEdt
						.getText().toString().trim(), carColorEdt.getText()
						.toString().trim(),
						spinnerCarType.getSelectedItemPosition(), carPicBase64);
			}
		});

		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		   
		carNumEdt.setTransformationMethod(new InputLowerToUpper());	//添加小写转大写监听
	}

	private void addCar(String carCode, String carBrand, String carColor,
			int type, String carPic) {
		if(carCode.length() <= 0){
			dialogUtils.showToast(getString(R.string.car_code_null_error));
			return;
		}
		char firstCarCode = carCode.charAt(0);
		String secondPart = carCode.substring(1);
		if (carCode.length() != 7 || !StringUtils.isChinese(firstCarCode) || StringUtils.containsChinese(secondPart)) {
			dialogUtils.showToast(getString(R.string.car_code_null_error));
			return;
		} else if (carBrand.length() <= 0) {
			dialogUtils.showToast(getString(R.string.car_brand_null_error));
			return;
		} else if (carColor.length() <= 0) {
			dialogUtils.showToast(getString(R.string.car_color_null_error));
			return;
		} 
		long customerId = mLocalSharePref.getUserId();
		String carType = null;
		switch (type) {
		case 0:
			carType = "00";
			break;
		case 1:
			carType = "01";
			break;
		}
		BaseCommand carRegister = ClientSession
				.getInstance()
				.getCmdFactory()
				.getCarRegister(carCode, carBrand, carColor, carType, carPic,
						customerId);

		mExecuter.execute(carRegister, mRespHandler);

		dialogUtils.showProgress();
	}

	public void onCarRegisterSuccess() {
		setResult(RESULT_OK);
		finish();
	}

	/**
	 * 处理服务器返回的车辆注册结果
	 * 
	 * @param rsp
	 *            服务返回的车辆注册结果信息
	 */
	private void onReceiveCarRegisterResponse(BaseResponse rsp) {

		if (!rsp.isOK()) {
			String error = getString(R.string.protocol_error) + "(" + rsp.errno
					+ ")";
			dialogUtils.showToast(error);
		} else {
			CarRegister.Response carRegisterRsp = (CarRegister.Response) rsp;
			if (carRegisterRsp.responseType.equals("N")) {
				onCarRegisterSuccess();
				dialogUtils.showToast(carRegisterRsp.errorMessage);
			} else {
				dialogUtils.showToast(carRegisterRsp.errorMessage);
			}
		}
	}

	private CommandExecuter.ResponseHandler mRespHandler = new CommandExecuter.ResponseHandler() {

		public void handleResponse(BaseResponse rsp) {
			onReceiveCarRegisterResponse(rsp);
		}

		public void handleException(IOException e) {
			dialogUtils.showToast(getString(R.string.connection_error));
		}

		public void onEnd() {
			dialogUtils.dismissProgress();
		}
	};

	private void setHwView() {
		int displayHeight = ((XiwaoApplication) getApplication())
				.getDisplayHeight();
		int displayWidth = ((XiwaoApplication) getApplication())
				.getDisplayWidth();
		// title高度
		RelativeLayout title = (RelativeLayout) findViewById(R.id.header);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (displayHeight * 0.08f + 0.5f));
		title.setLayoutParams(titleParams);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (displayHeight * 0.16f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.04f + 0.5f), 0, 0);
		carPic.setLayoutParams(params);
		
		// 车牌号码
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins(0, (int) (displayHeight * 0.001f + 0.5f), 0, 0);

		carNum.setLayoutParams(params);
		carType.setLayoutParams(params);
		// 所在网点
		carColor.setLayoutParams(params);
		// 联系电话
		carBrand.setLayoutParams(params);

		params = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.94f + 0.5f),
				(int) (displayHeight * 0.08f + 0.5f));
		params.setMargins((int) (displayWidth * 0.03f + 0.5f),
				(int) (displayHeight * 0.1f + 0.5f),
				(int) (displayWidth * 0.03f + 0.5f), 0);
		submitBtn.setLayoutParams(params);

//		RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(
//				(int) (displayHeight * 0.12f + 0.5f),
//				(int) (displayHeight * 0.12f + 0.5f));
//		imgParams.setMargins(0, (int) (displayHeight * 0.04f + 0.5f), 0, 0);
//		addImgBtn.setLayoutParams(imgParams);
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
			// view2.setText("你使用什么样的手机：" + adapter2.getItem(arg2));
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
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

	private void setPicToViewAndSave(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			Bitmap photo = bundle.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			addImgBtn.setBackgroundDrawable(drawable);
//			addImgBtn.setBackground(drawable);
			addImgBtn.setText("");
			carPicBase64 = FileUtil.bitmapToBase64(photo);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		switch (requestCode) {
		case PHOTO_REQUEST_TAKEPHOTO:
			startPhotoZoom(Uri.fromFile(tempFile), 150);
			break;

		case PHOTO_REQUEST_GALLERY:
			if (data != null)
				startPhotoZoom(data.getData(), 150);
			break;

		case PHOTO_REQUEST_CUT:
			if (data != null)
				setPicToViewAndSave(data);
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	// 提示对话框方法
	private void showDialog() {
		new AlertDialog.Builder(this)
				.setTitle("汽车靓照")
				.setPositiveButton("拍照", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						// 调用系统的拍照功能
						tempFile = new File(FileUtil.createFileOnSD(
								"/xiwao/img/", getPhotoFileName()));
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		ActivityManage.getInstance().setCurContext(this);
		super.onResume();
	}
	
	/**
	 * 小写转大写
	 * @author hpq
	 *
	 */
	public class InputLowerToUpper extends ReplacementTransformationMethod{ 
	    @Override 
	    protected char[] getOriginal() { 
	        char[] lower = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z' }; 
	        return lower; 
	    } 
	   
	    @Override 
	    protected char[] getReplacement() { 
	        char[] upper = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' }; 
	        return upper; 
	    } 
	   
	} 
}
