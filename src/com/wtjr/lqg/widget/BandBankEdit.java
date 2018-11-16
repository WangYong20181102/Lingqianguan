package com.wtjr.lqg.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;

public class BandBankEdit extends LinearLayout {
	/**
	 * 左侧内容
	 */
	private TextView tvLeft;
	/**
	 * 右边的输入框
	 */
	private ClearEditText edtRight;

	public BandBankEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		initData(context, attrs);
	}

	/**
	 * 初始化控件
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.widget_band_bank_edit, this, true);
		tvLeft = (TextView) view.findViewById(R.id.tv_left);
		edtRight = (ClearEditText) view.findViewById(R.id.edt_right);
	}

	/**
	 * 初始化数据
	 * 
	 * @param context
	 * @param attrs
	 */
	private void initData(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BandBankEdit);
		//左侧文字
		CharSequence tvLeft = a.getText(R.styleable.BandBankEdit_tvLeft); 
		
		//设置最大输入长度
		int maxLength = a.getInt(R.styleable.BandBankEdit_maxLength,50);
        //输入框是否可以点击
        boolean bFocusable = a.getBoolean(R.styleable.BandBankEdit_focusable,true);
        boolean bFocusableInTouchMode = a.getBoolean(R.styleable.BandBankEdit_edt_focusableInTouchMode,true);
 		edtRight.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
		edtRight.setFocusable(bFocusable);
        edtRight.setFocusableInTouchMode(bFocusableInTouchMode);
		setTvLeft(tvLeft);
		//右侧提示文字
		CharSequence edtRightHint = a.getText(R.styleable.BandBankEdit_edtRight_hint); 
		setEdtHint(edtRightHint);
		a.recycle();
	}
	
	/**
	 * 设置左侧内容
	 * @param content
	 */
	public void setTvLeft(CharSequence content) {
		if (!TextUtils.isEmpty(content)) {
			tvLeft.setText(content);
		}
	}
	
	/**
	 * 设置提示文字
	 */
	private void setEdtHint(CharSequence content) {
		if (!TextUtils.isEmpty(content)) {
			edtRight.setHint(content);
		}
	}
	
	public void setTextRight(String content) {
	    edtRight.setText(content);
    }
	
	public String getTextRight() {
		return edtRight.getText().toString();
	}
	
	/**
	 * 设置类型只能是数字
	 */
	public void setInputTypeNumber() {
		edtRight.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
	}
	
	/**
	 * 设置类型身份证
	 */
	public void setInputTypeIdCard() {
		edtRight.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
		edtRight.setKeyListener(new NumberKeyListener() {
			
			@Override
			public int getInputType() {
				return android.text.InputType.TYPE_CLASS_TEXT;
			}
			
			@Override
			protected char[] getAcceptedChars() {
				return new char[] { '1', '2', '3', '4', '5', '6', '7', '8','9', '0','x','X'};
			}
		});

	}
}
