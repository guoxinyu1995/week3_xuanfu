package com.example.gxy.myapplication20190112.adaper;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.gxy.myapplication20190112.R;
import com.example.gxy.myapplication20190112.bean.ShoppingBean;
import com.example.gxy.myapplication20190112.custom.CustomNum;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingAdaperChild extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ShoppingBean.DataBean.ListBean> mList;
    private Context mContext;

    public ShoppingAdaperChild(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }
    public void setmList(List<ShoppingBean.DataBean.ListBean> lists){
        mList.clear();
        if(lists!=null){
            mList.addAll(lists);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.shopping_children_item, viewGroup, false);
        ViewHolderChild holderChild = new ViewHolderChild(view);
        return holderChild;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ViewHolderChild holderChild = (ViewHolderChild) viewHolder;
        String image = mList.get(i).getImages().split("\\|")[0].replace("https","http");
        holderChild.image.setImageURI(Uri.parse(image));
        holderChild.price.setText("￥"+mList.get(i).getPrice());
        holderChild.title.setText(mList.get(i).getTitle());
        //根据我记录的状态，改变勾选
        holderChild.childCheckbox.setChecked(mList.get(i).isCheck());
        //添加商品的选中监听
        holderChild.childCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //优先改变自己的状态
                mList.get(i).setCheck(isChecked);
                //回调
                if(callBackChild!=null){
                    callBackChild.callBaack();
                }
            }
        });
        //为加减器赋值
        holderChild.customNum.setData(this,mList,i);
        holderChild.customNum.setCallBaackCustom(new CustomNum.CallBaackCustom() {
            @Override
            public void callBack() {
                if(callBackChild!=null){
                    callBackChild.callBaack();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolderChild extends RecyclerView.ViewHolder {
        @BindView(R.id.child_checkbox)
        CheckBox childCheckbox;
        @BindView(R.id.image)
        SimpleDraweeView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.customnum)
        CustomNum customNum;
        public ViewHolderChild(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    /**
     * 在子商品中修改选中状态
     * */
    public void selectOrRemveAll(boolean isSelectAll){
        for(ShoppingBean.DataBean.ListBean listBean : mList){
            listBean.setCheck(isSelectAll);
        }
        notifyDataSetChanged();
    }
    //定义接口
    private CallBackChild callBackChild;
    public void setCallBackChild(CallBackChild callBackChild){
        this.callBackChild = callBackChild;
    }
    public interface CallBackChild{
        void callBaack();
    }
}
