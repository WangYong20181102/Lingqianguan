package com.wtjr.lqg.widget;

import android.content.Context;
import android.content.res.TypedArray;
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
 * Created by WangYong on 2017/3/1.
 */

public class LineEditTextPhoneNumber extends LinearLayout implements View.OnFocusChangeListener, TextWatcher {

    /**
     * 输入内容
     */
    private EditText etInputPhone;

    /**
     * 删除按钮
     */
    private ImageView imageDelete;
    private Context context;
    public LineEditTextPhoneNumber(Context context) {
        this(context, null);
    }
    public LineEditTextPhoneNumber(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        getAttrs(attrs);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LineEditTextPhoneNumber);
        String strHint = array.getString(R.styleable.LineEditTextPhoneNumber_letp_hint);
        etInputPhone.setHint(strHint);
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_line_edit_text_phone_number, this);
        etInputPhone = (EditText) view.findViewById(R.id.ledt_input_phone);
        imageDelete = (ImageView) view.findViewById(R.id.image_delete_context);
        initListener();

    }

    private void initListener() {
        etInputPhone.setOnFocusChangeListener(this);
        etInputPhone.addTextChangedListener(this);
        imageDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etInputPhone.setText("");
            }
        });
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            imageDelete.setVisibility(GONE);
        }else if (etInputPhone.getText().length() > 0){
            imageDelete.setVisibility(VISIBLE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (hasFocus()) {
            if (s.length() > 0) {
                imageDelete.setVisibility(VISIBLE);
            } else {
                imageDelete.setVisibility(GONE);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
