package ucai.cn.fulishe.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.fulishe.Dao.NetDao;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.BundleUtils;
import ucai.cn.fulishe.Utils.CommonUtils;
import ucai.cn.fulishe.Utils.ImageLoader;
import ucai.cn.fulishe.Utils.L;
import ucai.cn.fulishe.Utils.OkHttpUtils;
import ucai.cn.fulishe.activity.Mycollections;
import ucai.cn.fulishe.activity.NewGoods_Details;
import ucai.cn.fulishe.activity.Personal_center;
import ucai.cn.fulishe.bean.CollectBean;
import ucai.cn.fulishe.bean.MessageBean;

/**
 * Created by Administrator on 2016/10/26.
 */
public class CollectionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final static int FOOTER = 1;
    final static int GOODS = 2;
    Context mcontext;
    ArrayList<CollectBean> mcollectionslist;
    RecyclerView mparent;
    String footer;
    boolean isMore;
    int goodsid;
    String username;
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

    public void setFooter(String footer) {
        this.footer = footer;
        notifyDataSetChanged();
    }

    public CollectionsAdapter(Context context, ArrayList<CollectBean> mGoodslist) {
        this.mcontext = context;
        this.mcollectionslist = mGoodslist;
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
                holder = new CollectionsHolder(LayoutInflater.from(mcontext).inflate(R.layout.my_collections, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == getItemCount() - 1) {
            ((FooterHolder) holder).mtv_footer.setText(footer);
            return;
        }
        ((CollectionsHolder) holder).tvNameCollects.setText(mcollectionslist.get(position).getGoodsName());
        if (mcollectionslist.size()>0) {
            ((CollectionsHolder) holder).collectDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NetDao.deletecollections(mcontext, mcollectionslist.get(position).getGoodsId(), username, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result.isSuccess()) {
                                CommonUtils.showShortToast("删除成功！");
                                removeGoods(position);
                            } else {
                                CommonUtils.showShortToast("删除收藏失败");
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                }
            });
        }
        username=mcollectionslist.get(position).getUserName();
        ImageDownLoad((CollectionsHolder) holder, position);
    }

    private void ImageDownLoad(CollectionsHolder holder, int position) {
        ImageLoader.downloadImg(mcontext, holder.ivCollects, mcollectionslist.get(position).getGoodsThumb());
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
        return mcollectionslist == null ? 0 : mcollectionslist.size() + 1;
    }

    public void initGoods(ArrayList<CollectBean> goodslist) {
        this.mcollectionslist.clear();
        this.mcollectionslist.addAll(goodslist);
        notifyDataSetChanged();
    }

    public void addGoods(ArrayList<CollectBean> goodslist) {
        this.mcollectionslist.addAll(goodslist);
        notifyDataSetChanged();
    }
    public void removeGoods(int position) {
        this.mcollectionslist.remove(position);
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
     class CollectionsHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_collects)
        ImageView ivCollects;
        @Bind(R.id.collect_delete)
        ImageView collectDelete;
        @Bind(R.id.tv_name_collects)
        TextView tvNameCollects;
         int position;
         CollectionsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
         @OnClick(R.id.rl_collections_item)
         void mrl_newgoods_item() {
             BundleUtils.putInt("goods", mcollectionslist.get(getAdapterPosition()).getGoodsId());
             BundleUtils.intent((Activity) mcontext, NewGoods_Details.class);
         }
    }
}
