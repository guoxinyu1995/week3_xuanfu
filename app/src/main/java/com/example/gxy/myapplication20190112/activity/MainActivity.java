package com.example.gxy.myapplication20190112.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.gxy.myapplication20190112.R;
import com.example.gxy.myapplication20190112.fragment.MineFragment;
import com.example.gxy.myapplication20190112.fragment.ShoppingFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.shoppingcart)
    RadioButton shoppingcart;
    @BindView(R.id.mine)
    RadioButton mine;
    @BindView(R.id.rediao)
    RadioGroup rediao;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<Fragment> list;
    private ShoppingFragment shoppingFragment;
    private MineFragment mineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
    }

    /**
     * 初始化view
     */
    private void initView() {
        shoppingFragment = new ShoppingFragment();
        mineFragment = new MineFragment();
        list = new ArrayList<>();
        list.add(shoppingFragment);
        list.add(mineFragment);
        //创建适配器
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return list.get(i);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });
        //滑动切换
        getScoll();
        //点击切换
        getClick();
    }
    //点击切换
    private void getClick() {
        rediao.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.shoppingcart:
                        viewpager.setCurrentItem(0);
                        break;
                    case R.id.mine:
                        viewpager.setCurrentItem(1);
                        break;
                        default:
                            break;
                }
            }
        });
    }
    //滑动切换
    private void getScoll() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i){
                    case 0:
                        rediao.check(R.id.shoppingcart);
                        break;
                    case 1:
                        rediao.check(R.id.mine);
                        default:
                            break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}
