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
	/** 界面底部的指示圆点容器 */
	private LinearLayout layout_dotView;
	private ImageView[] imgDots;
	/** 统计页卡个数 */
	private int dotCount;
	private int curPage;
	
	// Preference数据存储对象
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

	/** 设置底部圆点 */
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
			// 设置圆点布局参数
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
			 * 此处设置当前页的显示位置,设置在100(随便什么数,稍微大点就行)就 可以实现向左循环,当然是有限制的,不过一般情况下没啥问题
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
