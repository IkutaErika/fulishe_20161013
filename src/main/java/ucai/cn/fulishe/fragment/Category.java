package ucai.cn.fulishe.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import ucai.cn.fulishe.Adapter.Category_Adapter;
import ucai.cn.fulishe.Dao.NetDao;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.ConvertUtils;
import ucai.cn.fulishe.Utils.L;
import ucai.cn.fulishe.Utils.OkHttpUtils;
import ucai.cn.fulishe.activity.MainActivity;
import ucai.cn.fulishe.bean.CategoryChildBean;
import ucai.cn.fulishe.bean.CategoryGroup;

public class Category extends Fragment {
    Category_Adapter mAdapter;
    ArrayList<CategoryGroup> mgrouplist=new ArrayList<>();
    ArrayList<ArrayList<CategoryChildBean>> mchildlist=new ArrayList<>();
    MainActivity mcontext;
    @Bind(R.id.elv_category)
     ExpandableListView melv;

    public Category() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        initdata();
        return view;
    }

    private void initdata() {
        mcontext = (MainActivity) getContext();
        NetDao.downloadCategory(mcontext, new OkHttpUtils.OnCompleteListener<CategoryGroup[]>() {
            @Override
            public void onSuccess(CategoryGroup[] categoryGroup) {
                mgrouplist = ConvertUtils.array2List(categoryGroup);
                mAdapter=new Category_Adapter(mcontext,mgrouplist,melv);
                melv.setAdapter(mAdapter);
            }

            @Override
            public void onError(String error) {
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
