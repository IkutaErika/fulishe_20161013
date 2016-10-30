package ucai.cn.fulishe.activity;

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
import ucai.cn.fulishe.Adapter.CollectionsAdapter;
import ucai.cn.fulishe.Dao.NetDao;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.CommonUtils;
import ucai.cn.fulishe.Utils.I;
import ucai.cn.fulishe.Utils.OkHttpUtils;
import ucai.cn.fulishe.Utils.SpaceItemDecoration;
import ucai.cn.fulishe.bean.CollectBean;
import ucai.cn.fulishe.bean.UserBean;

public class Mycollections extends AppCompatActivity {
    CollectionsAdapter madapter;
    ArrayList<CollectBean> mcollections;
    GridLayoutManager manager;
    int mPage_ID = 1;
    int mNewState;
    Mycollections mcontext;
    @Bind(R.id.iv_top_menu_back)
    ImageView ivTopMenuBack;
    @Bind(R.id.tv_collects_refresh)
    TextView tvCollectsRefresh;
    @Bind(R.id.collects_rv)
    RecyclerView collectsRv;
    @Bind(R.id.collects_srl)
    SwipeRefreshLayout collectsSrl;
    UserBean user;

    public Mycollections() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycollections);
        ButterKnife.bind(this);
        initview();
        PAGE_UP();//上拉刷新
        PAGE_DOWN();

    }

    private void PAGE_DOWN() {
        collectsSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                collectsSrl.setRefreshing(true);
                collectsSrl.setEnabled(true);
                tvCollectsRefresh.setVisibility(View.VISIBLE);
                mPage_ID = 1;
                downloadGoods(I.ACTION_PULL_DOWN, mPage_ID);
            }
        });
    }

    private void PAGE_UP() {
        collectsRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastposition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mNewState = newState;
                lastposition = manager.findLastVisibleItemPosition();
                if (lastposition >= madapter.getItemCount() - 1 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        madapter.isMore()) {
                    mPage_ID += 1;
                    downloadGoods(I.ACTION_PULL_UP, mPage_ID);
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

    private void initview() {
        user = (UserBean) getIntent().getSerializableExtra("user");
        mcontext = Mycollections.this;
        manager = new GridLayoutManager(mcontext, 2, LinearLayoutManager.VERTICAL, false);
        mcollections = new ArrayList<>();
        madapter = new CollectionsAdapter(mcontext, mcollections);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == madapter.getItemCount() - 1 ? 2 : 1;
            }
        });//判断最后，网格布局两行合并变一行
        if (mPage_ID == 1) {
            downloadGoods(I.ACTION_DOWNLOAD, mPage_ID);
        }
        collectsRv.addItemDecoration(new SpaceItemDecoration(12));
        collectsRv.setAdapter(madapter);
        collectsRv.setLayoutManager(manager);
    }

    private void downloadGoods(final int action, int mPage_id) {
        NetDao.downloadcollections(mcontext, user.getMuserName(), mPage_id, new OkHttpUtils.OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] CollectsBean) {
                if (CollectsBean != null && !CollectsBean.equals("")) {
                    madapter.setMore(true);
                    ArrayList<CollectBean> goodslist = new OkHttpUtils<>(mcontext).array2List(CollectsBean);
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
                            tvCollectsRefresh.setVisibility(View.GONE);
                            collectsSrl.setRefreshing(false);
                            break;
                    }
                }
                collectsSrl.setRefreshing(false);
                tvCollectsRefresh.setVisibility(View.GONE);
                if (action == I.ACTION_PULL_UP) {
                    madapter.setFooter("没有更多了...");
                }
            }

            @Override
            public void onError(String error) {
                collectsSrl.setRefreshing(false);
                tvCollectsRefresh.setVisibility(View.GONE);
                CommonUtils.showShortToast(error);
            }
        });
    }

    @Override
    protected void onResume() {
        downloadGoods(I.ACTION_PULL_DOWN, mPage_ID);
        super.onResume();
    }

    @OnClick(R.id.iv_top_menu_back)
    public void onClick() {
        this.finish();
    }
}
