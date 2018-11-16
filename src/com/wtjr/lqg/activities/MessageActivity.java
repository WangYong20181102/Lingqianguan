package com.wtjr.lqg.activities;

import java.util.ArrayList;
import java.util.List;

import org.xutils.http.RequestParams;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.NewBaseAdapter;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.MessageBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.XCRoundRectImageView;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout;
import com.wtjr.lqg.widget.pullRefresh.PullableListView;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout.OnRefreshListener;

/**
 * 消息
 *
 * @author Myf
 */
public class MessageActivity extends BaseActivity implements OnClickListener,
        OnRefreshListener, HttpRequesListener, OnItemClickListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * json数据
     */
    JSONObject jsonObject;

    private PullToRefreshLayout pullToRefreshLayout;

    private PullableListView pullableListView;

    private MessageBaseAdapter<MessageBase> mAdapter;

    private List<MessageBase> messages = new ArrayList<MessageBase>();
    private RefreshType mRefreshType;
    private int mPageNum = 1;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_message);
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
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);

        pullToRefreshLayout.setOnRefreshListener(this);
        pullableListView.setOnItemClickListener(this);
        httpUtil.setHttpRequesListener(this);
    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        // 设置名字为:消息
        tvTitleName.setText(R.string.message_name);
    }

    @Override
    public void initData() {
        mAdapter = new MessageBaseAdapter<MessageBase>(this, messages);
        pullableListView.setAdapter(mAdapter);

        sendMessageRequest(RefreshType.RefreshNoPull);
    }

    public class MessageBaseAdapter<T> extends NewBaseAdapter<T> {

        public MessageBaseAdapter(Context context, List<T> list) {
            super(context, list);
        }

        @Override
        public int getContentView() {
            return R.layout.item_message;
        }

        @Override
        public void onInitView(View view, int position) {
            MessageBase message = (MessageBase) getItem(position);

            TextView tvItemTitle = getViewChild(view, R.id.tv_item_title);
            tvItemTitle.setText(message.newsTitle);

            TextView tvItemDate = getViewChild(view, R.id.tv_item_date);
            tvItemDate.setText(TimeUtil.getFullTime(message.newsTime, "MM-dd"));

            TextView tvItemContent = getViewChild(view, R.id.tv_item_content);
            tvItemContent.setText(message.newContent);

            XCRoundRectImageView ivIcon = getViewChild(view, R.id.iv_icon);
            app.setOptions(R.drawable.ic_load_hard);
            app.setDisplayImage(Constant.IMAGE_ADDRESS + message.newsImage, ivIcon);
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
     * 发送消息请求
     */
    public void sendMessageRequest(RefreshType refreshType) {
        mRefreshType = refreshType;
        RequestParams params = new RequestParams(Constant.SETTINGUP_MSGPAGE);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("pageNum", mPageNum + "");
        params.addBodyParameter("pageSize", "6");
        httpUtil.sendRequest(params, refreshType, this);
    }

    @Override
    public void onRefresh() {
        mPageNum = 1;
        sendMessageRequest(RefreshType.RefreshPull);
    }

    @Override
    public void onLoadMore() {
        mPageNum++;
        sendMessageRequest(RefreshType.RefreshPull);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        if (mRefreshType == RefreshType.RefreshPull) {
            if (mPageNum == 1) {
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                if (messages.size() > 0) {
                    pullableListView.setPullLoadEnable(true);
                } else {
                    pullableListView.setPullLoadEnable(false);
                }
            } else {
                mPageNum--;
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }

        noNetAndData(this, errorContent, messages);
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        if (mRefreshType == RefreshType.RefreshPull) {
            if (mPageNum == 1) {
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            } else {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }

        if (mPageNum == 1) {
            messages.clear();
        }

        jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        String msgpage = jsonObject.getString("msgpage");

        messages.addAll(JSON.parseArray(msgpage, MessageBase.class));

        if (mAdapter == null) {
            mAdapter = new MessageBaseAdapter<MessageBase>(this, messages);
            pullableListView.setAdapter(mAdapter);
        } else {
            if (mPageNum == 1) {
                pullableListView.setAdapter(mAdapter);
            }
            mAdapter.notifyDataSetChanged();
        }

        if (mPageNum == 1 && messages.size() == 0) {//禁止上拉
            pullableListView.setPullLoadEnable(false);
        }

        noNetAndData(this, jsonBase, messages);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        MessageBase message = messages.get(arg2);

        if (TextUtils.isEmpty(message.newsUrl)) return;

        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(MessageBase.class.getName(), message);
        startActivity(intent);
    }
}
