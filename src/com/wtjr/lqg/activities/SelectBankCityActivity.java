package com.wtjr.lqg.activities;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.NewBaseAdapter;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.enums.BandBankType;
import com.wtjr.lqg.utils.SelectAddressUtil;

/**
 * 选择银行地址城市
 * 
 * @author Myf
 * 
 */
public class SelectBankCityActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;
	/**
	 * 城市集合
	 */
	private CityAdapter<String> cityAdapter;
	private ListView lvCity;
	/**
	 * 上层传递下来的省
	 */
	private String mProvince;
	/**
	 * 显示省得
	 */
	private TextView tvProvince;
	private BandBankType type;
	private Intent intent = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_select_bank_city);
		start();
	}

	@Override
	public void initIntent() {
		mProvince = getIntent().getStringExtra("province");
		type = (BandBankType) getIntent().getSerializableExtra(BandBankType.class.getName());
	}

	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
		lvCity = (ListView) findViewById(R.id.lv_city);
		tvProvince = (TextView) findViewById(R.id.tv_province);

	}

	@Override
	public void setListener() {
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);

		lvCity.setOnItemClickListener(this);
	}

	@Override
	public void setTitle() {

		// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
		imgBtnBack.setVisibility(View.VISIBLE);
		// 设置名字为:选择城市
		tvTitleName.setText(R.string.select_bank_city_name);
	}

	@Override
	public void initData() {

		tvProvince.setText(mProvince);
		try {
			if (cityAdapter == null) {
				cityAdapter = new CityAdapter<String>(this,
						SelectAddressUtil.domXMLCitys(
								SelectBankCityActivity.this, mProvince));
				lvCity.setAdapter(cityAdapter);
			} else {
				cityAdapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBtn_back:// 返回按钮点击操作
			// 结束当前的Activity
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 城市得适配器
	 * 
	 * @author Myf
	 * 
	 * @param <T>
	 */
	public class CityAdapter<T> extends NewBaseAdapter<T> {

		public CityAdapter(Context context, List<T> list) {
			super(context, list);
		}

		@Override
		public int getContentView() {
			return R.layout.bank_address_item;
		}

		@Override
		public void onInitView(View view, int position) {
			TextView tvCity = (TextView) view
					.findViewById(R.id.tv_bank_address);
			tvCity.setText(getItem(position) + "");
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String city = (String) cityAdapter.getItem(position);
		if (type == BandBankType.noBandBank){
			intent = new Intent(SelectBankCityActivity.this,BandBankActivity.class);
		}else{
			intent = new Intent(SelectBankCityActivity.this,ChangeBankCardActivity.class);
		}
		intent.putExtra("province", mProvince);
		intent.putExtra("city", city);
		startActivity(intent);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			SelectAddressUtil.domXMLCitys(SelectBankCityActivity.this,
					mProvince).clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
