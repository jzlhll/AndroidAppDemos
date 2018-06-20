package com.allan.statusobserver;

import android.content.UriMatcher;
import android.net.Uri;

public class Constant {
    public static final String KEY_NAME_SOMETHING = "isSomethingExist";
    public static final String KEY_NAME_TOKEN = "dataToken";
    public static final String KEY_NAME_CID = "customId";

    public static final String AUTHORITY = "com.allan.AllanContentProvider";
    public static final String STATUS = "status";
    public static final String MIME_DIR_PREFIX = "vnd.android.cursor.dir/";
    public static final String MIME_ITEM_PREFIX = "vnd.android.cursor.item/";

    public static final String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + STATUS;
    public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);

    public static final String CONTENT_URI_TOKEN_STR = "content://" + AUTHORITY + "/" + STATUS + "/1";
    public static final Uri CONTENT_URI_TOKEN = Uri.parse(CONTENT_URI_TOKEN_STR);

    public static final UriMatcher MATCHERS = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int TYPE_ID_STATUS = 1;
    public static final int TYPE_ID_STATUS_ITEM = 2;

    static {
        MATCHERS.addURI(AUTHORITY, STATUS, TYPE_ID_STATUS);
        MATCHERS.addURI(AUTHORITY, STATUS + "/#", TYPE_ID_STATUS_ITEM);
    }
}
