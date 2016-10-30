package ucai.cn.fulishe.activity;

import android.app.Application;
import android.content.Context;

import ucai.cn.fulishe.bean.UserBean;

/**
 * Created by Administrator on 2016/10/17.
 */
public class FuliCenterApplication extends Application{
     private static  FuliCenterApplication  Instance;
   public static   String musername;
  private  static UserBean user;
   public  static  int collections_count=0;
    public static boolean  checked=false;
    public static int getCollections_count() {
        return collections_count;
    }

    public static void setCollections_count(int collections_count) {
        FuliCenterApplication.collections_count = collections_count;
    }

    public  UserBean getUser() {
        return user;
    }

    public  void setUser(UserBean user) {
        FuliCenterApplication.user = user;
    }

    public FuliCenterApplication() {
        Instance=this;
    }

    public static FuliCenterApplication getInstance(){
       if (Instance==null)
       {
           synchronized (FuliCenterApplication.class) {
               if (Instance==null) {
                   Instance = new FuliCenterApplication();
               }
           }
       }
        return Instance;
    }

    public  String getUsername() {
        return musername;
    }

    public  void setUsername(String username) {
        this.musername = username;
    }
    public static boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
