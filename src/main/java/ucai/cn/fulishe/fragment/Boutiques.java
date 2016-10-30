package ucai.cn.fulishe.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import ucai.cn.fulishe.Adapter.BoutiquesAdapter;
import ucai.cn.fulishe.Dao.NetDao;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.OkHttpUtils;
import ucai.cn.fulishe.Utils.SpaceItemDecoration;
import ucai.cn.fulishe.activity.MainActivity;
import ucai.cn.fulishe.bean.BoutiqueBean;

/**
 * A simple {@link Fragment} subclass.
 */
public class Boutiques extends Fragment {

    BoutiquesAdapter boutiquesAdapter;
    MainActivity mcontext;
    @Bind(R.id.boutiques_rv)
    RecyclerView mboutiquesRv;
   LinearLayoutManager manager;
    public Boutiques() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_boutiques, container, false);
        ButterKnife.bind(this, view);
        initview();
        initdata();
        return view;
    }

    private void initdata() {
        NetDao.downloadBoutiques(mcontext, new OkHttpUtils.OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] boutique) {
                ArrayList<BoutiqueBean> boutiques=new OkHttpUtils<>(mcontext).array2List(boutique);
                boutiquesAdapter.initBoutiques(boutiques);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void initview() {
        mcontext = (MainActivity) getContext();
        ArrayList<BoutiqueBean> boutiqueslist = new ArrayList<>();
        manager=new LinearLayoutManager(mcontext);
        boutiquesAdapter = new BoutiquesAdapter(mcontext, boutiqueslist);
        mboutiquesRv.setAdapter(boutiquesAdapter);
        mboutiquesRv.setLayoutManager(manager);
        mboutiquesRv.addItemDecoration(new SpaceItemDecoration(12));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
