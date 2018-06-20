package allan.com.allancontentprovider.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import allan.com.allancontentprovider.MyLog;

public class MyDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "MyDbHelper";
    public final static String DATABASE_NAME = "mine.db";
    private final static boolean DEBUG = true;
    // 创建数据库的语句
    private static final String CARD_TAB_CREATE_V1 = "create table "
            + Constant.TABLE_NOTIFY
            + " (_id integer primary key autoincrement," // _id
            + "name text not null default 'UNKWN'," // 名字
            + "modify_time INTEGER not null default 0," // 时间
            + "otherinfos text," // 其他信息
            + "isRead BOOLEAN default 0" // 是否已读
            + ");";

    private SQLiteDatabase db;

    private Context context;
    // 数据库版本号
    private int versionCode = 1;

    public MyDbHelper(Context context, String dbName, int DBVERSION, int oldVersion) {
        super(context, dbName, null, DBVERSION);

        this.context = context;
        versionCode = oldVersion;
        MyLog.d("MyDbHelper dbversion " + DBVERSION);
        this.context = context;
        versionCode = oldVersion;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CARD_TAB_CREATE_V1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MyLog.d("onUpgrade MyDbHelper oldVersion "
                + oldVersion + "  newVersion " + newVersion);
        this.db = db;
        //TODO update
    }

    private SQLiteDatabase getDb() {
        if (this.db == null) {
            if (DEBUG)
                MyLog.d("insert db = null");
            db = getWritableDatabase();// 得到一个可读写的数据库实例
        }
        return db;
    }

    @Override
    public synchronized void close() {
        if (db != null && db.isOpen())
            db.close();
    }

    public void deleteTab(String tableName) {
        getDb();
        if (DEBUG)
            MyLog.d("del set table....");
        db.delete(tableName, null, null);
    }

    public void deleteDataBase() {
        getDb();
        if (DEBUG)
            MyLog.d("del set table....");
        context.deleteDatabase(DATABASE_NAME);
    }

    public Cursor queryAll(String tableName) {
        getDb();
        String sql = "select" + " * " + " from " + tableName;
        return db.rawQuery(sql, null);
    }

    public int updateARowAsIsRead(int _id, boolean isRead, String tableName) {
        if (_id < 0)
            return -1;
        getDb();
        ContentValues values = new ContentValues();
        values.put("isRead", isRead);
        // 此ID为该卡组的数据库_id
        return db.update(tableName, values, "_id=?",
                new String[]{"" + _id});
    }

    /**
     * 用于向一个tab插入数据
     *
     * @param values    具体的数据
     * @param tableName 表名
     */
    public boolean insert(ContentValues values, String tableName) {
        getDb();
        return db.insert(tableName, null, values) != -1;
    }

    /**
     * @param list      插入的条目
     * @param tableName 表名
     * @return 剩余多少没有插入
     */
    public int insert(List<ContentValues> list, String tableName) {
        if (list == null || list.size() == 0) return 0;
        getDb();
        int ret = list.size();
        for (ContentValues cv : list) {
            if (db.insert(tableName, null, cv) != -1) {
                ret--;
            }
        }
        return ret;
    }

    /**
     * 用来删除一个卡组，通过id
     *
     * @param _id
     * @return
     */
    public void delete(int _id, String tableName) {
        getDb();
        db.delete(tableName, "_id=?", new String[]{""
                + _id});
    }

    /**
     * 用来删除N个卡组，通过ids
     *
     * @param _ids
     * @return
     */
    public void delete(int[] _ids, String tableName) {
        if (_ids == null || _ids.length == 0) {
            return;
        }
        getDb();
        db.beginTransaction();
        String[] idstrs = new String[]{"0"};
        try {
            for (int _id : _ids) {
                idstrs[0] = "" + _id;
                db.delete(tableName, "_id=?", idstrs);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public static void exportDBtoSd(Context con, String dbName) {
        String oldPath = con.getDatabasePath(dbName)
                .getAbsolutePath();
        File dir = new File(Environment.getExternalStorageDirectory()
                + File.separator + "dbDir");
        if (!dir.isDirectory())
            dir.mkdir();
        String newPath = Environment.getExternalStorageDirectory()
                + File.separator + "dbDir" + File.separator
                + dbName;
        copyFile(oldPath, newPath);
    }

    private static void copyFile(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newfile = new File(newPath);
            if (!newfile.exists()) {
                newfile.createNewFile();
            }
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
