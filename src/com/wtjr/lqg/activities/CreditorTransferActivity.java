package com.wtjr.lqg.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.NewBaseAdapter;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.utils.HttpUtil;
import com.wtjr.lqg.widget.AboutLQGLayoutItem;

import java.util.ArrayList;
import java.util.List;

import static com.wtjr.lqg.R.id.view;

/**
 * Created by WangYong on 2017/11/20.
 * 债权转让
 */

public class CreditorTransferActivity extends BaseActivity implements View.OnClickListener, HttpUtil.HttpRequesListener, AdapterView.OnItemClickListener {

    /**
     * 债权转让ListView
     */
    private ListView listCreditorTransfer;
    /**
     * 标题
     */
    private TextView tvTitleName;
    /**
     * 返回
     */
    private ImageButton imgBtnBack;
    private List<String> list = new ArrayList<>();
    private CreditorTransferAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditor_transfer_layout);
        start();
    }

    @Override
    public void initData() {
        list.add("《债权转让协议》");
        list.add("《借款合同》");
        list.add("《债权转让协议》");

        adapter = new CreditorTransferAdapter(this, list);
        listCreditorTransfer.setAdapter(adapter);
    }

    @Override
    public void findViewById() {
        listCreditorTransfer = (ListView) findViewById(R.id.lv_creditor_transfer);
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
    }

    @Override
    public void setTitle() {
        //标题
        tvTitleName.setText(R.string.creditor_transfer);
        //返回键
        imgBtnBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void setListener() {
        imgBtnBack.setOnClickListener(this);
        //网络监听
        httpUtil.setHttpRequesListener(this);
        //listView监听
        listCreditorTransfer.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:
                finish();
                break;
        }
    }

    @Override
    public void onFailure(String url, String errorContent) {

    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public class CreditorTransferAdapter extends NewBaseAdapter {

        public CreditorTransferAdapter(Context context, List<String> list) {
            super(context, list);
        }


        @Override
        public int getContentView() {
            return R.layout.creditor_transfer_adapter_item;
        }

        @Override
        public void onInitView(View view, int position) {

            //债权转让item
            AboutLQGLayoutItem tvCreditorTransfer = (AboutLQGLayoutItem) getViewChild(view, R.id.tv_creditor_transfer);
            //隐藏最后一条线
            View viewLine = getViewChild(view, R.id.creditor_transfer_line);
            if (list.size() - 1 == position) {
                viewLine.setVisibility(View.GONE);
            }else {
                viewLine.setVisibility(View.VISIBLE);
            }
            tvCreditorTransfer.setTextContext(list.get(position));

        }
    }
}
