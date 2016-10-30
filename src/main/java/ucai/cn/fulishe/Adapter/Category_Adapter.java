package ucai.cn.fulishe.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import ucai.cn.fulishe.Dao.NetDao;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.BundleUtils;
import ucai.cn.fulishe.Utils.ImageLoader;
import ucai.cn.fulishe.Utils.OkHttpUtils;
import ucai.cn.fulishe.activity.Category_child_details;
import ucai.cn.fulishe.activity.MainActivity;
import ucai.cn.fulishe.bean.CategoryChildBean;
import ucai.cn.fulishe.bean.CategoryGroup;

/**
 * Created by Administrator on 2016/10/19.
 */
public class Category_Adapter extends BaseExpandableListAdapter {
    Context mcontext;
    ArrayList<CategoryGroup> mCategory_grouplist;
    ArrayList<ArrayList<CategoryChildBean>> mCategory_child_list = new ArrayList<>();
    ArrayList<Integer> goodsidlist=new ArrayList<>();
    ExpandableListView melv;

    public Category_Adapter(Context mcontext, ArrayList<CategoryGroup> category_grouplist, ExpandableListView elv) {
        this.mcontext = mcontext;
        this.mCategory_grouplist = category_grouplist;
        this.melv = elv;
        for (int i = 0; i < mCategory_grouplist.size(); i++) {
            ArrayList<CategoryChildBean> list = new ArrayList<>();
            mCategory_child_list.add(list);
        }
    }

    @Override
    public int getGroupCount() {
        return mCategory_grouplist.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mCategory_child_list.get(groupPosition).size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return mCategory_grouplist.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mCategory_child_list.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mCategory_grouplist.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Category_group_viewholeder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mcontext, R.layout.category_group, null);
            holder = new Category_group_viewholeder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Category_group_viewholeder) convertView.getTag();
        }
        holder.mposition = groupPosition;
        holder.mtv_category_group.setText(mCategory_grouplist.get(groupPosition).getName());
        ImageLoader.downloadImg(mcontext, holder.miv_category_group, mCategory_grouplist.get(groupPosition).getImageUrl());
        if (isExpanded) {
            holder.miv_category_expanded.setImageResource(R.mipmap.arrow2_up);
        } else {
            holder.miv_category_expanded.setImageResource(R.mipmap.arrow2_down);
        }
        return convertView;
    }

    private void addlist(ArrayList<CategoryChildBean> list, int id) {
        this.mCategory_child_list.get(id).addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Category_item_viewholeder holder = null;
        CategoryChildBean bean = mCategory_child_list.get(groupPosition).get(childPosition);
        if (convertView == null) {
            convertView = View.inflate(mcontext, R.layout.category_item, null);
            holder = new Category_item_viewholeder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Category_item_viewholeder) convertView.getTag();
        }
        holder.mtv_category_item.setText(bean.getName());
        ImageLoader.downloadImg(mcontext, holder.miv_category_item, bean.getImageUrl());
        holder.mid=childPosition;
        holder.mposition=groupPosition;
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    class Category_item_viewholeder {
        @Bind(R.id.iv_category_item)
        ImageView miv_category_item;
        @Bind(R.id.tv_category_item_title)
        TextView mtv_category_item;
        int mid;
        int mposition;
        Category_item_viewholeder(View view) {
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Category_item_viewholeder holder = (Category_item_viewholeder) v.getTag();
                    BundleUtils.putString("childsname",holder.mtv_category_item.getText().toString());
                    BundleUtils.putInt("childsid",mCategory_child_list.get(mposition).get(mid).getId());
                    BundleUtils.putSerializable("childs",mCategory_child_list.get(mposition));
                    BundleUtils.intent((Activity) mcontext, Category_child_details.class);
                }
            });
        }
    }
    class Category_group_viewholeder {
        @Bind(R.id.iv_category_group)
        ImageView miv_category_group;
        @Bind(R.id.tv_category_title)
        TextView mtv_category_group;
        @Bind(R.id.category_down)
        ImageView miv_category_expanded;
        int mposition;

        Category_group_viewholeder(View view) {
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                       if (!melv.isGroupExpanded(mposition)) {
                           Category_group_viewholeder holder = (Category_group_viewholeder) v.getTag();
                           melv.expandGroup(holder.mposition);
                       if (!goodsidlist.contains(mposition)) {
                               NetDao.downloadCategory((MainActivity) mcontext, mCategory_grouplist.get(mposition).getId() ,new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
                                   @Override
                                   public void onSuccess(CategoryChildBean[] result) {
                                       ArrayList<CategoryChildBean> child = new OkHttpUtils<>(mcontext).array2List(result);
                                       addlist(child, mposition);
                                       goodsidlist.add(mposition);
                                   }

                                   @Override
                                   public void onError(String error) {

                                   }
                               });

                           }
                       }
                    else {
                           melv.collapseGroup(mposition);
                       }
                }
            });
        }
    }

}
