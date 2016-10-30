package ucai.cn.fulishe.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.MFGT;
import ucai.cn.fulishe.fragment.Boutiques;
import ucai.cn.fulishe.fragment.Cart;
import ucai.cn.fulishe.fragment.Category;
import ucai.cn.fulishe.fragment.NewGoods;
import ucai.cn.fulishe.fragment.Personal;


public class MainActivity extends AppCompatActivity {
    NewGoods mnewgoods;
    Category mcategory;
    Boutiques mboutiques;
    Cart mcarts;
    Personal mPersonal;
    ArrayList<Fragment> fragmentList = new ArrayList<>();

    @Bind(R.id.newgoods)
    RadioButton mrb_newgoods;
    @Bind(R.id.boutiques)
    RadioButton mrb_boutiques;
    @Bind(R.id.category)
    RadioButton mrb_category;
    @Bind(R.id.cart)
    RadioButton mrb_cart;
    @Bind(R.id.personal)
    RadioButton mrb_personal;
    @Bind(R.id.viewpager)
    ViewPager mviewpager;
    int INDEX;
    RadioButton[] mRB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initview();
        intentpage();
        initdata();
    }

    private void initdata() {

    }

    private void intentpage() {
        Intent intent=getIntent();
        if (intent.getIntExtra("id",0)==4)
        {
            INDEX=4;
          onCheckedChange(mrb_personal);
        }
        if (intent.getIntExtra("log",1)==0)
        {
            INDEX=0;
          onCheckedChange(mrb_newgoods);
        }
    }

    private void initview() {
        mRB=new RadioButton[5];
        mRB[0] =mrb_newgoods;
        mRB[1] =  mrb_boutiques;
        mRB[2] =  mrb_category;
        mRB[3] = mrb_cart;
        mRB[4] = mrb_personal;
        initfragments();
        pageChanger();
    }

    private void initfragments() {
        fragmentList.add(new NewGoods());//新品
        fragmentList.add(new Boutiques());//分类
        fragmentList.add(new Category());//精品
        fragmentList.add(new Cart());//购物车
        fragmentList.add(new Personal());//个人
        mviewpager.setAdapter(new FragmentPageAdapter(getSupportFragmentManager(), fragmentList));
    }

    private void pageChanger() {
        mviewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        INDEX = 0;
                        break;
                    case 1:
                        INDEX = 1;
                        break;
                    case 2:
                        INDEX = 2;
                        break;
                    case 3:
                        INDEX = 3;
                        break;
                    case 4:
                        INDEX = 4;
                        break;
                }
                checkedRadioButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onCheckedChange(View v) {
        switch (v.getId()) {
            case R.id.newgoods:
                INDEX = 0;
                break;
            case R.id.boutiques:
                INDEX = 1;
                break;
            case R.id.category:
                INDEX = 2;
                break;
            case R.id.cart:
                INDEX = 3;
                break;
            case R.id.personal:
                INDEX = 4;
                if (FuliCenterApplication.getInstance().getUsername()==null)
                {
                    MFGT.startActivity(this,personal_loginin.class);
                }
                break;
        }
        checkedRadioButton();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    class FragmentPageAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> fragments;

        public FragmentPageAdapter(FragmentManager fm, ArrayList<Fragment> fragment) {
            super(fm);
            this.fragments = fragment;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {

            return fragments.get(position);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container, position, object);
            getSupportFragmentManager().beginTransaction().hide(fragments.get(position));
        }
    }

    public void checkedRadioButton() {
        for (int i = 0; i < mRB.length; i++) {
            if (i == INDEX) {
                mviewpager.setCurrentItem(INDEX);
                mRB[i].setChecked(true);
            } else {
                mRB[i].setChecked(false);
            }
        }
    }


}
