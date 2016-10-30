package ucai.cn.fulishe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.fulishe.Dao.NetDao;
import ucai.cn.fulishe.Dao.UserDao;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.BundleUtils;
import ucai.cn.fulishe.Utils.CommonUtils;
import ucai.cn.fulishe.Utils.FileUtils;
import ucai.cn.fulishe.Utils.I;
import ucai.cn.fulishe.Utils.ImageLoader;
import ucai.cn.fulishe.Utils.L;
import ucai.cn.fulishe.Utils.MFGT;
import ucai.cn.fulishe.Utils.MyDialog;
import ucai.cn.fulishe.Utils.OkHttpUtils;
import ucai.cn.fulishe.Utils.OnSetAvatarListener;
import ucai.cn.fulishe.Utils.ResultUtils;
import ucai.cn.fulishe.Utils.SharedPerfenceUtils;
import ucai.cn.fulishe.bean.MessageBean;
import ucai.cn.fulishe.bean.Result;
import ucai.cn.fulishe.bean.UserBean;
import ucai.cn.fulishe.fragment.Personal;

public class Personal_center extends AppCompatActivity {

    @Bind(R.id.personal_center_iv_logo)
    ImageView personalCenterIvLogo;
    @Bind(R.id.iv_personal_center_back_logo1)
    ImageView ivPersonalCenterBackLogo1;
    @Bind(R.id.personal_center_username)
    TextView personalCenterUsername;
    @Bind(R.id.tv_personal_center_back_1)
    ImageView tvPersonalCenterBack1;
    @Bind(R.id.personal_center_nickname)
    TextView personalCenterNickname;
    @Bind(R.id.tv_personal_center_back_2)
    ImageView tvPersonalCenterBack2;
    @Bind(R.id.personal_center_quit)
    Button personalCenterQuit;
    UserBean user;
    @Bind(R.id.back_personal_center)
    ImageView backPersonalCenter;
    OnSetAvatarListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        ButterKnife.bind(this);

        initdata();

    }

    private void initdata() {
        Intent intent = getIntent();
        user = (UserBean) intent.getSerializableExtra("user");
        personalCenterNickname.setText(user.getMuserNick());
        personalCenterUsername.setText(user.getMuserName());
        ImageLoader.downloadImg(user, Personal_center.this, personalCenterIvLogo, user.getMavatarPath());
    }

    @OnClick({R.id.back_personal_center,R.id.iv_personal_center_back_logo1, R.id.tv_personal_center_back_1, R.id.tv_personal_center_back_2, R.id.personal_center_quit})
    public void onClick(View view) {
        final MyDialog dialog = new MyDialog(Personal_center.this);
        switch (view.getId()) {
            case R.id.iv_personal_center_back_logo1:
                 listener=new OnSetAvatarListener(Personal_center.this,R.id.center_personal,user.getMuserName(),"user_avatar");
                break;
            case R.id.tv_personal_center_back_1:
//不可修改用户名
                break;
            case R.id.tv_personal_center_back_2:
                final EditText editText = dialog.getEdDialog();
                dialog.SetOnPositiveClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText.getText().equals(user.getMuserNick()))
                        {
                            editText.setError("和原来的名字一样");
                            return;
                        }
                        else if (editText.getText().equals("")||editText.getText()==null)
                        {
                            editText.setError("不可为空！");
                            return;
                        }else {
                            NetDao.updateNickname(Personal_center.this, user.getMuserName(), editText.getText().toString(), new OkHttpUtils.OnCompleteListener<Result>() {
                                @Override
                                public void onSuccess(Result result) {
                                    if (result.getRetCode() == 0) {
                                        UserBean user = new Gson().fromJson(result.getRetData().toString(), UserBean.class);
                                        UserDao dao = new UserDao(Personal_center.this);
                                        boolean isSuccess = dao.saveUsers(user);
                                        if (isSuccess) {
                                            SharedPerfenceUtils.getInstance(Personal_center.this).saveuser(user.getMuserName());
                                            FuliCenterApplication.getInstance().setUser(user);
                                            FuliCenterApplication.getInstance().setUsername(user.getMuserName());
                                            BundleUtils.putInt("id", 4);
                                            BundleUtils.putSerializable("user", user);
                                            BundleUtils.intent(Personal_center.this, MainActivity.class);
                                            dialog.dismiss();
                                            refreshuser(user,personalCenterIvLogo,Personal_center.this);
                                        }
                                    } else {
                                        CommonUtils.showShortToast("修改失败");
                                    }
                                }

                                @Override
                                public void onError(String error) {

                                }
                            });

                        }
                    }
                });
                dialog.SetOnNegativeClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.personal_center_quit:
                FuliCenterApplication.getInstance().setUsername(null);
                SharedPerfenceUtils.getInstance(Personal_center.this).removeUser();
                personalCenterNickname.setText("");
                personalCenterUsername.setText("");
                personalCenterIvLogo.setImageResource(R.drawable.contactlogo);
                MFGT.startActivity(Personal_center.this, personal_loginin.class);
                break;
            case R.id.back_personal_center:
                this.finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     if (resultCode!=RESULT_OK)
     {
        return;
     }
        listener.setAvatar(requestCode,data,personalCenterIvLogo);
        if (requestCode==OnSetAvatarListener.REQUEST_CROP_PHOTO) {
            updateAvatar();
        }
    }

    private void updateAvatar() {
        File file=new File(OnSetAvatarListener.getAvatarPath(Personal_center.this
                ,user.getMavatarPath()+"/"+user.getMuserName()+I.AVATAR_SUFFIX_JPG));
        NetDao.updateAvatar(Personal_center.this,user.getMuserName(),file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String string) {
                Result result=ResultUtils.getResultFromJson(string,UserBean.class);
                if (result.getRetCode()==0)
                {
                    ImageLoader.release();
                   UserBean user = (UserBean) result.getRetData();

                    CommonUtils.showShortToast("上传成功");
                    refreshuser(user,personalCenterIvLogo,Personal_center.this);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void refreshuser(UserBean user, ImageView image, Context context) {
        if(user!=null) {
            SharedPerfenceUtils.getInstance(context).removeUser();
            ImageLoader.downloadImg(user, context, image, user.getMavatarPath());
            FuliCenterApplication.getInstance().setUser(user);
            FuliCenterApplication.getInstance().setUsername(user.getMuserName());
            SharedPerfenceUtils.getInstance(context).saveuser(user.getMuserName());
            NetDao.findCollectCount(context, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result.isSuccess()) {
                        FuliCenterApplication.setCollections_count(Integer.parseInt(result.getMsg()));
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }
        else {
            return;
        }
    }


}
