package ucai.cn.fulishe.Dao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ucai.cn.fulishe.Utils.I;

/**
 * Created by Administrator on 2016/10/24.
 */
public class DBSqlHelper extends SQLiteOpenHelper{
    public  static DBSqlHelper INSTANCE;
    public  static int Mversion=1;
    public  static final String CreateDB="CREATE TABLE "
            +UserDao.USER_TABLE_NAME+" ("
            +UserDao.USER_COLUMN_NAME
            +" TEXT PRIMARY KEY, "
            +UserDao.USER_COLUMN_NICK
            +" TEXT, "
            +UserDao.USER_COLUMN_AVATAR_ID+" INTEGER, "
            +UserDao.USER_COLUMN_AVATAR_TYPE+" INTEGER,"
            +UserDao.USER_COLUMN_AVATAR_PATH+" TEXT, "
            +UserDao.USER_COLUMN_AVATAR_SUFFIX+" TEXT,"
            +UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME
            +" TEXT);";
    public  static String UpdateDB="";
    public  static String CloseDB="";
    public DBSqlHelper(Context context) {
        super(context, getUserDatabaseName(), null, Mversion);
    }

    public static String getUserDatabaseName() {
        return I.User.TABLE_NAME+"_demo.db";
    }

    public  static DBSqlHelper init(Context context){
        if (INSTANCE==null)
        {
            INSTANCE = new DBSqlHelper(context.getApplicationContext());
        }
        return INSTANCE;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
   db.execSQL(CreateDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    if (newVersion>oldVersion)
    {
        db.execSQL(UpdateDB);
    }
    }
    public void closeDB() {
        if (INSTANCE!=null){
          SQLiteDatabase db=INSTANCE.getWritableDatabase();
            db.close();
            INSTANCE=null;
        }
    }
}
