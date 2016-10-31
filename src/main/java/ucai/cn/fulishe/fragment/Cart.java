package ucai.cn.fulishe.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.fulishe.Adapter.CartAdapter;
import ucai.cn.fulishe.Dao.NetDao;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.BundleUtils;
import ucai.cn.fulishe.Utils.L;
import ucai.cn.fulishe.Utils.OkHttpUtils;
import ucai.cn.fulishe.Utils.SpaceItemDecoration;
import ucai.cn.fulishe.activity.Bought;
import ucai.cn.fulishe.activity.FuliCenterApplication;
import ucai.cn.fulishe.activity.MainActivity;
import ucai.cn.fulishe.bean.CartBean;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cart extends Fragment implements CartAdapter.updateCartReceiver {
    CartAdapter madapter;
    ArrayList<CartBean> mcartlist;
    MainActivity context;
    @Bind(R.id.tv_cart_total)
    TextView tvCartTotal;
    @Bind(R.id.tv_cart_off)
    TextView tvCartOff;
    @Bind(R.id.cart_buy)
    Button cartBuy;
    @Bind(R.id.tv_cart_refresh)
    TextView tvCartRefresh;
    @Bind(R.id.cart_rv)
    RecyclerView cartRv;
    @Bind(R.id.cart_srl)
    SwipeRefreshLayout cartSrl;
    LinearLayoutManager manager;
    @Bind(R.id.tv_carttotal_text)
    TextView tvCarttotalText;
    @Bind(R.id.tv_cart_off_text)
    TextView tvCartOffText;
    CartAdapter.updateCartReceiver receiver;
    int prices;
    Map<Integer,String> map_name=new HashMap<>();
    Map<Integer,Integer> map_price=new HashMap<>();
    public Cart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        initview();
        initdata();
        return view;
    }

    private void initdata() {
        downloadcarts();

    }

    private void downloadcarts() {
        NetDao.findcartGoods(context, FuliCenterApplication.getInstance().getUsername(), new OkHttpUtils.OnCompleteListener<CartBean[]>() {
            @Override
            public void onSuccess(CartBean[] result) {
                if (result != null) {
                    ArrayList cartlist = new OkHttpUtils<>(context).array2List(result);
                    mcartlist.addAll(cartlist);
                    madapter.initCarts(cartlist);
                    tvCartRefresh.setVisibility(View.GONE);
                } else {
                    tvCartRefresh.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String error) {
                cartSrl.setRefreshing(false);
                cartSrl.setEnabled(false);
            }
        });
    }

    private void sumprice(Map<String,Integer> map_count) {
        int count = 0;
        int offcount = 0;
        Pattern p = Pattern.compile("\\D");
        if (mcartlist != null && mcartlist.size() != 0) {
            for (int i = 0; i < mcartlist.size(); i++) {
                if (mcartlist.get(i).isChecked()) {
                    Matcher m = p.matcher(mcartlist.get(i).getGoods().getCurrencyPrice().toString());
                    Matcher m2 = p.matcher(mcartlist.get(i).getGoods().getRankPrice().toString());
                    count += Integer.parseInt(m.replaceAll("").trim()) * map_count.get(mcartlist.get(i).getGoods().getGoodsName());
                    offcount += Integer.parseInt(m2.replaceAll("").trim()) * map_count.get(mcartlist.get(i).getGoods().getGoodsName());
                    map_name.put(i,mcartlist.get(i).getGoods().getGoodsName());
                    map_price.put(i,map_count.get(mcartlist.get(i).getGoods().getGoodsName()));
                }
                else {
                    count+=0;
                    offcount+=0;
                }
                tvCartTotal.setText(count + "");
                tvCartOff.setText(count - offcount + "");
                prices=count;

            }
        } else {
            tvCartTotal.setText(0+"");
            tvCartOff.setText(0+"");
        }
    }


    private void initview() {
        context = (MainActivity) getContext();
        mcartlist = new ArrayList<>();
        madapter = new CartAdapter(context, mcartlist);
        madapter.setReceiver(this);
        manager = new LinearLayoutManager(context);
        cartRv.setAdapter(madapter);
        cartRv.setLayoutManager(manager);
        cartRv.addItemDecoration(new SpaceItemDecoration(12));
    }


    @Override
    public void onResume() {
        NetDao.findcartGoods(context, FuliCenterApplication.getInstance().getUsername(), new OkHttpUtils.OnCompleteListener<CartBean[]>() {
            @Override
            public void onSuccess(CartBean[] result) {
                ArrayList<CartBean> cartlist = new OkHttpUtils<>(context).array2List(result);
                mcartlist.addAll(cartlist);
                madapter.initCarts(cartlist);
            }

            @Override
            public void onError(String error) {

            }
        });
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onReceive(Map<String,Integer> map_count) {
        sumprice(map_count);

    }

    @OnClick(R.id.cart_buy)
    public void cartBuy() {
        BundleUtils.putInt("price",prices);
        BundleUtils.intent(context, Bought.class);
    }
}
