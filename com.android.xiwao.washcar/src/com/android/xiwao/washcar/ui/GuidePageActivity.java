package com.android.xiwao.washcar.ui;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.android.xiwao.washcar.LocalSharePreference;
import com.android.xiwao.washcar.R;
import com.android.xiwao.washcar.listadapter.MyPageAdapter;
import com.android.xiwao.washcar.utils.PageUtil;

public class GuidePageActivity extends Activity implements OnPageChangeListener {
	private ViewPager vp_guide;
	private List<View> pageList;
	/** ����ײ���ָʾԲ������ */
	private LinearLayout layout_dotView;
	private ImageView[] imgDots;
	/** ͳ��ҳ������ */
	private int dotCount;
	private int curPage;
	
	// Preference���ݴ洢����
	private LocalSharePreference mLocalSharePref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide_page);
		mLocalSharePref = new LocalSharePreference(this);
		initView();
		initDots();
		setPage();
	}

	private void initView() {
		layout_dotView = (LinearLayout) findViewById(R.id.layout_dotView);
		vp_guide = (ViewPager) findViewById(R.id.vp_guide);
		vp_guide.setOnPageChangeListener(this);
		vp_guide.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				AppLog.v("", msg)
				if(curPage == 2){
					mLocalSharePref.putBooleanPref(LocalSharePreference.IF_FIRST_USE, true);
					finish();
				}
			}
		});
		pageList = PageUtil.getPageList(this);
		dotCount = pageList.size();
	}

	/** ���õײ�Բ�� */
	private void initDots() {
		imgDots = new ImageView[dotCount];
		for (int i = 0; i < dotCount; i++) {
			ImageView dotView = new ImageView(this);
			if (i == 0) {
				dotView.setBackgroundResource(R.drawable.guide_note_01);
			} else {
				dotView.setBackgroundResource(R.drawable.guide_note_02);
			}
			imgDots[i] = dotView;
			// ����Բ�㲼�ֲ���
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(20, 0, 20, 0);
			dotView.setLayoutParams(params);
			layout_dotView.addView(dotView);
		}
	}

	private void setPage() {
		vp_guide.setAdapter(new MyPageAdapter(pageList));
		if (PageUtil.isCycle) {
			/*
			 * �˴����õ�ǰҳ����ʾλ��,������100(���ʲô��,��΢������)�� ����ʵ������ѭ��,��Ȼ�������Ƶ�,����һ�������ûɶ����
			 */
			vp_guide.setCurrentItem(100);
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		if (PageUtil.isCycle) {
			arg0 = arg0 % dotCount;
		}
		curPage = arg0;
		for (int i = 0; i < dotCount; i++) {
			if (i == arg0) {
				imgDots[i].setBackgroundResource(R.drawable.guide_note_01);
			} else {
				imgDots[i].setBackgroundResource(R.drawable.guide_note_02);
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}
}
