package allan.com.allancontentprovider.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import allan.com.allancontentprovider.MyLog;

public class DbContentProvider extends ContentProvider {

    public DbContentProvider() {
    }

    private MyDbHelper mDbHelper;
    private static final String DBNAME = "my.db";

    @Override
    public boolean onCreate() {
        mDbHelper = new MyDbHelper(getContext(), DBNAME, 1, 1);
        return false;
    }

    @Nullable
    @Override
    public final String getType(@NonNull Uri uri) {
        switch (Constant.MATCHERS.match(uri)) {
            case Constant.TYPE_ID_NOTIFY:
                return Constant.MIME_DIR_PREFIX + Constant.TABLE_NOTIFY;
            case Constant.TYPE_ID_ORDER:
                return Constant.MIME_DIR_PREFIX + Constant.TABLE_ORDER;
            case Constant.TYPE_ID_NORDER_ITEM:
                return Constant.MIME_ITEM_PREFIX + Constant.TABLE_NOTIFY;
            case Constant.TYPE_ID_NOTIFY_ITEM: {
                return Constant.MIME_ITEM_PREFIX + Constant.TABLE_ORDER;
            }
        }

        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        checkPermission(true);
        MyLog.d("query......");
        int rowId = Constant.parseUriToId(uri);
        switch (rowId) {
            case -2:
                MyLog.e("insert Please path the correct uri {\" + uri + \"}!");
                return null;
            case -1:
            default: {
                Cursor cursor = mDataModel.query(rowId, projection, selection, selectionArgs, sortOrder);
                //注册内容观察者，观察数据变化 TODO 是否添加此操作这是什么
                //cursor.setNotificationUri(getContext().getContentResolver(), StatusStatics.CONTENT_URI);
                return cursor;
            }
        }
    }

    private void checkPermission(boolean readOrWrite) {
        String permission = readOrWrite ? "com.allan.permission.OB_READ_STATE" : "com.allan.permission.OB_WRITE_STATE";
        getContext().enforcePermission(permission, Binder.getCallingPid(), Binder.getCallingUid(),
                "unless " + permission + " is granted");
    }
}
