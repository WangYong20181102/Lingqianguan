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
 * 底部线条变化的输入框
 *
 * @author Myf
 */
public class LineEditTextPhone extends LinearLayout implements OnFocusChangeListener, TextWatcher {

    // private Paint mPaint;
    private CheckBox mCheckBox;
    private boolean mHasFocus;
    private TextView mTextView;
    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;
    private EditText ledt_input_phone;
    private View v_et_line;

    /**
     * @param context
     * @param attrs
     */
    public LineEditTextPhone(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_line_edit_text_phone, this, true);

        ledt_input_phone = (EditText) view.findViewById(R.id.ledt_input_phone);
        v_et_line = view.findViewById(R.id.v_et_line);

        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = ledt_input_phone.getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            // throw new
            // NullPointerException("You can add drawableRight attribute in XML");
            mClearDrawable = getResources().getDrawable(R.drawable.delete_gray);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        // 设置焦点改变的监听
        ledt_input_phone.setOnFocusChangeListener(this);
        ledt_input_phone.addTextChangedListener(this);

        ledt_input_phone.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (ledt_input_phone.getCompoundDrawables()[2] != null) {

                        boolean touchable = event.getX() > (ledt_input_phone.getWidth() - ledt_input_phone.getTotalPaddingRight()) && event.getX() < (ledt_input_phone.getWidth() - ledt_input_phone.getPaddingRight());

                        if (touchable) {
                            ledt_input_phone.setText("");
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
        ledt_input_phone.setCompoundDrawables(ledt_input_phone.getCompoundDrawables()[0], ledt_input_phone.getCompoundDrawables()[1], right, ledt_input_phone.getCompoundDrawables()[3]);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        mHasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(ledt_input_phone.getText().length() > 0);
            // 设置底部线条的颜色
            // invalidate();
            v_et_line.setBackgroundColor(Color.parseColor("#fa8f21"));
//	    if (!TextUtils.isEmpty(mTextView.getText().toString())) {
//		mTextView.setVisibility(View.VISIBLE);
//	    }
        } else {
            setClearIconVisible(false);
            // 设置底部线条的颜色
            // invalidate();
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

            String insert = "";
            String string = text.toString();

            if (string.length() > 3 && string.length() < 8) {
                insert = getInsert(string, "-", 3);
                mTextView.setText(insert);
            } else if (string.length() > 7) {
                insert = getInsert(string, "-", 3);
                insert = getInsert(insert, "-", 8);
                mTextView.setText(insert);
            } else {
                mTextView.setText(string);
            }

        } else {
            mTextView.setVisibility(View.GONE);
        }
    }

    /**
     * @param 将字符串插入指定的位置 str被插入的字符串 substr要插入的字符串 location 要插入的位置
     */
    public static String getInsert(String str, String substr, int location) {
        String s1 = str;
        String s2 = substr;
        int i = location;
        String insertedStr = s1.substring(0, i) + s2 + s1.substring(i, s1.length());

        return insertedStr;

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

    // @Override
    // protected void onTextChanged(CharSequence text, int start,
    // int lengthBefore, int lengthAfter) {
    // setTextContent(text);
    // int textLeng = text.length();
    // setClearIconVisible(textLeng > 0);
    // if (textLeng > 0&&mHasFocus) {// 如果大于0,并且得到焦点
    // showOrHide(true);
    // } else {
    // showOrHide(false);
    // }
    // }
}