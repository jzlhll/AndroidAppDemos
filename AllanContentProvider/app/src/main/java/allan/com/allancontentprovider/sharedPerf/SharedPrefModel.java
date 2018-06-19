package allan.com.allancontentprovider.sharedPerf;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.AbstractCursor;
import android.database.Cursor;

import allan.com.allancontentprovider.model.IDataModel;

public class SharedPrefModel implements IDataModel {
    private static final int THIS_VERSION = 1;
    private static final boolean NO_SUPPORT_DELETE_INSERT = true;

    private Context mCon;
    private SharedPreferences mSp;

    @Override
    public void init(Context context) {
        mCon = context;
        mSp = mCon.getSharedPreferences("content_provider_db", Context.MODE_PRIVATE);
        SharedPreferences appSp = mCon.getSharedPreferences("private", Context.MODE_PRIVATE);
        if (appSp.getInt("content_provider_ver", 0) < THIS_VERSION) {
            //init
            mSp.edit().putInt(StatusStatics.KEY_NAME_CID, 0)
                    .putBoolean(StatusStatics.KEY_NAME_SOMETHING, false)
                    .putString(StatusStatics.KEY_NAME_TOKEN, "dfd").apply();
            //版本标记
            appSp.edit().putInt("content_provider_ver", THIS_VERSION).apply();
        }
    }

    public SharedPrefModel() {
    }

    @Override
    public Cursor query(final int rowId, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //TODO 暂时不支持其他过滤的东西
        Cursor cursor = new AbstractCursor() {
            @Override
            public int getCount() {
                return rowId == -1 ? 3 : 1; //-1表示，全部就是3个status；不是-1就只有一条
            }

            @Override
            public String[] getColumnNames() {
                return new String[]{
                        StatusStatics.KEY_NAME_SOMETHING, StatusStatics.KEY_NAME_TOKEN, StatusStatics.KEY_NAME_CID
                };
            }

            @Override
            public String getString(int column) {
                switch (column) {
                    case 1:
                        return mSp.getString(StatusStatics.KEY_NAME_TOKEN, null);
                }
                return null;
            }

            @Override
            public short getShort(int column) {
                return 0;
            }

            @Override
            public int getInt(int column) {
                switch (column) {
                    case 2:
                        return mSp.getInt(StatusStatics.KEY_NAME_CID, -1);
                    case 0:
                        return mSp.getBoolean(StatusStatics.KEY_NAME_SOMETHING, false) ? 1 : 0;
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

        return cursor;
    }

    @Override
    public int insert(ContentValues values) {
        if (NO_SUPPORT_DELETE_INSERT) return 0; //不支持此事
        //TODO 修改此处
        Boolean somethingExist = values.getAsBoolean(StatusStatics.KEY_NAME_SOMETHING);
        String token = values.getAsString(StatusStatics.KEY_NAME_TOKEN);
        Integer cid = values.getAsInteger(StatusStatics.KEY_NAME_CID);

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
    public int delete(int rowId) {
        if (NO_SUPPORT_DELETE_INSERT) return 0; //不支持此事
        switch (rowId) {
            case 0:
                mSp.edit().remove(StatusStatics.KEY_NAME_SOMETHING).apply();
            case 1: {
                mSp.edit().remove(StatusStatics.KEY_NAME_TOKEN).apply();
            }
            case 2:
                mSp.edit().remove(StatusStatics.KEY_NAME_CID).apply();
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
        //TODO 修改
        return insert(values);
    }


    // TODO 不断追加的地方
    private void updateSomething(boolean exist) {
        mSp.edit().putBoolean(StatusStatics.KEY_NAME_SOMETHING, exist).apply();
    }

    private void updateToken(String token) {
        mSp.edit().putString(StatusStatics.KEY_NAME_TOKEN, token).apply();
    }

    private void updateCid(int cid) {
        mSp.edit().putInt(StatusStatics.KEY_NAME_SOMETHING, cid).apply();
    }
}
