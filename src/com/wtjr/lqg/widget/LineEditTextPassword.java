package com.wtjr.lqg.widget;

//请在这里添加您的包名
import org.xutils.x;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.LqgApplication;
import com.wtjr.lqg.utils.L;

/**
 * 底部线条变化的输入框
 * 
 * @author Myf
 * 
 */
public class LineEditTextPassword extends LinearLayout implements OnFocusChangeListener, TextWatcher {

    // private Paint mPaint;
    private CheckBoxShowOrHide mCheckBox;
    private boolean mHasFocus;
    private TextView mTextView;
    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;
    private EditText ledt_input_pas;
    private View v_et_line;

    /**
     * @param context
     * @param attrs
     */
    public LineEditTextPassword(Context context, AttributeSet attrs) {
	super(context, attrs);
	init(context);
    }

    private void init(Context context) {
	View view = LayoutInflater.from(context).inflate(R.layout.widget_line_edit_text_pwd, this, true);

	ledt_input_pas = (EditText) view.findViewById(R.id.ledt_input_pas);
	v_et_line = view.findViewById(R.id.v_et_line);

	mCheckBox = (CheckBoxShowOrHide) view.findViewById(R.id.cb_show_hide);
	mCheckBox.setEditText(ledt_input_pas);

	// 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
	mClearDrawable = ledt_input_pas.getCompoundDrawables()[2];
	if (mClearDrawable == null) {
	    // throw new
	    // NullPointerException("You can add drawableRight attribute in XML");
	    mClearDrawable = getResources().getDrawable(R.drawable.delete_gray);
	}
	mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
	// 设置焦点改变的监听
	ledt_input_pas.setOnFocusChangeListener(this);
	ledt_input_pas.addTextChangedListener(this);

	ledt_input_pas.setOnTouchListener(new OnTouchListener() {

	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
		    if (ledt_input_pas.getCompoundDrawables()[2] != null) {

			boolean touchable = event.getX() > (ledt_input_pas.getWidth() - ledt_input_pas.getTotalPaddingRight()) && event.getX() < (ledt_input_pas.getWidth() - ledt_input_pas.getPaddingRight());

			if (touchable) {
			    ledt_input_pas.setText("");
			}
		    }
		}
		return false;
	    }
	});
    }

    // /**
    // * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
    // * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
    // */
    // @Override
    // public boolean onTouchEvent(MotionEvent event) {
    // if (event.getAction() == MotionEvent.ACTION_UP) {
    // if (ledt_input_pas.getCompoundDrawables()[2] != null) {
    //
    // boolean touchable = event.getX() > (getWidth() -
    // ledt_input_pas.getTotalPaddingRight())
    // && (event.getX() < ((getWidth() - getPaddingRight())));
    //
    // if (touchable) {
    // ledt_input_pas.setText("");
    // }
    // }
    // }
    //
    // return super.onTouchEvent(event);
    // }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     * 
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
	Drawable right = visible ? mClearDrawable : null;
	ledt_input_pas.setCompoundDrawables(ledt_input_pas.getCompoundDrawables()[0], ledt_input_pas.getCompoundDrawables()[1], right, ledt_input_pas.getCompoundDrawables()[3]);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
	mHasFocus = hasFocus;
	showOrHide(hasFocus);
	if (hasFocus) {
	    setClearIconVisible(ledt_input_pas.getText().length() > 0);
	    // 设置底部线条的颜色
	    // invalidate();
	    v_et_line.setBackgroundColor(getResources().getColor(R.color.FC_CAN_SELECT));
	    // mCheckBox.setVisibility(View.VISIBLE);
	} else {
	    setClearIconVisible(false);
	    // 设置底部线条的颜色
	    // invalidate();
	    v_et_line.setBackgroundColor(getResources().getColor(R.color.Line_Color));
	    // mCheckBox.setVisibility(View.GONE);
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
     * 设置控件显示还是隐藏
     * 
     * @param isShow
     */
    private void showOrHide(boolean isShow) {
	if (mCheckBox == null) {
	    return;
	}

	String content = ledt_input_pas.getText().toString();
	if (TextUtils.isEmpty(content)) {
	    mCheckBox.setVisibility(View.INVISIBLE);
	    return;
	}

	// 如果得到焦点，并且有内容就显示，反之隐藏
	if (isShow) {
	    mCheckBox.setVisibility(View.VISIBLE);
	} else {
	    mCheckBox.setVisibility(View.INVISIBLE);
	}
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
	    mTextView.setVisibility(View.VISIBLE);
	    mTextView.setText(text);
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
	if (textLeng > 0 && mHasFocus) {// 如果大于0,并且得到焦点
	    showOrHide(true);
	} else {
	    showOrHide(false);
	}
    }

    @Override
    public void afterTextChanged(Editable s) {
	// TODO Auto-generated method stub

    }
}