package com.wtjr.lqg.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wtjr.lqg.R;

/**
 * Created by WangYong on 2017/3/20.
 */

public class LineEditTextObtainSMS extends LinearLayout implements TextWatcher, View.OnFocusChangeListener {
    private Context context;

    /**
     * 清除按钮
     */
    private ImageView imageDeleteSMS;

    /**
     * 短信验证码
     */
    private EditText etSMS;
    public LineEditTextObtainSMS(Context context) {
        super(context);
    }

    public LineEditTextObtainSMS(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_line_edit_text_obtain_sms,this);
        etSMS = (EditText) view.findViewById(R.id.ledt_sms);
        imageDeleteSMS = (ImageView) view.findViewById(R.id.image_sms_delete);
        setlistener();

    }

    /**
     * 设置监听事件
     */
    private void setlistener() {
        etSMS.addTextChangedListener(this);
        etSMS.setOnFocusChangeListener(this);
        imageDeleteSMS.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etSMS.setText("");
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (hasFocus()){
            if (s.length() > 0){
                imageDeleteSMS.setVisibility(VISIBLE);
            }else{
                imageDeleteSMS.setVisibility(GONE);
            }
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus){
            imageDeleteSMS.setVisibility(GONE);
        }else if (etSMS.getText().length() > 0){
            imageDeleteSMS.setVisibility(VISIBLE);
        }
    }
}
