package com.wtjr.lqg.widget;

//请在这里添加您的包名

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;

/**
 * 通用的输入短信的底部线条变化的输入框
 *
 * @author Myf
 */
public class LineEditTextSMS extends LinearLayout implements OnFocusChangeListener, TextWatcher {

    //	private Paint mPaint;
    private CheckBox mCheckBox;
    private boolean mHasFocus;
    private TextView mTextView;
    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;
    private EditText ledt_sms;
    private View v_et_line;

    /**
     * @param context
     * @param attrs
     */
    public LineEditTextSMS(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_line_edit_text_sms, this, true);

        ledt_sms = (EditText) view.findViewById(R.id.ledt_sms);
        v_et_line = view.findViewById(R.id.v_et_line);


        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = ledt_sms.getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            // throw new
            // NullPointerException("You can add drawableRight attribute in XML");
            mClearDrawable = getResources().getDrawable(R.drawable.delete_gray);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        // 设置焦点改变的监听
        ledt_sms.setOnFocusChangeListener(this);
        ledt_sms.addTextChangedListener(this);

        ledt_sms.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (ledt_sms.getCompoundDrawables()[2] != null) {

                        boolean touchable = event.getX() > (ledt_sms.getWidth() - ledt_sms.getTotalPaddingRight())
                                && event.getX() < (ledt_sms.getWidth() - ledt_sms.getPaddingRight());

                        if (touchable) {
                            ledt_sms.setText("");
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
        ledt_sms.setCompoundDrawables(ledt_sms.getCompoundDrawables()[0],
                ledt_sms.getCompoundDrawables()[1], right, ledt_sms.getCompoundDrawables()[3]);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        mHasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(ledt_sms.getText().length() > 0);
            // 设置底部线条的颜色
//			invalidate();
            v_et_line.setBackgroundColor(Color.parseColor("#fa8f21"));
        } else {
            setClearIconVisible(false);
            // 设置底部线条的颜色
//			invalidate();
            v_et_line.setBackgroundColor(Color.parseColor("#e5e5e5"));
        }
    }

    /**
     * 设置要显示或者隐藏的控件是
     *
     * @param checkBox
     */
    public void setCheckBox(CheckBox checkBox) {
        mCheckBox = checkBox;
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
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
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

//	@Override
//	protected void onTextChanged(CharSequence text, int start,
//			int lengthBefore, int lengthAfter) {
//		setTextContent(text);
//		int textLeng = text.length();
//		setClearIconVisible(textLeng > 0);
//		if (textLeng > 0&&mHasFocus) {// 如果大于0,并且得到焦点
//			showOrHide(true);
//		} else {
//			showOrHide(false);
//		}
//	}
}