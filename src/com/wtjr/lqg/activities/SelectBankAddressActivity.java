package com.wtjr.lqg.activities;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
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
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.LocationUtil;
import com.wtjr.lqg.utils.SelectAddressUtil;
import com.wtjr.lqg.utils.LocationUtil.OnLocationListener;
/**
 * 选择银行地址省
 * @author Myf
 *
 */
public class SelectBankAddressActivity extends BaseActivity implements
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
	 * 省列表
	 */
	private ListView lvProvince;
	/**
	 * 省数据
	 */
	private ProvinceAdapter<String> provinceAdapter;
	/**
	 * 当前位置
	 */
	private TextView tvCurronLoaction;
	/**
	 * 地址名字	
	 */
	private String mLocationName;
    private Intent mIntent;
    private Location location;
	private BandBankType type;
    

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_select_bank_address);
		start();
	}
	
	@Override
	public void initIntent() {
	    mIntent = getIntent();
		type = (BandBankType) getIntent().getSerializableExtra(BandBankType.class.getName());
	}
	
	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
		lvProvince = (ListView) findViewById(R.id.lv_province);
		tvCurronLoaction = (TextView) findViewById(R.id.tv_curron_loaction);
	}

	@Override
	public void setListener() {
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
		
		lvProvince.setOnItemClickListener(this);
		
		tvCurronLoaction.setOnClickListener(this);
	}

	@Override
	public void setTitle() {
		// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
		imgBtnBack.setVisibility(View.VISIBLE);
		// 设置名字为:开户行所在地
		tvTitleName.setText(R.string.bank_address_name);
	}

	@Override
	public void initData() {
		try {
			if (provinceAdapter==null) {
				provinceAdapter = new ProvinceAdapter<String>(this,SelectAddressUtil.domXMLProvince(SelectBankAddressActivity.this));
				lvProvince.setAdapter(provinceAdapter);
			}else{
				provinceAdapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 得到地址名字
	 */
	private void getLocationName() {
		final ProgressDialog progressDialog = new ProgressDialog(this);
		// 设置进度条风格，风格为圆形，旋转的
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 提示信息
        progressDialog.setMessage("定位中...");
        // 设置ProgressDialog 的进度条是否不明确
        progressDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        progressDialog.setCancelable(true);
        // 让ProgressDialog显示
        progressDialog.show();  
            
	    
	    LocationUtil locationUtil  = new LocationUtil();
	    
	    locationUtil.getAddress(this, new OnLocationListener() {
            
            @Override
            public void location(Location location) {
                L.hintI("location="+location);
                if(location == null) {//定位失败
                    progressDialog.dismiss();
                }
            }
            
            @Override
            public void address(final String address) {
                L.hintI("address="+address);    
                
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
						Intent intent = null;
						if (type == BandBankType.noBandBank){
							intent = new Intent(SelectBankAddressActivity.this,BandBankActivity.class);
						}else{
							intent = new Intent(SelectBankAddressActivity.this,ChangeBankCardActivity.class);
						}
                        intent.putExtra("province", address);
                        startActivity(intent);
                    }
                }, 1000);
            }
        });

	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBtn_back:// 返回按钮点击操作
			finish();
			break;
		case R.id.tv_curron_loaction:// 返回按钮点击操作
		    getLocationName();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 省得适配器
	 * @author Myf
	 *
	 * @param <T>
	 */
	public class ProvinceAdapter<T> extends NewBaseAdapter<T> {

		public ProvinceAdapter(Context context, List<T> list) {
			super(context, list);
		}
		@Override
		public int getContentView() {
			return R.layout.bank_address_item;
		}

		@Override
		public void onInitView(View view, int position) {
		    String item = (String) getItem(position);
		    
		    L.hintI("onInitView="+position);
		    
			TextView tvProvince = (TextView) view.findViewById(R.id.tv_bank_address);
			tvProvince.setText(item + "");
		}

	}
	
   

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(SelectBankAddressActivity.this,SelectBankCityActivity.class);
		intent.putExtra("province", provinceAdapter.getItem(position) + "");
		intent.putExtra(BandBankType.class.getName(), type);
		startActivity(intent);
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			SelectAddressUtil.domXMLProvince(SelectBankAddressActivity.this).clear();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
