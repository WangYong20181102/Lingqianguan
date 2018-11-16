package com.wtjr.lqg.activities;

import java.util.ArrayList;
import java.util.List;

import org.xutils.http.RequestParams;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.NewBaseAdapter;
import com.wtjr.lqg.base.Investment;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.LqbInvestment;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.InvestmentDetailsType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout;
import com.wtjr.lqg.widget.pullRefresh.PullableListView;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout.OnRefreshListener;

/**
 * 零钱宝中的投资明细
 * 
 */
public class LqbInvestmentActivity extends BaseActivity implements OnClickListener,HttpRequesListener, OnRefreshListener, OnItemClickListener {
	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;

    
    private PullToRefreshLayout pullToRefreshLayout;

    private PullableListView pullableListView;
    
    private List<LqbInvestment> lqbInvestments = new ArrayList<LqbInvestment>();
    private RefreshType mRefreshType;
    
    private LqbInvestmentListAdapter<LqbInvestment> mAdapter;
    
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_lqb_invest_detail);
		start();

	}

	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
		// 下拉刷新
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullTo_refreshLayout);
        // ListView列表
        pullableListView = (PullableListView) findViewById(R.id.pullable_listView);
		
		// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
		imgBtnBack.setVisibility(View.VISIBLE);
	}

	@Override
	public void setListener() {
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
		httpUtil.setHttpRequesListener(this);
		
		pullToRefreshLayout.setOnRefreshListener(this);
        pullableListView.setOnItemClickListener(this);
        pullableListView.setPullLoadEnable(false);
	}

	@Override
	public void setTitle() {
		tvTitleName.setText(R.string.investment_mingxi_name);
	}

	@Override
	public void initData() {
	    mAdapter = new LqbInvestmentListAdapter<LqbInvestment>(this, lqbInvestments);
        pullableListView.setAdapter(mAdapter);
        
        sendLqbInvestmentRequest(RefreshType.RefreshNoPull);

	}
	
	/**
	 * 发送零钱宝投资记录请求
	 */
	private void sendLqbInvestmentRequest(RefreshType refreshType) {
	    mRefreshType = refreshType;
        RequestParams params = new RequestParams(Constant.LQB_QUERYLQBMATCHBORROWINFO);
        params.addBodyParameter("user_userunid", mUid);
        httpUtil.sendRequest(params,refreshType,this);
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
     * 零钱宝的投资明细界面的ＬｉｓｔＶｉｅｗ用的适配器
     * 
     * @author dell
     * 
     * @param <T>
     */
    public class LqbInvestmentListAdapter<T> extends NewBaseAdapter<T> {

        public LqbInvestmentListAdapter(Context context, List<T> list) {
            super(context, list);
        }

        // item布局
        public int getContentView() {
            return R.layout.item_lqg_invest_detail;
        }

        // 利用getViewChild直接获得item布局里的控件
        public void onInitView(View view, int position) {
            // 名称
            TextView name = getViewChild(view, R.id.tv_name);
            // 金额
            TextView account = getViewChild(view, R.id.tv_account);
            // 期数
            TextView status = getViewChild(view, R.id.tv_status);
            
            LqbInvestment lqbInvestment = lqbInvestments.get(position);
            
            status.setText("正常还款中"+lqbInvestment.periods+"/"+lqbInvestment.totalPeriods+"期");
             
            ProgressBar refundProgress = getViewChild(view, R.id.pb_refundProgress);
            refundProgress.setMax(lqbInvestment.totalPeriods);
            refundProgress.setProgress(lqbInvestment.periods);
            
            
            name.setText(lqbInvestment.pro_code);
            account.setText(MoneyFormatUtil.format(lqbInvestment.loanMoney));
        }
    }
	

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        LqbInvestment lqbInvestment = lqbInvestments.get(position);
        Investment investment = new Investment();
        investment.bCountdown  = lqbInvestment.borrowTime;
        investment.bName = lqbInvestment.pro_code;
        investment.bStates = 7;
        investment.bLoanMoney = lqbInvestment.loanMoney;
        investment.bLoanDays = lqbInvestment.totalPeriods;
        investment.bId = lqbInvestment.bId;
        investment.bYearRate = lqbInvestment.lqbRate;
        investment.bRepaymentDay = lqbInvestment.periods;
        investment.bInvestedMoney = (double)lqbInvestment.periods/(double)lqbInvestment.totalPeriods*lqbInvestment.loanMoney;
        
        L.hintD("investment.bInvestedMoney="+investment.bInvestedMoney);
        
        Intent intent = new Intent(LqbInvestmentActivity.this,InvestmentDetailsActivity.class);
        intent.putExtra(Investment.class.getName(),investment);
        intent.putExtra(InvestmentDetailsType.class.getName(),InvestmentDetailsType.LqbInvestment);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        sendLqbInvestmentRequest(RefreshType.RefreshPull);
    }

    @Override
    public void onLoadMore() {
        
    }
    

    @Override
    public void onFailure(String url, String errorContent) {
        if(mRefreshType == RefreshType.RefreshPull) {
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
        }
        
        noNetAndData(this, errorContent, lqbInvestments);
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        if(mRefreshType == RefreshType.RefreshPull) {
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        }
        
        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        String matchB = jsonObject.getString("matchB");
        
        lqbInvestments.clear();
        lqbInvestments.addAll(JSON.parseArray(matchB, LqbInvestment.class));
        
        
        mAdapter.notifyDataSetChanged();
        
        noNetAndData(this, jsonBase, lqbInvestments);
    }
}
