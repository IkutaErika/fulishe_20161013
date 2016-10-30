package ucai.cn.fulishe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.fulishe.Adapter.GoodsAdapter;
import ucai.cn.fulishe.Dao.NetDao;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.CommonUtils;
import ucai.cn.fulishe.Utils.I;
import ucai.cn.fulishe.Utils.L;
import ucai.cn.fulishe.Utils.OkHttpUtils;
import ucai.cn.fulishe.Utils.SpaceItemDecoration;
import ucai.cn.fulishe.bean.NewGoodsBean;

public class Boutiques_category extends AppCompatActivity {
    GridLayoutManager manager;
    GoodsAdapter madapter;
    ArrayList<NewGoodsBean> mgoodslist;
    int mPage_ID = 1;
    int mNewState;
    @Bind(R.id.tv_boutiques_av_refresh)
    TextView mtvBoutiquesAvRefresh;
    @Bind(R.id.boutiques_av_rv)
    RecyclerView mrv;
    @Bind(R.id.boutiques_av_srl)
    SwipeRefreshLayout mAvSrl;
    int mcat_id;
    @Bind(R.id.iv_top_menu_back)
    ImageView mivTopMenuBack;
    @OnClick(R.id.iv_top_menu_back)
    public void OnClick(){
      this.finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutiques_category);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mcat_id = intent.getIntExtra("GoodsId", 2);
        initview();
        PAGE_UP();//上拉刷新
        PAGE_DOWN();

    }

    private void initview() {
        manager = new GridLayoutManager(Boutiques_category.this, 2, LinearLayoutManager.VERTICAL, false);
        mgoodslist = new ArrayList<>();
        madapter = new GoodsAdapter(Boutiques_category.this, mgoodslist);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == madapter.getItemCount() - 1 ? 2 : 1;
            }
        });//判断最后，网格布局两行合并变一行
        if (mPage_ID == 1) {
            downloadGoods(I.ACTION_DOWNLOAD,mcat_id ,mPage_ID);
        }
        mrv.addItemDecoration(new SpaceItemDecoration(12));
        mrv.setAdapter(madapter);
        mrv.setLayoutManager(manager);
    }

    private void PAGE_DOWN() {
        mAvSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAvSrl.setRefreshing(true);
                mAvSrl.setEnabled(true);
                mtvBoutiquesAvRefresh.setVisibility(View.VISIBLE);
                mPage_ID = 1;
                downloadGoods(I.ACTION_PULL_DOWN,mcat_id, mPage_ID);
            }
        });
    }

    private void PAGE_UP() {
        mrv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastposition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mNewState = newState;
                lastposition = manager.findLastVisibleItemPosition();
                if (lastposition >= madapter.getItemCount() - 1 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        madapter.isMore()) {
                    mPage_ID += 1;
                    L.i(mPage_ID + "");
                    downloadGoods(I.ACTION_PULL_UP,mcat_id, mPage_ID);
                }
                if (newState != RecyclerView.SCROLL_STATE_DRAGGING) {
                    madapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastposition = manager.findLastVisibleItemPosition();
            }
        });
    }

    private void downloadGoods(final int action,int cat_id, int mPage_ID) {
        NetDao.downloadBoutiques(Boutiques_category.this, cat_id,mPage_ID, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] NewGoodsBean) {
                if (NewGoodsBean != null) {
                    madapter.setMore(true);
                    ArrayList<NewGoodsBean> goodslist = new OkHttpUtils<>(Boutiques_category.this).array2List(NewGoodsBean);
                    switch (action) {
                        case I.ACTION_DOWNLOAD:
                            madapter.initGoods(goodslist);
                            madapter.setFooter("加载更多数据...");
                            break;
                        case I.ACTION_PULL_UP:
                            madapter.addGoods(goodslist);
                            break;
                        case I.ACTION_PULL_DOWN:
                            madapter.initGoods(goodslist);
                            madapter.setFooter("加载更多数据...");
                            mtvBoutiquesAvRefresh.setVisibility(View.GONE);
                            mAvSrl.setRefreshing(false);
                            break;
                    }
                }
                if (action == I.ACTION_PULL_UP) {
                    madapter.setFooter("没有更多了...");
                }
            }

            @Override
            public void onError(String error) {
                mAvSrl.setRefreshing(false);
                mtvBoutiquesAvRefresh.setVisibility(View.GONE);
                CommonUtils.showShortToast(error);
                L.i(error);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
