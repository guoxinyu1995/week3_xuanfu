package com.example.gxy.myapplication20190112.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.gavin.com.library.BaseDecoration;

public class MyRecycleView extends RecyclerView {

    private BaseDecoration mDecoration;

    public MyRecycleView(@NonNull Context context) {
        super(context);
    }

    public MyRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void addItemDecoration(@NonNull ItemDecoration decor) {
        if(decor!=null && decor instanceof BaseDecoration){
            mDecoration = (BaseDecoration) decor;
        }
        super.addItemDecoration(decor);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if(mDecoration!=null){
            switch (e.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mDecoration.onEventDown(e);
                    break;
                case MotionEvent.ACTION_UP:
                    if( mDecoration.onEventUp(e)){
                        return true;
                    }
                    break;
                    default:
                        break;
            }
        }
        return super.onInterceptTouchEvent(e);
    }
}
