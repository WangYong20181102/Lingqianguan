package com.wtjr.lqg.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

/**
 * 根据输入框中的有无内容来激活按钮
 * @author JoLie
 *
 */
public class ButtonEnabledListener implements TextWatcher {
    private EditText[] editTexts;
    private Button button;

    /**
     * 根据输入框中的有无内容来激活按钮
     * @param button 被激活的按钮
     * @param editTexts 所有用于激活的EditText
     */
    public ButtonEnabledListener(Button button, EditText...editTexts) {
        this.editTexts = editTexts;
        this.button = button;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub
    }

    @Override
    public void afterTextChanged(Editable s) {
        String content = null;
        for (int i = 0; i < editTexts.length; i++) {
            content = editTexts[i].getText().toString();
            if (TextUtils.isEmpty(content)) {
                break;
            }
        }

        if (TextUtils.isEmpty(content)) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }
    }
}
