package com.wtjr.lqg.widget;

import com.wtjr.lqg.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
/**
 * 复选按钮用来显示或者隐藏密码的
 * @author Myf
 *
 */
public class CheckBoxShowOrHide extends CheckBox implements OnCheckedChangeListener{
	/**
	 * 文本输入框
	 */
	private EditText mEditText;
	/**
	 * 显示或者隐藏，默认是隐藏的
	 */
	private boolean mShowOrHide;

	public CheckBoxShowOrHide(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnCheckedChangeListener(this);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CheckBoxShowOrHide);
		mShowOrHide = a.getBoolean(R.styleable.CheckBoxShowOrHide_cbs_show_or_hide, false);
		a.recycle();
	}

	/**
	 * 得到输入框控件
	 * 
	 * @param editText
	 */
	public void setEditText(EditText editText) {
		mEditText = editText;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			if (mShowOrHide) {
				setText("隐藏密码");
			}
			//密码设置为明文
			mEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			//光标移动到内容最后
			mEditText.setSelection(mEditText.getText().toString().length());
		}else{
			if (mShowOrHide) {
				setText("显示密码");
			}
			//密码设置为密文
			mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
			//光标移动到内容最后
			mEditText.setSelection(mEditText.getText().toString().length());
		}
	}

}
