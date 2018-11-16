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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.Manifest;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.NewBaseAdapter;
import com.wtjr.lqg.base.Investment;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.NewBorrow;
import com.wtjr.lqg.base.PlanMasData;
import com.wtjr.lqg.base.RiskMessagePicture;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.InvestmentDetailsType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;

/**
 * 风控信息
 *
 * @author JoLie
 */
public class RiskMessageActivity extends BaseActivity implements OnClickListener, HttpRequesListener, OnItemClickListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 显示风控图片的GridView
     */
    private GridView gvRiskPicture;
    /**
     * 风控图片的宽度
     */
    private int mRiskPictureWidth;
    private RiskGridViewAdapter<RiskMessagePicture> mAdapter;
    private ArrayList<RiskMessagePicture> mRiskMessagePictures = new ArrayList<RiskMessagePicture>();
    /**
     * 判断是新手标（true）还是普通标(false)
     */
    private int borrowState;
    private Investment mInvestment;
    private TextView tvRiskMessageContent;

    private String[] imageUrls; // 存放图片Url，为了传递到查看图片的Activity去
    private InvestmentDetailsType mInvestmentDetailsType;
    private NewBorrow newBorrow;
    /**
     * 周周升
     */
    private PlanMasData planMasData;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_risk_message);

        float dimension1 = getResources().getDimension(R.dimen.d15);
        float dimension2 = getResources().getDimension(R.dimen.d10);

        mRiskPictureWidth = (int) ((app.mScreenWidth - dimension1 * 2 - dimension2 * 2) / 3);

        start();
    }

    @Override
    public void initIntent() {
        borrowState = getIntent().getIntExtra("borrowState", 1);
        if (borrowState == 2) {//新手标过来
            newBorrow = (NewBorrow) getIntent().getSerializableExtra(Investment.class.getName());
        } else if (borrowState == 1) {
            mInvestment = (Investment) getIntent().getSerializableExtra(Investment.class.getName());
        } else if (borrowState == 3) {//周周升
            planMasData = (PlanMasData) getIntent().getSerializableExtra(Investment.class.getName());
        }
        mInvestmentDetailsType = (InvestmentDetailsType) getIntent().getSerializableExtra(InvestmentDetailsType.class.getName());
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);

        gvRiskPicture = (GridView) findViewById(R.id.gv_riskPicture);

        tvRiskMessageContent = (TextView) findViewById(R.id.tv_riskMessageContent);
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        httpUtil.setHttpRequesListener(this);

        gvRiskPicture.setOnItemClickListener(this);
    }

    @Override
    public void setTitle() {
        // 设置名字为:活动
        tvTitleName.setText("风控信息");

    }

    @Override
    public void initData() {
        app.setOptions(R.drawable.risk_iamge_load);

        mAdapter = new RiskGridViewAdapter<RiskMessagePicture>(this, mRiskMessagePictures);
        gvRiskPicture.setAdapter(mAdapter);

        sendRiskMessageRequest(RefreshType.RefreshNoPull);
    }

    public void sendRiskMessageRequest(RefreshType refreshType) {
        RequestParams params = null;
        switch (mInvestmentDetailsType) {
            case LqbInvestment:
                params = new RequestParams(Constant.LQG_QUERYMATCHBORROWRISKINFO);
                break;
            default:
                params = new RequestParams(Constant.INVESTMENT_VIEWRISKCONTROLINFORMATION);
                break;
        }
        params.addBodyParameter("user_userunid", mUid);
        if (mInvestment != null && borrowState == 1) {
            params.addBodyParameter("borrow_id", mInvestment.bId + "");
        }
        if (newBorrow != null && borrowState == 2) {
            params.addBodyParameter("borrow_id", newBorrow.bId + "");
        }
        if (planMasData !=null && borrowState == 3){
            params.addBodyParameter("planId", planMasData.planId + "");
        }
        httpUtil.sendRequest(params, refreshType, this);
    }

    /**
     * 风控信息界面的用GridView的适配器
     *
     * @param <T>
     * @author dell
     */
    public class RiskGridViewAdapter<T> extends NewBaseAdapter<T> {

        public RiskGridViewAdapter(Context context, List<T> list) {
            super(context, list);
        }

        // item布局
        public int getContentView() {
            return R.layout.item_risk_message;
        }

        public void onInitView(View view, int position) {
            // // 获得显示风控图片的控件
            ImageView ivPicture = getViewChild(view, R.id.iv_picture);
            RelativeLayout rlPicture = getViewChild(view, R.id.rl_picture);
            rlPicture.getLayoutParams().width = mRiskPictureWidth;
            rlPicture.getLayoutParams().height = mRiskPictureWidth;
            //
            RiskMessagePicture picture = (RiskMessagePicture) getItem(position);
            // // 将图片显示任务增加到执行池，图片将被显示到ImageView当轮到此ImageView
            app.setDisplayImage(Constant.IMAGE_ADDRESS + picture.dataImagesSmallurl, ivPicture);
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

    @Override
    public void onFailure(String url, String errorContent) {
        // TODO Auto-generated method stub
        if (errorContent.contains("已下架")) {
            DialogUtil.promptDialog(RiskMessageActivity.this, DialogUtil.HEAD_SERVICE, "温馨提示", errorContent, new DialogUtil.OnClickYesListener() {
                @Override
                public void onClickYes() {
                    Intent intent = new Intent(RiskMessageActivity.this, MainActivity.class);
                    startActivity(intent);
                    // 更新前3个界面
                    Intent mIntent = new Intent(Constant.UPDATE_ALL);
                    mIntent.putExtra("ShowFragmentLocation", 2);
                    sendBroadcast(mIntent, Manifest.permission.receiver_permission);
                }
            });
        }

    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());

        String landing_info = jsonObject.getString("landing_info");
        tvRiskMessageContent.setText(landing_info);

        String proved_photos = jsonObject.getString("proved_photos");

        mRiskMessagePictures.clear();
        mRiskMessagePictures.addAll(JSON.parseArray(proved_photos, RiskMessagePicture.class));

        mAdapter.notifyDataSetChanged();

        imageUrls = new String[mRiskMessagePictures.size()];
        for (int i = 0; i < mRiskMessagePictures.size(); i++) {
            imageUrls[i] = Constant.IMAGE_ADDRESS + mRiskMessagePictures.get(i).dataImagesUrl;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startImagePagerActivity(position);
    }

    /**
     * 启动浏览图片的Activity
     *
     * @param position
     */
    private void startImagePagerActivity(int position) {
        Intent intent = new Intent(this, RiskImagePagerActivity.class);
        intent.putExtra("ImageUrls", imageUrls);
        intent.putExtra("ShowPosition", position);
        startActivity(intent);
    }
}
