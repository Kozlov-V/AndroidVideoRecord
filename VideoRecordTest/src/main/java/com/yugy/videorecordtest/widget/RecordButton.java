package com.yugy.videorecordtest.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yugy.videorecordtest.R;

public class RecordButton extends FrameLayout{

    private ImageView icon;

    public RecordButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initViews();
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initViews();
    }

    public RecordButton(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initViews();
    }

    private void initViews(){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.widget_recordbutton, this);
        icon = (ImageView)findViewById(R.id.recordbutton_icon);
    }

    private boolean brighted = true;

    @SuppressWarnings("deprecation")
    public void setBright(){
        icon.setAlpha(255);
        brighted = true;
    }

    @SuppressWarnings("deprecation")
    public void twinkle(){
        if(brighted){
            icon.setAlpha(128);
        }else{
            icon.setAlpha(255);
        }
        brighted = !brighted;

    }
}