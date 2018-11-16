package com.wtjr.lqg.utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;

public class FloatView{
    private Context context;
    private View b;
    private int lastX, lastY;
    private int x,y;
    private RelativeLayout relativeLayout;
    private OnImageClickListener onImageClickLitener;
    private int screenWidth;
    private int screenHeight;
    public int left;
    public int right;
    public int top;
    public int bottom;


    public interface OnImageClickListener{
        void isBoolean(boolean b);
    }

    public void setOnImageClickLitener(OnImageClickListener onImageClickLitener) {
        this.onImageClickLitener = onImageClickLitener;
    }

    public FloatView(Context context, View view, RelativeLayout rlAvtivityJump) {
        this.context = context;
        this.b = view;
        this.relativeLayout = rlAvtivityJump;
        init();
    }

    private void init() {

        b.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                relativeLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        screenWidth = relativeLayout.getWidth();
                        screenHeight = relativeLayout.getHeight() - MainActivity.footBottomDistance();  //减去底部banner高度
                    }
                });
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        /*getX()相对于父布局的x坐标，getRawX()相对于屏幕的x坐标*/
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        x = lastX;
                        y = lastY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;

                        left = v.getLeft() + dx;
                        top = v.getTop() + dy;
                        right = v.getRight() + dx;
                        bottom = v.getBottom() + dy;

                        if (left < 0) {
                            left = 0;
                            right = left + v.getWidth();
                        }

                        if (right > screenWidth) {
                            right = screenWidth;
                            left = right - v.getWidth();
                        }

                        if (top < 0) {
                            top = 0;
                            bottom = top + v.getHeight();
                        }

                        if (bottom > screenHeight) {
                            bottom = screenHeight;
                            top = bottom - v.getHeight();
                        }

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        layoutParams.leftMargin = left;
                        layoutParams.topMargin = top;
                        v.setLayoutParams(layoutParams);
//                        //保存位置到本地
//                        SharedPreferencesUtil.setPrefInt(context,"left",left);
//                        SharedPreferencesUtil.setPrefInt(context,"top",top);

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(x - lastX) < 2 && Math.abs(y - lastY) < 2) {
                            onImageClickLitener.isBoolean(true);
                        }else{
                            onImageClickLitener.isBoolean(false);
                        }
                        break;
                }
                return false;
            }
        });
    }

}