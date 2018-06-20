package allan.com.allancontentprovider.sharedPerf;

import android.content.ContentUris;
import android.content.UriMatcher;
import android.net.Uri;

public class Constant {
    // TODO 不断追加的地方
    public static final String KEY_NAME_SOMETHING = "isSomethingExist";
    public static final String KEY_NAME_TOKEN = "dataToken";
    public static final String KEY_NAME_CID = "customId";

    public static final String AUTHORITY = "com.allan.AllanContentProvider";
    public static final String STATUS = "status";
    public static final String MIME_DIR_PREFIX = "vnd.android.cursor.dir/";
    public static final String MIME_ITEM_PREFIX = "vnd.android.cursor.item/";

    public static final String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + STATUS;
    public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);

    public static final UriMatcher MATCHERS = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int TYPE_ID_STATUS = 1;
    public static final int TYPE_ID_STATUS_ITEM = 2;

    static {
        MATCHERS.addURI(AUTHORITY, STATUS, TYPE_ID_STATUS);
        MATCHERS.addURI(AUTHORITY, STATUS + "/#", TYPE_ID_STATUS_ITEM);
    }

    /**
     * -2表示匹配不了<p>
     * -1表示匹配上了**全部**<p>
     * >= 0 返回的是解析出来的代码行
     */
    public static int parseUriToId(Uri uri) {
        switch (MATCHERS.match(uri)) {
            case TYPE_ID_STATUS:
                return -1;
            case TYPE_ID_STATUS_ITEM: {
                int id = (int) ContentUris.parseId(uri);
                return id;
            }
        }
        return -2;
    }

    /**
     * return：<p>
     * null表示匹配错误<p>
     * -1表示匹配上了**全部**<p>
     * >= 0 返回的是解析出来的代码行
     */
    public static Uri parseUriToMyUri(Uri uri) {
        switch (MATCHERS.match(uri)) {
            case TYPE_ID_STATUS:
                return Uri.parse(CONTENT_URI_STRING);
            case TYPE_ID_STATUS_ITEM: {
                int id = (int) ContentUris.parseId(uri);
                return Uri.parse(CONTENT_URI_STRING + "/" + id);
            }
            default:
                return null;
        }
    }
}
