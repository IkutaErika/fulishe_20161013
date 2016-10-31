package ucai.cn.fulishe.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import ucai.cn.fulishe.Dao.NetDao;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.BundleUtils;
import ucai.cn.fulishe.Utils.CommonUtils;
import ucai.cn.fulishe.Utils.I;
import ucai.cn.fulishe.Utils.ImageLoader;
import ucai.cn.fulishe.Utils.L;
import ucai.cn.fulishe.Utils.OkHttpUtils;
import ucai.cn.fulishe.activity.FuliCenterApplication;
import ucai.cn.fulishe.activity.NewGoods_Details;
import ucai.cn.fulishe.bean.CartBean;
import ucai.cn.fulishe.bean.GoodsDetailsBean;
import ucai.cn.fulishe.bean.MessageBean;
import ucai.cn.fulishe.fragment.Cart;

/**
 * Created by Administrator on 2016/10/27.
 */
public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mcontext;
    ArrayList<CartBean> mCartlist;
    RecyclerView mparent;
    Map<Integer,Integer> count =new HashMap<>();
    Map<String,Integer> count_pass=new HashMap<>();
    Map<String,Integer> count_kong=new HashMap<>();
    updateCartReceiver receiver;
    public void setReceiver(updateCartReceiver receiver) {
        this.receiver = receiver;
    }

    public CartAdapter() {
    }

    public CartAdapter(Context context, ArrayList<CartBean> Cartlist) {
        this.mcontext = context;
        this.mCartlist = Cartlist;
        this.setReceiver(receiver);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mparent = (RecyclerView) parent;
        RecyclerView.ViewHolder holder = new CartHolder(LayoutInflater.from(mcontext).inflate(R.layout.cart_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final CartBean cartBean=mCartlist.get(position);
        final GoodsDetailsBean goods=cartBean.getGoods();
        if (goods!=null){
            ImageLoader.downloadImg(mcontext,((CartHolder) holder).ivCartItem,goods.getGoodsThumb());
            ((CartHolder) holder).tvCartGoodsname.setText(goods.getGoodsName());
            ((CartHolder) holder).tvCartGoodsprice.setText(goods.getCurrencyPrice());
            ((CartHolder) holder).holder= ((CartHolder) holder);
            count.put(position,cartBean.getCount());
            count_pass.put(goods.getGoodsName(),cartBean.getCount());
            ((CartHolder) holder).tvCartGoodscount.setText(cartBean.getCount()+"");
             cartBean.setChecked(false);
            ((CartHolder) holder).cartItemCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean Checked) {
                    cartBean.setChecked(Checked);
                    if (Checked){
                        count_pass.put(goods.getGoodsName(),cartBean.getCount());
                        receiver.onReceive(count_pass);
                    }
                    else {
                        count_pass.put(goods.getGoodsName(),0);
                        receiver.onReceive(count_pass);
                    }
                }
            });
        }
        }
    private void update(int position) {
        NetDao.updateCartscount(mcontext,mCartlist.get(position).getId(),count.get(position),mCartlist.get(position).isChecked(),new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result.isSuccess()){
                    CommonUtils.showShortToast("修改成功");
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }


    private void detele(int goodsid) {
        NetDao.deletegoods(mcontext,goodsid, new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                      if (result.isSuccess())
                      {
                          CommonUtils.showShortToast("删除成功！");
                      }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void initCarts(ArrayList<CartBean> Cartlist) {
        mCartlist=Cartlist;
        notifyDataSetChanged();
    }
    public void removeCarts(int position) {
        detele(mCartlist.get(position).getId());
        this.mCartlist.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mCartlist == null ? 0 : mCartlist.size();
    }

      class CartHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.cart_item_checkbox)
        CheckBox cartItemCheckbox;
        @Bind(R.id.iv_cart_item)
        ImageView ivCartItem;
        @Bind(R.id.tv_cart_goodsname)
        TextView tvCartGoodsname;
        @Bind(R.id.iv_cart_add)
        ImageView ivCartAdd;
        @Bind(R.id.tv_cart_goodscount)
        TextView tvCartGoodscount;
        @Bind(R.id.iv_cart_minus)
        ImageView ivCartMinus;
        @Bind(R.id.tv_cart_goodsprice)
        TextView tvCartGoodsprice;
          CartHolder holder;
         CartHolder(View view) {
             super(view);
            ButterKnife.bind(this, view);
             view.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     BundleUtils.putInt("goods", mCartlist.get(getAdapterPosition()).getGoodsId());
                     BundleUtils.intent((Activity) mcontext, NewGoods_Details.class);
                 }
             });
             ivCartAdd.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     int counts=count.get(getAdapterPosition());
                         counts+=1;
                         count.put(getAdapterPosition(),counts);
                         count_pass.put(mCartlist.get(getAdapterPosition()).getGoods().getGoodsName(),counts);
                         holder.tvCartGoodscount.setText(counts +"");
                         update(getAdapterPosition());
                     }
             });
             ivCartMinus.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     int position=getAdapterPosition();
                         int counts=count.get(position);
                         counts-=1;
                         count.put(position,counts);
                         count_pass.put(mCartlist.get(getAdapterPosition()).getGoods().getGoodsName(),counts);
                         holder.tvCartGoodscount.setText(counts+"");
                         update(position);
                         if (count.get(position) ==0){
                             removeCarts(position);
                         }
                     }
             });

        }
    }
    public  interface  updateCartReceiver {
         void onReceive(Map<String,Integer> map_count);

    }
}
