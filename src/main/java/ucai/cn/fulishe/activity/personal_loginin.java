package ucai.cn.fulishe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.fulishe.Dao.NetDao;
import ucai.cn.fulishe.Dao.UserDao;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.BundleUtils;
import ucai.cn.fulishe.Utils.CommonUtils;
import ucai.cn.fulishe.Utils.L;
import ucai.cn.fulishe.Utils.MD5;
import ucai.cn.fulishe.Utils.OkHttpUtils;
import ucai.cn.fulishe.Utils.ResultUtils;
import ucai.cn.fulishe.Utils.SharedPerfenceUtils;
import ucai.cn.fulishe.bean.MessageBean;
import ucai.cn.fulishe.bean.Result;
import ucai.cn.fulishe.bean.UserBean;

public class personal_loginin extends AppCompatActivity {

    @Bind(R.id.iv_personal_back)
    ImageView ivPersonalBack;
    @Bind(R.id.et_personal_username)
    EditText metPersonalUsername;
    @Bind(R.id.et_personal_password)
    EditText metPersonalPassword;
    @Bind(R.id.btn_personal_login)
    Button btnPersonalLogin;
    @Bind(R.id.btn_personal_request)
    Button btnPersonalRequest;
    String password;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_loginin);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        metPersonalUsername.setText(intent.getStringExtra("username"));
    }

    @OnClick({R.id.btn_personal_login, R.id.btn_personal_request})
    public void onClick(View view) {
        username = metPersonalUsername.getText().toString();
        password = MD5.getMessageDigest(metPersonalPassword.getText().toString());
        switch (view.getId()) {
            case R.id.btn_personal_login:
                if (username == null || username.equals("")) {
                    metPersonalUsername.setError("请输入用户的名字！");
                    return;
                } else if (!username.matches("[A-Za-z0-9]\\w{5,15}")) {
                    metPersonalUsername.setError("请输入包含字母、数字、下划线！");
                    return;
                } else if (password == null || password.equals("")) {
                    metPersonalPassword.setError("请输入密码！");
                    return;
                }

                NetDao.loginin(personal_loginin.this, username, password, new OkHttpUtils.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String string) {
                        Result result= ResultUtils.getResultFromJson(string,UserBean.class);
                        if (result.getRetCode() == 0) {
                            UserBean userBean= (UserBean) result.getRetData();
                            CommonUtils.showShortToast("登录成功！");
                            NetDao.findCollectCount(personal_loginin.this,userBean.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                                @Override
                                public void onSuccess(MessageBean result) {
                                    if (result.isSuccess())
                                    {
                                       FuliCenterApplication.setCollections_count(Integer.parseInt(result.getMsg()));
                                    }
                                }

                                @Override
                                public void onError(String error) {

                                }
                            });
                            UserDao dao = new UserDao(personal_loginin.this);
                            boolean isSuccess = dao.saveUsers(userBean);
                            if (isSuccess) {
                                SharedPerfenceUtils.getInstance(personal_loginin.this).saveuser(userBean.getMuserName());
                                FuliCenterApplication.getInstance().setUser(userBean);
                                FuliCenterApplication.getInstance().setUsername(userBean.getMuserName());
                                BundleUtils.putInt("id", 4);
                                BundleUtils.putSerializable("user", userBean);
                                BundleUtils.intent(personal_loginin.this, MainActivity.class);
                            } else {
                                CommonUtils.showShortToast("登录失败，数据库操作异常问题");
                            }
                        } else {
                            CommonUtils.showShortToast("登录失败！");
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
                break;
            case R.id.btn_personal_request:
                Intent intent = new Intent(this, personal_request.class);
                startActivity(intent);
                break;
        }
    }

    @OnClick(R.id.iv_personal_back)
    public void onClick() {
        this.finish();
        BundleUtils.putInt("log",0);
        BundleUtils.intent(personal_loginin.this,MainActivity.class);
    }
}
