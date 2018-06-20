package allan.com.allancontentprovider.sharedPerf;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.AbstractCursor;
import android.database.Cursor;

import allan.com.allancontentprovider.MyLog;
import allan.com.allancontentprovider.model.Constant;
import allan.com.allancontentprovider.model.IDataModel;

/**
 * 使用sharedPreference作为介质存储数据
 */
public class SharedPrefModel implements IDataModel {
    private static final int THIS_VERSION = 3;
    private static final boolean NO_SUPPORT_DELETE_INSERT = true;

    private Context mCon;
    private SharedPreferences mSp;

    @Override
    public void init(Context context) {
        mCon = context;
        mSp = mCon.getSharedPreferences("content_provider_db", Context.MODE_PRIVATE);
        SharedPreferences appSp = mCon.getSharedPreferences("private", Context.MODE_PRIVATE);
        //根据版本号是否重建status表
        if (appSp.getInt("content_provider_ver", 0) < THIS_VERSION) {
            MyLog.d("first time init status data! cid 8 exist false token:110");
            //init
            mSp.edit().putInt(Constant.KEY_NAME_CID, 8)
                    .putBoolean(Constant.KEY_NAME_SOMETHING, true)
                    .putString(Constant.KEY_NAME_TOKEN, "token:110").apply();
            //版本标记
            appSp.edit().putInt("content_provider_ver", THIS_VERSION).apply();
        }
    }

    public SharedPrefModel() {
    }

    @Override
    public Cursor query(final int rowId, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //TODO 暂时不支持其他过滤的东西
        MyLog.d("query rowId " + rowId + " " + mSp.getString(Constant.KEY_NAME_TOKEN, null));
        //**由于使用的是SP保存，没有cursor，自己构建，那么要求客户端查询的时候，不能太随意**
        return new AbstractCursor() {
            @Override
            public int getCount() {
                return rowId == -1 ? 3 : 1; //rowId -1表示，全部就是3个status；不是-1就只有一条
            }

            @Override
            public String[] getColumnNames() {
                switch (rowId) {
                    case -1:
                        return new String[]{Constant.KEY_NAME_SOMETHING, Constant.KEY_NAME_TOKEN, Constant.KEY_NAME_CID};
                    case 0:
                        return new String[]{Constant.KEY_NAME_SOMETHING};
                    case 1:
                        return new String[]{Constant.KEY_NAME_TOKEN};
                    case 2:
                        return new String[]{Constant.KEY_NAME_CID};
                }
                return null;
            }

            @Override
            public String getString(int column) {
                switch (rowId) {
                    case -1:
                        switch (column) {
                            case 0:
                                return "" + mSp.getBoolean(Constant.KEY_NAME_SOMETHING, false);
                            case 1:
                                return mSp.getString(Constant.KEY_NAME_TOKEN, null);
                            case 2:
                                return "" + mSp.getInt(Constant.KEY_NAME_CID, -20);
                        }
                    case 0:
                        return "" + mSp.getBoolean(Constant.KEY_NAME_SOMETHING, false);
                    case 1:
                        return mSp.getString(Constant.KEY_NAME_TOKEN, null);
                    case 2:
                        return "" + mSp.getInt(Constant.KEY_NAME_CID, -20);
                }
                return null;
            }

            @Override
            public short getShort(int column) {
                return 0;
            }

            @Override
            public int getInt(int column) {
                switch (rowId) {
                    case -1:
                        switch (column) {
                            case 0:
                                return mSp.getBoolean(Constant.KEY_NAME_SOMETHING, false) ? 1 : 0;
                            case 2:
                                return mSp.getInt(Constant.KEY_NAME_CID, -20);
                        }
                    case 0:
                        return mSp.getBoolean(Constant.KEY_NAME_SOMETHING, false) ? 1 : 0;
                    case 2:
                        return mSp.getInt(Constant.KEY_NAME_CID, -20);
                }
                return -1;
            }

            @Override
            public long getLong(int column) {
                return 0;
            }

            @Override
            public float getFloat(int column) {
                return 0;
            }

            @Override
            public double getDouble(int column) {
                return 0;
            }

            @Override
            public boolean isNull(int column) {
                return false;
            }
        };
    }

    private int insertReal(ContentValues values) {
        Boolean somethingExist = values.getAsBoolean(Constant.KEY_NAME_SOMETHING);
        String token = values.getAsString(Constant.KEY_NAME_TOKEN);
        Integer cid = values.getAsInteger(Constant.KEY_NAME_CID);

        int count = 0;

        if (somethingExist != null) {
            updateSomething(somethingExist);
            count++;
        }
        if (token != null) {
            updateToken(token);
            count++;
        }

        if (cid != null) {
            updateCid(cid);
            count++;
        }

        return count;
    }

    @Override
    public int insert(ContentValues values) {
        if (NO_SUPPORT_DELETE_INSERT) return 0; //不支持此事
        return insertReal(values);
    }

    @Override
    public int delete(int rowId) {
        if (NO_SUPPORT_DELETE_INSERT) return 0; //不支持此事
        switch (rowId) {
            case 0:
                mSp.edit().remove(Constant.KEY_NAME_SOMETHING).apply();
            case 1: {
                mSp.edit().remove(Constant.KEY_NAME_TOKEN).apply();
            }
            case 2:
                mSp.edit().remove(Constant.KEY_NAME_CID).apply();
        }
        return 1;
    }

    @Override
    public int deleteAll() {
        if (NO_SUPPORT_DELETE_INSERT) return 0; //不支持此事
        mSp.edit().clear().apply();
        return 0;
    }

    @Override
    public int update(ContentValues values) {
        return insertReal(values);
    }


    // TODO 不断追加的地方
    private void updateSomething(boolean exist) {
        mSp.edit().putBoolean(Constant.KEY_NAME_SOMETHING, exist).apply();
    }

    private void updateToken(String token) {
        mSp.edit().putString(Constant.KEY_NAME_TOKEN, token).apply();
    }

    private void updateCid(int cid) {
        mSp.edit().putInt(Constant.KEY_NAME_SOMETHING, cid).apply();
    }
}
