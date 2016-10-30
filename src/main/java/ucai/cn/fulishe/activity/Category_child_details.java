package ucai.cn.fulishe.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.fulishe.Adapter.GoodsAdapter;
import ucai.cn.fulishe.Dao.NetDao;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.CommonUtils;
import ucai.cn.fulishe.Utils.ConvertUtils;
import ucai.cn.fulishe.Utils.I;
import ucai.cn.fulishe.Utils.L;
import ucai.cn.fulishe.Utils.OkHttpUtils;
import ucai.cn.fulishe.Views.CatChildFilterButton;
import ucai.cn.fulishe.bean.CategoryChildBean;
import ucai.cn.fulishe.bean.NewGoodsBean;

public class Category_child_details extends AppCompatActivity {

    @Bind(R.id.iv_top_menu_back)
    ImageView mivTopMenuBack;
    @Bind(R.id.category_details_price)
    Button mcategoryDetailsPrice;
    @Bind(R.id.category_details_time)
    Button mcategoryDetailsTime;
    @Bind(R.id.tv_newgoods_refresh)
    TextView mtvNewgoodsRefresh;
    @Bind(R.id.newgoods_rv)
    RecyclerView mRv;
    @Bind(R.id.srl)
    SwipeRefreshLayout msrl;
    GoodsAdapter mAdapter;
    ArrayList<NewGoodsBean> mNewsGoodslist=new ArrayList<>();
    int carid;
    int pageid = 1;
    boolean isClick_time = true;
    boolean isClick_price = true;
    int SORTBY = I.SORT_BY_ADDTIME_ASC;
    @Bind(R.id.btnCatChildFilter)
    CatChildFilterButton mbtnCatChildFilter;
    GridLayoutManager gridlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_details);
        ButterKnife.bind(this);
        initview();
        initdata(I.ACTION_DOWNLOAD,1);
        PageChange();
    }

    private void PageChange() {
        PageUp();
        PageDown();


    }

    private void PageDown() {
        msrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                msrl.setRefreshing(true);
                msrl.setEnabled(true);
                mtvNewgoodsRefresh.setVisibility(View.VISIBLE);
                pageid=1;
                initdata(I.ACTION_PULL_DOWN,pageid);
            }
        });
    }

    private void PageUp() {
        mRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastposition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                 lastposition=gridlayout.findLastVisibleItemPosition();
               if (lastposition>=mAdapter.getItemCount()-1&&newState==RecyclerView.SCROLL_STATE_IDLE&&mAdapter.isMore())
               {
                   pageid++;
                   initdata(I.ACTION_PULL_UP,pageid);
                   mAdapter.setFooter("加载更多...");
               }
                if (newState!=RecyclerView.SCROLL_STATE_DRAGGING)
                {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                 lastposition=gridlayout.findLastVisibleItemPosition();
            }
        });
    }

    private void initdata(final int action,int mpage_id) {
        NetDao.downloadCategory_details_child(this, carid, mpage_id, I.PAGE_SIZE_DEFAULT, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] goodsbean) {
                if (goodsbean != null) {
                    mAdapter.setMore(true);
                    mNewsGoodslist = ConvertUtils.array2List(goodsbean);
                    switch (action) {
                        case I.ACTION_DOWNLOAD:
                            mAdapter.initGoods(mNewsGoodslist);
                            mAdapter.setFooter("加载中。。。");
                            break;
                        case I.ACTION_PULL_DOWN:
                            mAdapter.initGoods(mNewsGoodslist);
                            msrl.setRefreshing(false);
                            mtvNewgoodsRefresh.setVisibility(View.GONE);
                            mAdapter.setFooter("加载中。。。");
                            break;
                        case I.ACTION_PULL_UP:
                            mAdapter.addGoods(mNewsGoodslist);
                            break;

                    }
                }
                if (action == I.ACTION_PULL_UP) {
                    mAdapter.setFooter("没有更多了...");
                }
            }
            @Override
            public void onError(String error) {
                msrl.setRefreshing(false);
                mtvNewgoodsRefresh.setVisibility(View.GONE);
                CommonUtils.showShortToast(error);
            }
        });

    }

    private void initview() {
        Intent intent = getIntent();
        ArrayList<CategoryChildBean> childslist= (ArrayList<CategoryChildBean>) intent.getSerializableExtra("childs");
        mbtnCatChildFilter.setOnCatFilterClickListener(intent.getStringExtra("childsname"),childslist);
        mbtnCatChildFilter.setText(intent.getStringExtra("childsname"));
        carid = intent.getIntExtra("childsid", 0);
        mAdapter = new GoodsAdapter(Category_child_details.this, mNewsGoodslist);
        gridlayout = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        gridlayout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position==mAdapter.getItemCount()-1?2:1;
            }
        });
        mRv.setLayoutManager(gridlayout);
        mRv.setAdapter(mAdapter);
    }

    @OnClick(R.id.iv_top_menu_back)
    public void onClickback() {
        this.finish();
    }

    @OnClick({R.id.category_details_price, R.id.category_details_time})
    public void onClick(View view) {
        Drawable right;
        switch (view.getId()) {
            case R.id.category_details_price:
                if (isClick_price) {
                    SORTBY = I.SORT_BY_PRICE_ASC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_up);
                } else {
                    SORTBY = I.SORT_BY_PRICE_DESC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                isClick_price = !isClick_price;
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                mcategoryDetailsPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
                break;
            case R.id.category_details_time:
                if (isClick_time) {
                    SORTBY = I.SORT_BY_ADDTIME_ASC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_down);
                } else {
                    SORTBY = I.SORT_BY_ADDTIME_DESC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_up);
                }
                isClick_time = !isClick_time;
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                mcategoryDetailsTime.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
                break;
        }
        mAdapter.setSortOf(SORTBY);
    }

}



