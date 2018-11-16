package com.wtjr.lqg.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.LqgApplication;
import com.wtjr.lqg.utils.MoneyFormatUtil;

import org.xutils.x;

/**
 * 金额输入框
 */
public class MoneyEditText extends EditText {
    /**
     * 自定义金额输入框变化输入监听
     */
    private OnTextChangeListener onTextChangeListener;

    /**
     * 设置输入框变化输入监听
     *
     * @param onTextChangeListener
     */
    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.onTextChangeListener = onTextChangeListener;
    }


    public interface OnTextChangeListener {
        /**
         * 金额输入框变化输入监听
         *
         * @param text
         */
        void setTextChange(String text);
    }

    /**
     * 文本控件
     */
    private TextView mTextView;
    /**
     * 文本内容
     */
    private String strContext = "";

    private double mMaxMoney = Double.MAX_VALUE;


    public MoneyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }

        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        LqgApplication app = (LqgApplication) x.app();

        setSingleLine();

        setTextColor(Color.parseColor("#000000"));
        setHintTextColor(Color.parseColor("#DDDDDD"));

        //设置文字大小
        float dimension2 = app.getResources().getDimension(R.dimen.s16);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, dimension2);
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        setSelection(text.toString().length());

        format(text.toString(), this);

        String textContent = getText().toString();

        setTextContent(textContent, strContext);

        if (onTextChangeListener != null) {
            if (TextUtils.isEmpty(textContent)) {
                onTextChangeListener.setTextChange("0.00");
            } else {
                onTextChangeListener.setTextChange(textContent);
            }
        }
    }

    /**
     * 获取文本控件,随输入框变化而变化
     *
     * @param textView
     */
    public void setTextView(TextView textView) {
        mTextView = textView;
    }

    /**
     * 充值通道
     */
    public void setTextContext(String strContext) {
        this.strContext = strContext;
    }

    /**
     * 设置文本框输入内容
     *
     * @param content
     */
    private void setTextContent(String content, String strContext) {
        if (mTextView != null) {
            if (TextUtils.isEmpty(content)) {
                if (!TextUtils.isEmpty(strContext)) {
                    mTextView.setText(strContext);
                } else {
                    mTextView.setText("0.00");
                }
            } else {
                mTextView.setText(MoneyFormatUtil.format(content));
            }
        }

    }

    /**
     * 格式化小数的方法
     *
     * @param editText 显示格式化后的EditText
     */
    public void format(String content, EditText editText) {
        if (content.length() == 2) {//判断是否是0几开头的，例如:02---->改成2

            String substring1 = content.subSequence(0, 1).toString();
            String substring2 = content.subSequence(1, 2).toString();

            if (substring1.equals("0")) {
                if (!substring2.equals(".")) {
                    editText.setText(substring2);
                    editText.setSelection(substring2.length());
                    return;
                }
            }
        }


        if (content.toString().contains(".")) {
            if (content.length() - 1 - content.toString().indexOf(".") > 2) {
                content = content.toString().substring(0, content.toString().indexOf(".") + 3);
                editText.setText(content);
                editText.setSelection(content.length());
            }
        }

        if (content.toString().trim().substring(0).equals(".")) {
            content = "0" + content;
            editText.setText(content);
            editText.setSelection(2);
        }

        if (content.toString().startsWith("0") && content.toString().trim().length() > 1) {
            if (!content.toString().substring(1, 2).equals(".")) {
                editText.setText(content.subSequence(0, 1));
                editText.setSelection(1);
                return;
            }
        }

        int length = content.length();

        //获取第一个小数点的位置
        int indexOf = content.indexOf(".");
        if (indexOf != -1) {
            String sub1 = content.substring(indexOf + 1);
            if (sub1.equals(".")) {
                editText.setText(content.substring(0, indexOf + 1));// 为了小数点后面不能再输入点，例如:1..
                editText.setSelection(indexOf + 1);
            }

            if (length > (indexOf + 1)) {
                String sub2 = content.substring(indexOf + 2);
                if (sub2.equals(".")) {
                    editText.setText(content.substring(0, indexOf + 2));// 为了小数点后面一位的后面不能再输入点，例如:1.5.
                    editText.setSelection(indexOf + 2);
                }
            }
        }

        //判断是否超出输入的最大限额
        if (!TextUtils.isEmpty(content)) {
            double money = Double.parseDouble(content);
//            if(mMaxMoney != 0) {//有最大限制金额
            if (money > mMaxMoney) {
                editText.setText(MoneyFormatUtil.format2(mMaxMoney));
            }
//            }
        }
    }


    /**
     * 设置输入的最大限额
     *
     * @param money 金额
     */
    public void setInputMaxMoney(double money) {
        mMaxMoney = money;
    }
}
