package ucai.cn.fulishe.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.fulishe.Dao.NetDao;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.BundleUtils;
import ucai.cn.fulishe.Utils.CommonUtils;
import ucai.cn.fulishe.Utils.I;
import ucai.cn.fulishe.Utils.L;
import ucai.cn.fulishe.Utils.MD5;
import ucai.cn.fulishe.Utils.OkHttpUtils;
import ucai.cn.fulishe.bean.Result;
import ucai.cn.fulishe.bean.UserBean;

public class personal_request extends AppCompatActivity {

    @Bind(R.id.iv_personal_request_back)
    ImageView mivPersonalRequestBack;
    @Bind(R.id.et_personal_request_username)
    EditText metPersonalRequestUsername;
    @Bind(R.id.et_personal_request_nickname)
    EditText metPersonalRequestNickname;
    @Bind(R.id.et_personal_request_password)
    EditText metPersonalRequestPassword;
    @Bind(R.id.et_personal_request_comfirm_password)
    EditText metPersonalRequestComfirmPassword;
    @Bind(R.id.btn_personal_request_zhuce)
    Button mbtnPersonalRequestZhuce;
    String username;
    String password;
    String confirmpassword;
    String nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_request);
        ButterKnife.bind(this);
        initdata();
    }

    private void initdata() {


    }

    @OnClick(R.id.btn_personal_request_zhuce)
    public void onClick() {
        username=metPersonalRequestUsername.getText().toString();
        password= metPersonalRequestPassword.getText().toString();
        confirmpassword=metPersonalRequestComfirmPassword.getText().toString();
        nickname=metPersonalRequestNickname.getText().toString();
        if (username==null||username.equals("")){
            metPersonalRequestUsername.setError("请输入用户的名字！");
            return;
        }
        else if (!username.matches("[A-Za-z0-9]\\w{5,15}")) {
                metPersonalRequestUsername.setError("请输入包含字母、数字、下划线！");
                return;
            }
        else  if (password==null||password.equals("")){
            metPersonalRequestPassword.setError("请输入密码！");
            return;
        }
      else if (password.length()>16||password.length()<4) {
                metPersonalRequestPassword.setError("请输入3-16位！");
            return;
        }
        else  if (confirmpassword==null||confirmpassword.equals(""))
        {
            metPersonalRequestComfirmPassword.setError("请再次输入密码");
            return;
        }
        else if (nickname==null||nickname.equals("")) {
            metPersonalRequestNickname.setError("请输入昵称");
            return;
        }
        else  if (!confirmpassword.equals(password)) {
            metPersonalRequestComfirmPassword.setError("两次输入的密码不一致");
            return;
        }
        NetDao.register(personal_request.this,username,nickname,MD5.getMessageDigest(password),new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if (result.getRetCode()==0)
                {
                    CommonUtils.showShortToast("注册成功！");
                    BundleUtils.putString("username",username);
                    BundleUtils.intent(personal_request.this,personal_loginin.class);
                }
                else {
                    CommonUtils.showShortToast("注册失败！");
                }
            }

            @Override
            public void onError(String error) {
            }
        });
    }
}
