package ucai.cn.fulishe.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import ucai.cn.fulishe.Adapter.GoodsAdapter;
import ucai.cn.fulishe.Dao.NetDao;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.CommonUtils;
import ucai.cn.fulishe.Utils.I;
import ucai.cn.fulishe.Utils.L;
import ucai.cn.fulishe.Utils.OkHttpUtils;
import ucai.cn.fulishe.Utils.SpaceItemDecoration;
import ucai.cn.fulishe.activity.MainActivity;
import ucai.cn.fulishe.bean.NewGoodsBean;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoods extends Fragment {
    GridLayoutManager manager;
    GoodsAdapter madapter;
    ArrayList<NewGoodsBean> mgoodslist;
    int mPage_ID = 1;
    int mNewState;
    MainActivity mcontext;
    @Bind(R.id.tv_newgoods_refresh)
    TextView mtvRefresh;
    @Bind(R.id.newgoods_rv)
    RecyclerView mrv;
    @Bind(R.id.srl)
    SwipeRefreshLayout msrl;
    public NewGoods() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, layout);
        initview();
        PAGE_UP();//上拉刷新
        PAGE_DOWN();
        return layout;
    }


    private void initview() {
        mcontext = (MainActivity) getContext();
        manager = new GridLayoutManager(mcontext, 2, LinearLayoutManager.VERTICAL, false);
        mgoodslist = new ArrayList<>();
        madapter = new GoodsAdapter(getContext(), mgoodslist);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == madapter.getItemCount() - 1 ? 2 : 1;
            }
        });//判断最后，网格布局两行合并变一行
        if (mPage_ID == 1) {
            downloadGoods(I.ACTION_DOWNLOAD, mPage_ID);
        }
        mrv.addItemDecoration(new SpaceItemDecoration(12));
        mrv.setAdapter(madapter);
        mrv.setLayoutManager(manager);
    }
    private void PAGE_DOWN() {
        msrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                msrl.setRefreshing(true);
                msrl.setEnabled(true);
                mtvRefresh.setVisibility(View.VISIBLE);
                mPage_ID=1;
                downloadGoods(I.ACTION_PULL_DOWN,mPage_ID);
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

    private void downloadGoods(final int action, int mPage_ID) {
        NetDao.download(mcontext, mPage_ID, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] NewGoodsBean) {
                if (NewGoodsBean != null) {
                    madapter.setMore(true);
                    ArrayList<NewGoodsBean> goodslist = new OkHttpUtils<>(mcontext).array2List(NewGoodsBean);
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
                            mtvRefresh.setVisibility(View.GONE);
                            msrl.setRefreshing(false);
                            break;
                    }
                }
                if (action == I.ACTION_PULL_UP) {
                    madapter.setFooter("没有更多了...");
                }
            }

            @Override
            public void onError(String error)
            {
                msrl.setRefreshing(false);
                mtvRefresh.setVisibility(View.GONE);
                CommonUtils.showShortToast(error);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
