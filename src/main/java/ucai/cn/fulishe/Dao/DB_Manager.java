package ucai.cn.fulishe.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ucai.cn.fulishe.Utils.I;
import ucai.cn.fulishe.bean.UserBean;

/**
 * Created by Administrator on 2016/10/24.
 */
public class DB_Manager {
  private    DBSqlHelper mHeleper;
  public static   DB_Manager dbManager =new DB_Manager();
   void onInit(Context context){
           mHeleper=new DBSqlHelper(context);
   }
  public static synchronized DB_Manager   getInstance(){
        return dbManager;
    }
public  void  closeDB() {
    if (mHeleper != null) {
        mHeleper.closeDB();
    }
}
    public synchronized boolean saveUser(UserBean user){
        SQLiteDatabase DB=mHeleper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(UserDao.USER_COLUMN_NAME,user.getMuserName());
        values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
        values.put(UserDao.USER_COLUMN_AVATAR_ID,user.getMavatarId());
        values.put(UserDao.USER_COLUMN_AVATAR_TYPE,user.getMavatarType());
        values.put(UserDao.USER_COLUMN_AVATAR_PATH,user.getMavatarPath());
        values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX,user.getMavatarSuffix());
        values.put(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME,user.getMavatarLastUpdateTime());
        if (DB.isOpen())
        {
            return DB.replace(UserDao.USER_TABLE_NAME,null,values)!=-1;
        }
        return false;
    }

    public synchronized UserBean getUser(String username) {
        SQLiteDatabase DB=mHeleper.getReadableDatabase();
        String sql="select * from "+UserDao.USER_TABLE_NAME
                +" where "+UserDao.USER_COLUMN_NAME+" =?";
        UserBean user=null;
        Cursor cursor=DB.rawQuery(sql,new String[]{username});
        if (cursor.moveToNext())
        {
            user=new UserBean();
            user.setMuserName(username);
            user.setMuserNick(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NICK)));
            user.setMavatarId(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_ID)));
            user.setMavatarType(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_TYPE)));
            user.setMavatarLastUpdateTime(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME)));
            user.setMavatarSuffix(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_SUFFIX)));
            user.setMavatarPath(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_PATH)));
            return user;
        }
          return user;
    }
    public synchronized boolean udapteUser(UserBean user){
        int result=-1;
        SQLiteDatabase db =mHeleper.getWritableDatabase();
        String sql=UserDao.USER_COLUMN_NAME+"=?";
        ContentValues values=new ContentValues();
        values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
        if (db.isOpen())
        {
            result=db.update(UserDao.USER_TABLE_NAME,values,sql,new String[]{user.getMuserName()});
        }
        return result>0;
    }
}



