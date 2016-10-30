package ucai.cn.fulishe.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.ImageLoader;
import ucai.cn.fulishe.Utils.MFGT;
import ucai.cn.fulishe.activity.Boutiques_category;
import ucai.cn.fulishe.bean.BoutiqueBean;

/**
 * Created by Administrator on 2016/10/18.
 */
public class BoutiquesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mcontext;
    ArrayList<BoutiqueBean> mBoutiqueslist;
    RecyclerView mparent;
    public BoutiquesAdapter(Context context, ArrayList<BoutiqueBean> Boutiqueslist) {
        this.mcontext = context;
        this.mBoutiqueslist = Boutiqueslist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mparent = (RecyclerView) parent;
        RecyclerView.ViewHolder holder = new BoutiquesHolder(LayoutInflater.from(mcontext).inflate(R.layout.boutiques_items, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BoutiquesHolder) holder).mtvBoutiquesName.setText(mBoutiqueslist.get(position).getName());
        ((BoutiquesHolder) holder).mtvBoutiquesTitle.setText(mBoutiqueslist.get(position).getTitle());
        ((BoutiquesHolder) holder).mtvBoutiquesDescription.setText(mBoutiqueslist.get(position).getDescription());
        ImageDownLoad(((BoutiquesHolder) holder),position);
    }

    private void ImageDownLoad(BoutiquesHolder holder, int position) {
     ImageLoader.downloadImg(mcontext, holder.mivBoutiques, mBoutiqueslist.get(position).getImageurl());
    }
    @Override
    public int getItemCount() {
        return mBoutiqueslist == null ? 0 : mBoutiqueslist.size();
    }

    public void initBoutiques(ArrayList<BoutiqueBean> Boutiquelist) {
        this.mBoutiqueslist.clear();
        this.mBoutiqueslist.addAll(Boutiquelist);
        notifyDataSetChanged();
    }
  public    class BoutiquesHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.iv_boutiques)
        ImageView mivBoutiques;
        @Bind(R.id.tv_boutiques_title)
        TextView mtvBoutiquesTitle;
        @Bind(R.id.tv_boutiques_name)
        TextView mtvBoutiquesName;
        @Bind(R.id.tv_boutiques_description)
        TextView mtvBoutiquesDescription;
        @OnClick(R.id.ll_boutiques)
          public void onClick(){
             Intent intent =new Intent(mcontext,Boutiques_category.class);
             intent.putExtra("GoodsId",mBoutiqueslist.get(getAdapterPosition()).getId());
            MFGT.startActivity((Activity) mcontext,intent);
        }

         BoutiquesHolder(View view) {
             super(view);
             ButterKnife.bind(this, view);
             view.setBackgroundColor(Color.GRAY);
        }
    }
}

