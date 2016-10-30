package ucai.cn.fulishe.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

import ucai.cn.fulishe.R;
import ucai.cn.fulishe.activity.MainActivity;
import ucai.cn.fulishe.activity.personal_loginin;
import ucai.cn.fulishe.bean.UserBean;

/**
 * Created by Administrator on 2016/10/22.
 */
public class BundleUtils {
static     Bundle bundle=new Bundle();
    public static void putInt(String id, int i) {

        bundle.putInt(id,i);
    }

    public static void putSerializable(String user, Object userBean) {
        bundle.putSerializable(user, (Serializable) userBean);
    }

    public static void intent(Activity activity, Class<?> clas) {
        Intent intent = new Intent();
        intent.setClass(activity,clas);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void putString(String key, String value) {
        bundle.putString(key,value);
    }
}
