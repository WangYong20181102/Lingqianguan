package com.wtjr.lqg.activities;

import java.util.ArrayList;
import java.util.List;

import org.xutils.http.RequestParams;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.NewBaseAdapter;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout;
import com.wtjr.lqg.widget.pullRefresh.PullableListView;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout.OnRefreshListener;

/**
 * 零钱宝中的投资明细
 * 
 * @author Myf
 * 
 */
public class LqbInvestDetailActivity extends BaseActivity implements OnClickListener,HttpRequesListener, OnRefreshListener, OnItemClickListener {
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
		
		
		ArrayList<String> strs = new ArrayList<String>();
		for (int i = 0; i < 3; i++) {
		    strs.add(i+"");
        }
		
		LqgInvestDetailListAdapter<String> adapter = new LqgInvestDetailListAdapter<String>(this, strs);
		pullableListView.setAdapter(adapter);
	}

	@Override
	public void setListener() {
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
		httpUtil.setHttpRequesListener(this);
		
		pullToRefreshLayout.setOnRefreshListener(this);
        pullableListView.setOnItemClickListener(this);
	}

	@Override
	public void setTitle() {
		tvTitleName.setText(R.string.investment_mingxi_name);
	}

	@Override
	public void initData() {
	    RefreshType refreshType = RefreshType.RefreshNoPull;
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
    public class LqgInvestDetailListAdapter<T> extends NewBaseAdapter<T> {

        public LqgInvestDetailListAdapter(Context context, List<T> list) {
            super(context, list);
        }

        // item布局
        public int getContentView() {
            return R.layout.item_lqg_invest_detail;
        }

        // 利用getViewChild直接获得item布局里的控件
        public void onInitView(View view, int position) {
//            // 名称
//            TextView name = getViewChild(view, R.id.tv_name);
//            // 金额
//            TextView account = getViewChild(view, R.id.tv_account);
//            // 期数
//            TextView status = getViewChild(view, R.id.tv_status);
//            
//            LqgInvestDetail lqgInvestDetailItem = investDetailItems.get(position);
//            
//            
//            status.setText("正常还款中"+lqgInvestDetailItem.qi+"/"+lqgInvestDetailItem.time_limit+"期");
//            
//            status.measure(measureWidth, measureHeigh);
//            int Width = status.getMeasuredWidth();
////          int Height = status.getMeasuredHeight();
//             
//            ProgressBar refundProgress = getViewChild(view, R.id.pb_refundProgress);
//            refundProgress.setMax(Integer.parseInt(lqgInvestDetailItem.time_limit));
//            
//            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) refundProgress.getLayoutParams(); 
//            linearParams.width = Width;
//            refundProgress.setLayoutParams(linearParams);
//        
//            refundProgress.setProgress(Integer.parseInt(lqgInvestDetailItem.qi));
//            
//            
//            name.setText(lqgInvestDetailItem.name);
//            account.setText(MoneyFormat.format(lqgInvestDetailItem.account));
            
        }
    }
	

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        
    }
    

    @Override
    public void onFailure(String url, String errorContent) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        
    }

}
