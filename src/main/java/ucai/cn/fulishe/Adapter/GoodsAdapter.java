package ucai.cn.fulishe.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.BundleUtils;
import ucai.cn.fulishe.Utils.I;
import ucai.cn.fulishe.Utils.ImageLoader;
import ucai.cn.fulishe.Utils.L;
import ucai.cn.fulishe.Utils.MFGT;
import ucai.cn.fulishe.activity.NewGoods_Details;
import ucai.cn.fulishe.bean.NewGoodsBean;
import ucai.cn.fulishe.fragment.NewGoods;

/**
 * Created by Administrator on 2016/10/18.
 */
    public class GoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        final static int FOOTER = 1;
        final static int GOODS = 2;
        Context mcontext;
        ArrayList<NewGoodsBean> mGoodslist;
        RecyclerView mparent;
        String footer;
        boolean isMore;
        int SortOf=I.SORT_BY_PRICE_ASC;
        public String getFooter() {
            return footer;
        }

        public boolean isMore() {
            return isMore;
        }

        public void setMore(boolean more) {
            isMore = more;
            notifyDataSetChanged();
        }

    public int getSortOf() {
        return SortOf;
    }

    public void setSortOf(int sortOf) {
        this.SortOf = sortOf;
        SortBy();
        notifyDataSetChanged();
    }

    public void setFooter(String footer) {
            this.footer = footer;
            notifyDataSetChanged();
        }

        public GoodsAdapter(Context context, ArrayList<NewGoodsBean> mGoodslist) {
            this.mcontext = context;
            this.mGoodslist = mGoodslist;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            this.mparent = (RecyclerView) parent;
            RecyclerView.ViewHolder holder = null;
            switch (viewType) {
                case FOOTER:
                    holder = new FooterHolder(LayoutInflater.from(mcontext).inflate(R.layout.footer, parent, false));
                    break;
                case GOODS:
                    holder = new GoodsHolder(LayoutInflater.from(mcontext).inflate(R.layout.newgoods_item, parent, false));
                    break;
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position == getItemCount() - 1) {
                ((FooterHolder) holder).mtv_footer.setText(footer);
                return;
            }
            ((GoodsHolder) holder).mtv_goods_name.setText(mGoodslist.get(position).getGoodsName());
            ((GoodsHolder) holder).mtv_goods_price.setText(mGoodslist.get(position).getCurrencyPrice());
            ImageDownLoad((GoodsHolder) holder, position);
        }

        private void ImageDownLoad(GoodsHolder holder, int position) {
            ImageLoader.downloadImg(mcontext, holder.miv_Goods, mGoodslist.get(position).getGoodsThumb());
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return FOOTER;
            }
            return GOODS;
        }

        @Override
        public int getItemCount() {
            return mGoodslist == null ? 0 : mGoodslist.size() + 1;
        }

        public void initGoods(ArrayList<NewGoodsBean> goodslist) {
            this.mGoodslist.clear();
            this.mGoodslist.addAll(goodslist);
            notifyDataSetChanged();
            SortBy();
        }

        public void addGoods(ArrayList<NewGoodsBean> goodslist) {
            this.mGoodslist.addAll(goodslist);
            notifyDataSetChanged();
        }
    class FooterHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_footer)
        TextView mtv_footer;

        public FooterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class GoodsHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_newgoods)
        ImageView miv_Goods;
        @Bind(R.id.tv_name_newgoods)
        TextView mtv_goods_name;
        @Bind(R.id.tv_price_newgoods)
        TextView mtv_goods_price;
        public GoodsHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick(R.id.rl_newgoods_item)
        void mrl_newgoods_item(){
            BundleUtils.putInt("goods",mGoodslist.get(getAdapterPosition()).getGoodsId());
            BundleUtils.intent((Activity) mcontext, NewGoods_Details.class);
        }
    }
  public void  SortBy(){
      Collections.sort(mGoodslist, new Comparator<NewGoodsBean>() {
          @Override
          public int compare(NewGoodsBean lhs, NewGoodsBean rhs) {
              int result=0;
              Pattern p=Pattern.compile("\\D");
              Matcher m1 =p.matcher(lhs.getCurrencyPrice().toString());
              Matcher m2 =p.matcher(rhs.getCurrencyPrice().toString());
              Integer price1=Integer.parseInt( m1.replaceAll("").trim());
              Integer price2=Integer.parseInt( m2.replaceAll("").trim());
              switch (SortOf){

                  case I.SORT_BY_PRICE_ASC:
                      result=price1-price2;
                      break;
                  case I.SORT_BY_ADDTIME_DESC:
                      result=(int) (rhs.getAddTime()-lhs.getAddTime());
                      break;
                  case I.SORT_BY_PRICE_DESC:
                      result=price2-price1;
                      break;
                  case I.SORT_BY_ADDTIME_ASC:
                      result=(int) (lhs.getAddTime()-rhs.getAddTime());
                      break;
              }
              return result;
          }

      });
  }
    }


