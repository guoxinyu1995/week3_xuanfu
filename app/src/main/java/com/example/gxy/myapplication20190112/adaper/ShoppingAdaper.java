package com.example.gxy.myapplication20190112.adaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.gxy.myapplication20190112.R;
import com.example.gxy.myapplication20190112.bean.ShoppingBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ShoppingBean.DataBean> mData;
    private Context mContext;

    public ShoppingAdaper(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }
    public void setmData(List<ShoppingBean.DataBean> datas){
        mData.clear();
        if(datas!=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.shopping_fragment_item, viewGroup, false);
        ViewHolderTitla holderTitla = new ViewHolderTitla(view);
        return holderTitla;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final ViewHolderTitla holderTitla = (ViewHolderTitla) viewHolder;
        holderTitla.textTital.setText(mData.get(i).getSellerName());
        //创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        holderTitla.childRecycle.setLayoutManager(layoutManager);
        //创建适配器
        final ShoppingAdaperChild adaperChild = new ShoppingAdaperChild(mContext);
        holderTitla.childRecycle.setAdapter(adaperChild);
        adaperChild.setmList(mData.get(i).getList());
        holderTitla.checkbox.setChecked(mData.get(i).isCheck());
        adaperChild.setCallBackChild(new ShoppingAdaperChild.CallBackChild() {
            @Override
            public void callBaack() {
                //从商品里回调
                if(callBaackShopp!=null){
                    callBaackShopp.callBack(mData);
                }
                List<ShoppingBean.DataBean.ListBean> list = mData.get(i).getList();
                //创建一个临时标志位
                boolean isAllCheck = true;
                for(ShoppingBean.DataBean.ListBean bean :list){
                    if(!bean.isCheck()){
                        isAllCheck = false;
                        break;
                    }
                }
                //刷新商家
                holderTitla.checkbox.setChecked(isAllCheck);
                mData.get(i).setCheck(isAllCheck);
            }
        });
        //商家checkedbox的监听
        //改变旗下所有子商品的选中状态
        holderTitla.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先改变自己的标志位
                mData.get(i).setCheck(holderTitla.checkbox.isChecked());
                //调用子商品里adaper里的方法
                adaperChild.selectOrRemveAll(holderTitla.checkbox.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolderTitla extends RecyclerView.ViewHolder {
        @BindView(R.id.checkbox)
        CheckBox checkbox;
        @BindView(R.id.text_tital)
        TextView textTital;
        @BindView(R.id.child_recycle)
        RecyclerView childRecycle;
        public ViewHolderTitla(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    //定义接口
    private CallBaackShopp callBaackShopp;
    public void setCallBaackShopp(CallBaackShopp callBaackShopp){
        this.callBaackShopp = callBaackShopp;
    }
    public interface CallBaackShopp{
        void callBack(List<ShoppingBean.DataBean> list);
    }
}
