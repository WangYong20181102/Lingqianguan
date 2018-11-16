package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout;
import com.wtjr.lqg.widget.pullRefresh.PullableScrollView;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout.OnRefreshListener;

/**
 * 更换银行卡
 * @author JoLie
 *
 */
public class ReplaceBankCardActivity extends BaseActivity implements OnClickListener,HttpRequesListener , OnRefreshListener{
	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;
	private PullToRefreshLayout pullToRefreshLayout;
	private PullableScrollView pullableScrollView;
    private Button button;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_replace_bank_card);
		start();

	}

	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
		
		button = (Button) findViewById(R.id.button);
		
		// 下拉刷新
		pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullTo_refreshLayout);
		pullableScrollView = (PullableScrollView) findViewById(R.id.pullable_scrollView);
	}

	@Override
	public void setListener() {
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
		httpUtil.setHttpRequesListener(this);
		pullToRefreshLayout.setOnRefreshListener(this);
		
		button.setOnClickListener(this);
	}

	@Override
	public void setTitle() {
		// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
		imgBtnBack.setVisibility(View.VISIBLE);
		// 设置名字为:活动
		tvTitleName.setText("更换银行卡");

	}

	@Override
	public void initData() {

	}
	
	public void click(View view) {

    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBtn_back:// 返回按钮点击操作
			// 结束当前的Activity
			finish();
			break;
		case R.id.button:// 
		    sendReplaceBankCardRequest(RefreshType.RefreshNoPull);
            break;
		default:
			break;
		}
	}
	
	
    /**
     * 发送更换银行卡请求
     */
    public void sendReplaceBankCardRequest(RefreshType refreshType) {
//        String nickName = edtNickName.getText().toString();
//        if (TextUtils.isEmpty(nickName)) {
//            Toast.makeText(this, "请填写昵称", Toast.LENGTH_SHORT).show();
//            return;
//        }

        RequestParams params = new RequestParams(Constant.SETTINGUP_REPLACEBANKCARD);
        params.addBodyParameter("user_userunid", "U_20160627141442822094707");
        //开户行所在地
        params.addBodyParameter("openAccount_loc", "123");
        //开户支行
        params.addBodyParameter("openAccount_branch", "123");
        httpUtil.sendRequest(params,refreshType,this);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        
    }

	@Override
	public void onRefresh() {
		pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
	}

	@Override
	public void onLoadMore() {
		pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
	}

}
