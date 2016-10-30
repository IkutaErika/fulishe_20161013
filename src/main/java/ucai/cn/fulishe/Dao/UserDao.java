package ucai.cn.fulishe.Dao;

import android.content.Context;

import ucai.cn.fulishe.Utils.I;
import ucai.cn.fulishe.bean.UserBean;

/**
 * Created by Administrator on 2016/10/24.
 */
public class UserDao {
    public static final String USER_COLUMN_NAME = "m_user_name";
    public static final String USER_COLUMN_NICK = "m_user_nick";
    public static final String USER_TABLE_NAME ="t_superwechat_user" ;
    public static final String USER_COLUMN_AVATAR_ID ="m_user_avatar_id" ;
    public static final String USER_COLUMN_AVATAR_TYPE ="m_user_avatar_type" ;
    public static final String USER_COLUMN_AVATAR_PATH ="m_user_avatar_path" ;
    public static final String USER_COLUMN_AVATAR_SUFFIX ="m_user_avatar_suffix" ;
    public static final String USER_COLUMN_AVATAR_LASTUPDATE_TIME ="m_user_lastupdate_time" ;
    public UserDao(Context context) {
   DB_Manager.getInstance().onInit(context);
    }
    public  boolean saveUsers(UserBean user){
        return DB_Manager.getInstance().saveUser(user);
    }
    public  boolean updateUsers(UserBean user){
        return DB_Manager.getInstance().udapteUser(user);
    }
    public UserBean getUsers(String username){
        return DB_Manager.getInstance().getUser(username);
    }


}
