package ucai.cn.fulishe.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ucai.cn.fulishe.Dao.UserDao;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.L;
import ucai.cn.fulishe.Utils.MFGT;
import ucai.cn.fulishe.Utils.SharedPerfenceUtils;
import ucai.cn.fulishe.bean.UserBean;

public class SplashActivity extends AppCompatActivity {

    long sleeptime=2000;//沉睡时间
    SplashActivity mcontext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mcontext=this;
    }
    @Override
    protected void onStart() {
        super.onStart();
        Handler mhandler=new Handler();
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                UserBean userBean=FuliCenterApplication.getInstance().getUser();
                String username=SharedPerfenceUtils.getInstance(mcontext).getUser();
                if (userBean==null&&username!=null)
                {
                    UserDao dao =new UserDao(mcontext);
                    UserBean user= dao.getUsers(username);
                    if (user!=null) {
                        FuliCenterApplication.getInstance().setUsername(user.getMuserName());
                        FuliCenterApplication.getInstance().setUser(user);
                    }
                }
              MFGT.gotoMainActivity(SplashActivity.this);
            }
        },sleeptime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MFGT.finish(SplashActivity.this);
    }
}
