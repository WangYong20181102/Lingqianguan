package com.wtjr.lqg.widget.pullRefresh;

import org.xutils.x;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wtjr.lqg.Manifest;
import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.LqgApplication;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.activities.MyInvestmentActivity;
import com.wtjr.lqg.activities.WelfareCardVoucherActivity;
import com.wtjr.lqg.activities.WelfareCardVoucherFailureActivity;

public class PullableListView extends ListView implements Pullable {

    private boolean mEnablePullRefresh = true;
    private boolean mEnablePullLoad = true;

    /**
     * 显示占无数据一条item的高度
     */
    private int mItemHeight;
    private Activity mActivity;
    private LqgApplication app;

    public PullableListView(Context context) {
        super(context);
    }

    public PullableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//	}

//	  /**
//     * 重写该方法，达到使ListView适应ScrollView的效果
//     */
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, expandSpec);
//    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean canPullDown() {
        if (mEnablePullRefresh) {
            int firstVisiblePosition = getFirstVisiblePosition();
            View childAt = getChildAt(0);
            if (getCount() == 0) {
                // 没有item的时候也可以下拉刷新
                return true;
            } else if (firstVisiblePosition == 0 && childAt != null
                    && childAt.getTop() >= 0) {
                // 滑到ListView的顶部了
                return true;
            } else
                return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean canPullUp() {
        if (mEnablePullLoad) {
            if (getCount() == 0) {
                // 没有item的时候也可以上拉加载
                return true;
            } else if (getLastVisiblePosition() == (getCount() - 1)) {
                // 滑到底部了
                if (getChildAt(getLastVisiblePosition()
                        - getFirstVisiblePosition()) != null
                        && getChildAt(
                        getLastVisiblePosition()
                                - getFirstVisiblePosition())
                        .getBottom() <= getMeasuredHeight())
                    return true;
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 设置可以下拉刷新
     */
    public void setPullRefreshEnable(boolean isPullRefresh) {
        mEnablePullRefresh = isPullRefresh;
    }

    /**
     * 是否禁止上拉加载
     *
     * @param isPullLoad
     */
    public void setPullLoadEnable(boolean isPullLoad) {
        mEnablePullLoad = isPullLoad;
    }

    /**
     * 显示暂无数据
     */
    public void showNoData(Activity activity) {
        this.app = (LqgApplication) x.app();
        this.mActivity = activity;

        NoDataAdapter adapter = new NoDataAdapter();
        this.setAdapter(adapter);
    }

    class NoDataAdapter extends BaseAdapter {

        public int getCount() {
            return 1;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            int childCount = PullableListView.this.getChildCount();

            //获得MainActivity底部的菜单的高度
            int menuHeight = (int) app.getResources().getDimension(R.dimen.d55);
            //获得状态栏的高度
            int statusBarHeight = app.mStatusHeight;
            //获得标题栏的高度
            int titleHeight = (int) app.getResources().getDimension(R.dimen.d50);

            //计算显示暂无数据那个item的高度
            if (childCount == 1) {//可能有头显示
                //获得头视图
                View headView = PullableListView.this.getChildAt(0);
                mItemHeight = app.mScreenContentHeight - headView.getHeight();
                //如果不是MainActivity（为了避免FragmentInvest界面有头没有显示标题栏）
                if (mActivity instanceof MainActivity) {
                    mItemHeight = mItemHeight - (int) menuHeight;
                } else {
                    mItemHeight = mItemHeight - titleHeight;
                }
            } else {
                mItemHeight = app.mScreenContentHeight;
                if (mActivity instanceof MainActivity) {
                    mItemHeight = mItemHeight - (int) menuHeight - titleHeight;
                } else {
                    mItemHeight = mItemHeight - titleHeight;
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                int statusBarView = (int) app.getResources().getDimension(R.dimen.d25);
                mItemHeight = mItemHeight - statusBarView;
            } else {
                mItemHeight = mItemHeight - statusBarHeight;
            }

            View view = View.inflate(mActivity, R.layout.no_data, null);
            LinearLayout llNoData = (LinearLayout) view.findViewById(R.id.ll_item_on_data);
            LinearLayout llNoData2 = (LinearLayout) view.findViewById(R.id.ll_noData);
            llNoData2.setVisibility(View.VISIBLE);
            TextView tvNoDataContent = (TextView) view.findViewById(R.id.tv_noDataContent);


            ImageView lmageLmm = (ImageView) view.findViewById(R.id.lmage_lmm);


            if (mActivity instanceof MainActivity) {
                lmageLmm.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        DialogUtil.lmmDialog(mContext);
                    }
                });
            } else if (mActivity instanceof MyInvestmentActivity) {//我的投资界面
                lmageLmm.setOnClickListener(null);
                tvNoDataContent.setText("暂无投资项目");

                //立即投资按钮
                Button btInvestment = (Button) view.findViewById(R.id.bt_investment);
                btInvestment.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.finish();

                        Intent mIntent = new Intent(com.wtjr.lqg.constants.Constant.UPDATE_ACCOUNTDATA);
                        mIntent.putExtra("ShowFragmentLocation", 2);
                        mActivity.sendBroadcast(mIntent, Manifest.permission.receiver_permission);
                    }
                });
                btInvestment.setVisibility(View.VISIBLE);

            } else if (mActivity instanceof WelfareCardVoucherActivity) {   //卡券界面
                tvNoDataContent.setText("你还没有可用的福利卡券哦~");
                tvNoDataContent.setTextColor(getResources().getColor(R.color.FC_3F1E00));
            } else if (mActivity instanceof WelfareCardVoucherFailureActivity) {     //已失效卡券
                tvNoDataContent.setText("无失效卡券记录");
                tvNoDataContent.setTextColor(getResources().getColor(R.color.FC_3F1E00));
            } else {
                lmageLmm.setOnClickListener(null);
            }


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, mItemHeight);
            llNoData.setLayoutParams(params);

            return view;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }
    }

}
