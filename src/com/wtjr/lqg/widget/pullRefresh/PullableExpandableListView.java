package com.wtjr.lqg.widget.pullRefresh;

import org.xutils.x;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.LqgApplication;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.utils.L;

public class PullableExpandableListView extends ExpandableListView implements Pullable {

    private boolean mEnablePullRefresh = true;
    private boolean mEnablePullLoad = true;
    private LqgApplication app;
    private Activity mActivity;
    private int mItemHeight;

    public PullableExpandableListView(Context context) {
	super(context);
    }

    public PullableExpandableListView(Context context, AttributeSet attrs) {
	super(context, attrs);
    }

    public PullableExpandableListView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
	if (getChildAt(0) == null) {
	    return false;
	}

	if (mEnablePullRefresh) {
	    if (getCount() == 0) {
		// 没有item的时候也可以下拉刷新
		return true;
	    } else if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0) {
		// 滑到顶部了
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
		if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
			&& getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
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
	L.hintD("==========showNoData=============");

	this.app = (LqgApplication) x.app();
	this.mActivity = activity;

	NoDataAdapter adapter = new NoDataAdapter();
	this.setAdapter(adapter);
    }

    class NoDataAdapter extends BaseExpandableListAdapter {

	@Override
	public int getGroupCount() {
	    // TODO Auto-generated method stub
	    return 1;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
	    // TODO Auto-generated method stub
	    return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
	    // TODO Auto-generated method stub
	    return 1;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
	    // TODO Auto-generated method stub
	    return 1;
	}

	@Override
	public long getGroupId(int groupPosition) {
	    // TODO Auto-generated method stub
	    return 1;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
	    // TODO Auto-generated method stub
	    return 1;
	}

	@Override
	public boolean hasStableIds() {
	    // TODO Auto-generated method stub
	    return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
	    int childCount = PullableExpandableListView.this.getChildCount();

	    // 获得MainActivity底部的菜单的高度
	    int menuHeight = (int) app.getResources().getDimension(R.dimen.d55);
	    // 获得状态栏的高度
	    int statusBarHeight = app.mStatusHeight;
	    // 获得标题栏的高度
	    int titleHeight = (int) app.getResources().getDimension(R.dimen.d50);

	    // 计算显示暂无数据那个item的高度
	    if (childCount == 1) {// 可能有头显示
		// 获得头视图
		View headView = PullableExpandableListView.this.getChildAt(0);
		mItemHeight = app.mScreenContentHeight - headView.getHeight();
		// 如果不是MainActivity（为了避免FragmentInvest界面有头没有显示标题栏）
		if (!(mActivity instanceof MainActivity)) {
		    mItemHeight = mItemHeight - titleHeight;
		} else {
		    mItemHeight = mItemHeight - (int) menuHeight;
		}

	    } else {
		if (!(mActivity instanceof MainActivity)) {
		    mItemHeight = mItemHeight - titleHeight;
		} else {
		    mItemHeight = mItemHeight - (int) menuHeight;
		}

		mItemHeight = app.mScreenContentHeight;
		mItemHeight = mItemHeight - titleHeight;

	    }

	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		mItemHeight = mItemHeight - statusBarHeight;
	    }

	    View view = View.inflate(mActivity, R.layout.no_data, null);
	    LinearLayout llNoData = (LinearLayout) view.findViewById(R.id.ll_item_on_data);
	    LinearLayout llNoData2 = (LinearLayout) view.findViewById(R.id.ll_noData);
	    llNoData2.setVisibility(View.VISIBLE);
	    ImageView lmageLmm = (ImageView) view.findViewById(R.id.lmage_lmm);

	    if (mActivity instanceof MainActivity) {
		lmageLmm.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View v) {
			// DialogUtil.lmmDialog(mContext);
		    }
		});
	    } else {
		lmageLmm.setOnClickListener(null);
	    }

	    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
	    llNoData.setLayoutParams(params);

	    return view;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
	    // TODO Auto-generated method stub
	    return false;
	}

    }

}
