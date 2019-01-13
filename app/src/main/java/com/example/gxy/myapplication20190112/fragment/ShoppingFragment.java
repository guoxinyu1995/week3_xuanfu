package com.example.gxy.myapplication20190112.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gxy.myapplication20190112.R;
import com.example.gxy.myapplication20190112.adaper.ShoppingAdaper;
import com.example.gxy.myapplication20190112.api.Apis;
import com.example.gxy.myapplication20190112.bean.ShoppingBean;
import com.example.gxy.myapplication20190112.custom.MyRecycleView;
import com.example.gxy.myapplication20190112.presenter.PresenterImpl;
import com.example.gxy.myapplication20190112.view.Iview;
import com.gavin.com.library.StickyDecoration;
import com.gavin.com.library.listener.GroupListener;
import com.gavin.com.library.listener.OnGroupClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ShoppingFragment extends Fragment implements Iview {

    @BindView(R.id.query)
    CheckBox query;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.jiesuan)
    TextView jiesuan;
    @BindView(R.id.myrecycle)
    MyRecycleView recyclerView;
    Unbinder unbinder;
    private PresenterImpl presenter;
    private ShoppingAdaper shoppingAdaper;
    private List<ShoppingBean.DataBean> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopping_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        presenter = new PresenterImpl(this);
        initView();
        initData();
        super.onViewCreated(view, savedInstanceState);
    }

    private void initData() {
        presenter.requestGet(Apis.URL_BANNER_GET, ShoppingBean.class);
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //创建适配器
        shoppingAdaper = new ShoppingAdaper(getActivity());
        recyclerView.setAdapter(shoppingAdaper);
        shoppingAdaper.setCallBaackShopp(new ShoppingAdaper.CallBaackShopp() {
            @Override
            public void callBack(List<ShoppingBean.DataBean> list) {
                double totalPrice = 0;
                int num = 0;
                int tottalNum = 0;
                for (int a = 0; a < list.size(); a++) {
                    List<ShoppingBean.DataBean.ListBean> list1 = list.get(a).getList();
                    for (int i = 0; i < list1.size(); i++) {
                        tottalNum = tottalNum + list1.get(i).getNum();
                        if (list1.get(i).isCheck()) {
                            totalPrice = totalPrice + (list1.get(i).getPrice() * list1.get(i).getNum());
                            num = num + list1.get(i).getNum();
                        }
                    }
                }
                if (num < tottalNum) {
                    query.setChecked(false);
                } else {
                    query.setChecked(true);
                }
                total.setText("合计:" + totalPrice + "元");
                jiesuan.setText("结算(" + num + ")");
            }
        });
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSellect(query.isChecked());
                shoppingAdaper.notifyDataSetChanged();
            }
        });
        GroupListener groupListener = new GroupListener() {
            @Override
            public String getGroupName(int position) {
                //获取分组名
                return data.get(position).getSellerName();
            }
        };
        StickyDecoration decoration = StickyDecoration.Builder
                .init(groupListener)
                //重置span（使用GridLayoutManager时必须调用）
                //.resetSpan(mRecyclerView, (GridLayoutManager) manager)
                .setGroupTextSize(20)
                .setOnClickListener(new OnGroupClickListener() {
                    @Override
                    public void onClick(int position, int id) {
                        Toast.makeText(getActivity(),data.get(position).getSellerName(),Toast.LENGTH_SHORT).show();
                        recyclerView.smoothScrollToPosition(position);
                    }
                })
                .setGroupHeight(50)
                .build();
        recyclerView.setLayoutManager(layoutManager);
//需要在setLayoutManager()之后调用addItemDecoration()
        recyclerView.addItemDecoration(decoration);
    }

    private void checkSellect(boolean b) {
        double totalPrice = 0;
        int num = 0;
        for (int j = 0; j < data.size(); j++) {
            ShoppingBean.DataBean dataBean = data.get(j);
            dataBean.setCheck(b);
            List<ShoppingBean.DataBean.ListBean> list = data.get(j).getList();
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setCheck(b);
                totalPrice = totalPrice + (list.get(i).getPrice() * list.get(i).getNum());
                num = num + list.get(i).getNum();
            }
        }
        if (b) {
            total.setText("合计:" + totalPrice + "元");
            jiesuan.setText("结算(" + num + ")");
        } else {
            total.setText("合计:0.00元");
            jiesuan.setText("结算(0)");
        }
    }

    @Override
    public void requestData(Object o) {
        if (o instanceof ShoppingBean) {
            ShoppingBean shoppingBean = (ShoppingBean) o;
            if (shoppingBean == null || !shoppingBean.isSuccess()) {
                Toast.makeText(getActivity(), shoppingBean.getMsg(), Toast.LENGTH_SHORT).show();
            } else {
                data = shoppingBean.getData();
                data.remove(0);
                shoppingAdaper.setmData(data);
            }

        }
    }

    @Override
    public void requestFail(Object o) {
        if (o instanceof Exception) {
            Exception e = (Exception) o;
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "请求错误", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
