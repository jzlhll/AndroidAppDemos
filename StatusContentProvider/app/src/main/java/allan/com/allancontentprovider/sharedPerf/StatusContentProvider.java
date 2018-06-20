package allan.com.allancontentprovider.sharedPerf;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import allan.com.allancontentprovider.MyLog;

public class StatusContentProvider extends ContentProvider {
    private static final String TAG = "StatusProvider";

    IDataModel mDataModel = null;

    public StatusContentProvider() {
    }

    @Override
    public boolean onCreate() {
        mDataModel = new SharedPrefModel();
        mDataModel.init(getContext());
        return false;
    }

    @Nullable
    @Override
    public final String getType(@NonNull Uri uri) {
        switch (Constant.MATCHERS.match(uri)) {
            case Constant.TYPE_ID_STATUS: {
                return Constant.MIME_DIR_PREFIX + Constant.STATUS;
            }
            case Constant.TYPE_ID_STATUS_ITEM:
                return Constant.MIME_ITEM_PREFIX + Constant.STATUS;
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

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) { //要求客户端必须uri是全部，因为是添加。StatusStatics.CONTENT_URI
        checkPermission(false);
        MyLog.d("insert......");
        int rowId = Constant.parseUriToId(uri);
        switch (rowId) {
            case -2:
                MyLog.e("insert Please path the correct uri {\" + uri + \"}!");
                return null;
            case -1: { //只匹配全部
                int dbRowId = mDataModel.insert(values);
                if (dbRowId == -1) {
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return uri;
            }
            default:
                MyLog.e("insert2 Please path the correct uri {\" + uri + \"}!");
                return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) { //可以删除单个，也可以全部
        checkPermission(false);
        MyLog.d("delete......");
        if (selection != null || selectionArgs != null) {
            throw new UnsupportedOperationException("selection selectionArgs should be null");
        }

        Uri parsedUri = Constant.parseUriToMyUri(uri);
        int rowId = Constant.parseUriToId(uri);
        int affectCount = 0;
        switch (rowId) {
            case -2:
                throw new UnsupportedOperationException("delete2 Please path the correct uri {" + uri + "}!");
            case -1: {
                affectCount = mDataModel.deleteAll();
            }
            break;
            default:
                affectCount = mDataModel.delete(rowId);
                break;
        }
        getContext().getContentResolver().notifyChange(parsedUri, null);
        return affectCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        //只允许一条一条更新
        checkPermission(false);
        Uri parsedUri = Constant.parseUriToMyUri(uri);
        int rowId = Constant.parseUriToId(uri);
        switch (rowId) {
            case -2:
                throw new UnsupportedOperationException("update Please path the correct uri {" + uri + "}!");
            case -1: {
                throw new UnsupportedOperationException("update2 Please path the correct uri {" + uri + "}!");
            }
            default:
                break;
        }

        int affectCount = mDataModel.update(values);
        MyLog.d("update...... " + parsedUri);
        getContext().getContentResolver().notifyChange(parsedUri, null);
        return affectCount;
    }

    private void checkPermission(boolean readOrWrite) {
        String permission = readOrWrite ? "com.allan.permission.OB_READ_STATE" : "com.allan.permission.OB_WRITE_STATE";
        getContext().enforcePermission(permission, Binder.getCallingPid(), Binder.getCallingUid(),
                "unless " + permission + " is granted");
    }
}
