package ucai.cn.fulishe.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.fulishe.Dao.NetDao;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.BundleUtils;
import ucai.cn.fulishe.Utils.ImageLoader;
import ucai.cn.fulishe.Utils.OkHttpUtils;
import ucai.cn.fulishe.activity.FuliCenterApplication;
import ucai.cn.fulishe.activity.MainActivity;
import ucai.cn.fulishe.activity.Mycollections;
import ucai.cn.fulishe.activity.Personal_center;
import ucai.cn.fulishe.activity.personal_loginin;
import ucai.cn.fulishe.bean.MessageBean;
import ucai.cn.fulishe.bean.UserBean;

/**
 * A simple {@link Fragment} subclass.
 */
public class Personal extends Fragment {

    @Bind(R.id.tv_personal_settings)
    TextView tvPersonalSettings;
    @Bind(R.id.iv_personal_message)
    ImageView ivPersonalMessage;
    @Bind(R.id.iv_personal_avatar)
    ImageView ivPersonalAvatar;
    @Bind(R.id.tv_frg_personal_userName)
    TextView tvFrgPersonalUserName;
    @Bind(R.id.tv_personal_countOfCollections)
    TextView tvPersonalCountOfCollections;
    @Bind(R.id.tv_personal_countOfShops)
    TextView tvPersonalCountOfShops;
    @Bind(R.id.tv_personal_MyFoot)
    TextView tvPersonalMyFoot;
    @Bind(R.id.tv_personal_whatHaveBought)
    TextView tvPersonalWhatHaveBought;
    @Bind(R.id.rb_personal_toPay)
    RadioButton rbPersonalToPay;
    @Bind(R.id.rb_personal_toSend)
    RadioButton rbPersonalToSend;
    @Bind(R.id.rb_personal_toReceive)
    RadioButton rbPersonalToReceive;
    @Bind(R.id.rb_personal_toEvaluate)
    RadioButton rbPersonalToEvaluate;
    @Bind(R.id.rb_personal_refundAndAfterSales)
    RadioButton rbPersonalRefundAndAfterSales;
    @Bind(R.id.tv_personal_myCardBag)
    TextView tvPersonalMyCardBag;
    MainActivity context;
    UserBean user;
    @Bind(R.id.mycollections)
    TextView mycollections;

    public Personal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, view);
        context = (MainActivity) getContext();
        initdata();
        return view;
    }

    @Override
    public void onResume() {
        if (FuliCenterApplication.getInstance().getUser() != null) {
            NetDao.findCollectCount(context, FuliCenterApplication.getInstance().getUser().getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result.isSuccess()&&!result.equals("")) {
                        FuliCenterApplication.setCollections_count(Integer.parseInt(result.getMsg()));
                        tvPersonalCountOfCollections.setText(FuliCenterApplication.getCollections_count() + "");
                    }
                    else {
                        tvPersonalCountOfCollections.setText(0 + "");
                    }
                }


                @Override
                public void onError(String error) {

                }
            });
            ImageLoader.downloadImg(user, context, ivPersonalAvatar, user.getMavatarPath());
            tvFrgPersonalUserName.setText(FuliCenterApplication.getInstance().getUser().getMuserNick());
        }
        super.onResume();
    }

    private void initdata() {
        user = FuliCenterApplication.getInstance().getUser();
        new Personal_center().refreshuser(user, ivPersonalAvatar, context);
        if (user == null) {
            tvFrgPersonalUserName.setText(FuliCenterApplication.getInstance().getUsername());
            Intent intent = new Intent(context, personal_loginin.class);
            tvPersonalCountOfCollections.setText(FuliCenterApplication.getCollections_count()+"");
            startActivity(intent);
        } else {
            tvFrgPersonalUserName.setText(FuliCenterApplication.getInstance().getUser().getMuserNick());
            NetDao.findCollectCount(context, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result.isSuccess()) {
                        FuliCenterApplication.setCollections_count(Integer.parseInt(result.getMsg()));
                        tvPersonalCountOfCollections.setText(FuliCenterApplication.getCollections_count()+"");
                    }
                    else if (result.getMsg().equals("0")){
                        tvPersonalCountOfCollections.setText(0+"");
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
            ImageLoader.downloadImg(user, context, ivPersonalAvatar, user.getMavatarPath());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.mycollections,R.id.tv_personal_whatHaveBought, R.id.tv_personal_settings, R.id.rb_personal_toPay, R.id.rb_personal_toSend, R.id.rb_personal_toReceive, R.id.rb_personal_toEvaluate, R.id.rb_personal_refundAndAfterSales, R.id.tv_personal_myCardBag})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_personal_settings:
                BundleUtils.putSerializable("user", user);
                BundleUtils.intent(context, Personal_center.class);
                break;
            case R.id.rb_personal_toPay:
                break;
            case R.id.rb_personal_toSend:
                break;
            case R.id.rb_personal_toReceive:
                break;
            case R.id.rb_personal_toEvaluate:
                break;
            case R.id.rb_personal_refundAndAfterSales:
                break;
            case R.id.tv_personal_myCardBag:
                break;
            case R.id.mycollections:
                BundleUtils.putSerializable("user", user);
                BundleUtils.intent(context, Mycollections.class);
                break;
            case R.id.tv_personal_whatHaveBought:
                break;
        }
    }

}
