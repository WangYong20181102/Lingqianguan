package com.wtjr.lqg.activities;

import java.util.ArrayList;
import java.util.List;

import org.xutils.http.RequestParams;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.FeedBackAdapter;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.MessageFeedBack;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;

/**
 * 用户反馈
 * 
 * @author Myf
 * 
 */
public class UserFeedBackActivity extends BaseActivity implements
		OnClickListener, HttpRequesListener {
	List<MessageFeedBack> backs = new ArrayList<MessageFeedBack>();
	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	// /**
	// * 记录listView位置
	// */
	// private int iPosition;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;

	private MessageFeedBack messageFeedBack;
	/**
	 * 返回
	 */
	private ImageButton imgbtnBack;
	/**
	 * 发送消息
	 */
	private Button btn_send_message;
	/**
	 * 反馈内容输入框
	 */
	private EditText ed_content;
	/**
	 * 展示反馈信息的列表
	 */
	private ListView listView;
	/**
	 * 信息的列表(listView)的适配器
	 */
	private FeedBackAdapter adapter;
	/**
	 * 用户ID
	 */
	private String user_id;
	/**
	 * 发送反馈信息的对话框
	 */
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_user_feed_back);

		//关闭默认弹出键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		start();
	}

	@Override
	public void findViewById() {

		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);

		listView = (ListView) findViewById(R.id.listView);
		btn_send_message = (Button) findViewById(R.id.btn_send_message);
		ed_content = (EditText) findViewById(R.id.ed_content);

	}

	@Override
	public void setListener() {
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
		btn_send_message.setOnClickListener(this);
		httpUtil.setHttpRequesListener(this);
	}

	@Override
	public void setTitle() {
		// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
		imgBtnBack.setVisibility(View.VISIBLE);
		// 设置名字为:用户反馈
		tvTitleName.setText(R.string.user_feed_back_name);
	}

	@Override
	public void initData() {
		sendHttpReques(RefreshType.RefreshPull);
		refreshHttpData();
		adapter = new FeedBackAdapter(UserFeedBackActivity.this, backs);
		listView.setAdapter(adapter);
		
//		listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
	}

	public void refreshHttpData() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				refreshHttpData();
				sendHttpReques(RefreshType.RefreshPull);
			}
		}, 3000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBtn_back:// 返回按钮点击操作
			// 结束当前的Activity
			finish();
			break;
		case R.id.btn_send_message:// 发送
			String content = ed_content.getText().toString();
			if (content.trim().length() == 0 || TextUtils.isEmpty(content)) {
				DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "输入内容不能为空，请重新输入");
			} else {
				sendHttpReques(content, RefreshType.RefreshPull);
				ed_content.setText("");
				listView.setSelection(backs.size());
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 得到反馈消息
	 */
	public void sendHttpReques(RefreshType refreshType) {

		RequestParams params = new RequestParams(Constant.PAY_MESSAGEFEEDBACK);
		params.addBodyParameter("user_userunid", mUid);
		params.addBodyParameter("userMsg", "");
		httpUtil.sendRequest(params, refreshType, this);

	}

	/**
	 * 发送反馈消息
	 */
	public void sendHttpReques(String str, RefreshType refreshType) {
		RequestParams params = new RequestParams(Constant.PAY_MESSAGEFEEDBACK);
		params.addBodyParameter("user_userunid", mUid);
		params.addBodyParameter("userMsg", str);
		httpUtil.sendRequest(params, refreshType, this);
		if (dialog != null) {
			TextView textView = (TextView) dialog.findViewById(R.id.textView1);
			textView.setText("发送中...");
		}

	}

	@Override
	public void onFailure(String url, String errorContent) {
		// TODO Auto-generated method stub
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	@Override
	public void onSuccess(String url, JSONBase jsonBase) {
		if (dialog != null) {
			dialog.dismiss();
		}

		JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
		if (url.equals(Constant.PAY_MESSAGEFEEDBACK)) {
			backs.clear();
			if (jsonObject.getString("userFeedbackList") == null) {
				sendHttpReques(RefreshType.RefreshPull);
				return;
			}
			// 解析出消息集合
			List<MessageFeedBack> messageFeedBacks = JSON.parseArray(jsonObject.getString("userFeedbackList"),MessageFeedBack.class);
			backs.addAll(messageFeedBacks);
			adapter.notifyDataSetChanged();
		}
	}
}
