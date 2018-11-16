package com.wtjr.lqg.widget;

//请在这里添加您的包名
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;

/**
 * 底部线条变化的输入框
 * 
 * @author Myf
 * 
 */
public class LineEditTextInvitationCode extends LinearLayout implements OnFocusChangeListener, TextWatcher {

    private TextView mTextView;
    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;
    private EditText ledt_input_invatation_code;
    private View v_et_line;

    /**
     * @param context
     * @param attrs
     */
    public LineEditTextInvitationCode(Context context, AttributeSet attrs) {
	super(context, attrs);
	init(context);
    }

    private void init(Context context) {
	View view = LayoutInflater.from(context).inflate(R.layout.widget_line_edit_text_invatation_code, this, true);

	ledt_input_invatation_code = (EditText) view.findViewById(R.id.ledt_input_invatation_code);
	v_et_line = view.findViewById(R.id.v_et_line);

	// 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
	mClearDrawable = ledt_input_invatation_code.getCompoundDrawables()[2];
	if (mClearDrawable == null) {
	    mClearDrawable = getResources().getDrawable(R.drawable.delete_gray);
	}
	mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
	// 设置焦点改变的监听
	ledt_input_invatation_code.setOnFocusChangeListener(this);
	ledt_input_invatation_code.addTextChangedListener(this);

	ledt_input_invatation_code.setOnTouchListener(new OnTouchListener() {

	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
		    if (ledt_input_invatation_code.getCompoundDrawables()[2] != null) {

			boolean touchable = event.getX() > (ledt_input_invatation_code.getWidth() - ledt_input_invatation_code.getTotalPaddingRight()) && event.getX() < (ledt_input_invatation_code.getWidth() - ledt_input_invatation_code.getPaddingRight());

			if (touchable) {
			    ledt_input_invatation_code.setText("");
			}
		    }
		}
		return false;
	    }
	});
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     * 
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
	Drawable right = visible ? mClearDrawable : null;
	ledt_input_invatation_code.setCompoundDrawables(ledt_input_invatation_code.getCompoundDrawables()[0], ledt_input_invatation_code.getCompoundDrawables()[1], right, ledt_input_invatation_code.getCompoundDrawables()[3]);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
	if (hasFocus) {
	    setClearIconVisible(ledt_input_invatation_code.getText().length() > 0);
	    // 设置底部线条的颜色
	    v_et_line.setBackgroundColor(getResources().getColor(R.color.FC_CAN_SELECT));
	    if (mTextView != null) {
		mTextView.setVisibility(View.GONE);
	    }
	} else {
	    setClearIconVisible(false);
	    // 设置底部线条的颜色
	    v_et_line.setBackgroundColor(getResources().getColor(R.color.Line_Color));
	}
    }

    /**
     * 设置要显示的控件是
     * 
     * @param textView
     */
    public void setTextView(TextView textView) {
	mTextView = textView;
    }

    /**
     * 设置内容
     * 
     * @param text
     */
    private void setTextContent(CharSequence text) {
	if (mTextView == null) {
	    return;
	}
	if (!TextUtils.isEmpty(text)) {
	    mTextView.setVisibility(View.GONE);
	} else {
	    mTextView.setVisibility(View.GONE);
	}
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	// TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
	setTextContent(text);
	int textLeng = text.length();
	setClearIconVisible(textLeng > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {
	// TODO Auto-generated method stub

    }
}