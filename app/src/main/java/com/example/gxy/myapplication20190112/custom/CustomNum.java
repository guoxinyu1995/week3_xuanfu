package com.example.gxy.myapplication20190112.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gxy.myapplication20190112.R;
import com.example.gxy.myapplication20190112.adaper.ShoppingAdaper;
import com.example.gxy.myapplication20190112.adaper.ShoppingAdaperChild;
import com.example.gxy.myapplication20190112.bean.ShoppingBean;

import java.util.ArrayList;
import java.util.List;

public class CustomNum extends LinearLayout {


    private ImageView jian;
    private ImageView add;
    private EditText edit;
    private ShoppingAdaperChild shoppingAdaperChild;
    private List<ShoppingBean.DataBean.ListBean> list = new ArrayList<>();
    private int position;
    private int num;

    public CustomNum(Context context) {
        super(context);
        initView(context);
    }

    public CustomNum(Context context,AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    private Context mContext;
    private void initView(Context context) {
        mContext = context;
        View view = View.inflate(context, R.layout.custom_item,null);
        jian = view.findViewById(R.id.jian);
        add = view.findViewById(R.id.add);
        edit = view.findViewById(R.id.num);
        addView(view);
        //点击加号
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                edit.setText(num+"");
                list.get(position).setNum(num);
                if(callBaackCustom!=null){
                    callBaackCustom.callBack();
                }
                shoppingAdaperChild.notifyDataSetChanged();
            }
        });
        //点击减号
        jian.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num>1){
                    num--;
                }else{
                    Toast.makeText(mContext,"不能在减了",Toast.LENGTH_SHORT).show();
                }
                edit.setText(num+"");
                list.get(position).setNum(num);
                if(callBaackCustom!=null){
                    callBaackCustom.callBack();
                }
                shoppingAdaperChild.notifyDataSetChanged();
            }
        });
    }
    //定义方法
    public void setData(ShoppingAdaperChild shoppingAdaperChild, List<ShoppingBean.DataBean.ListBean> list,int i){
        this.list = list;
        this.shoppingAdaperChild = shoppingAdaperChild;
        position = i;
        num = list.get(i).getNum();
        edit.setText(num+"");
    }
    //定义接口
    public CallBaackCustom callBaackCustom;
    public void setCallBaackCustom(CallBaackCustom callBaackCustom){
        this.callBaackCustom = callBaackCustom;
    }
    public interface CallBaackCustom{
        void callBack();
    }
}
