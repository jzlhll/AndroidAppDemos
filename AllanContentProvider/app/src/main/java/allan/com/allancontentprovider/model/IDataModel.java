package allan.com.allancontentprovider.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public interface IDataModel {
    void init(Context context);
    public Cursor query(int rowId, String[] projection, String selection, String[] selectionArgs, String sortOrder);

    public int insert(ContentValues values);

    public int delete(int rowId);
    public int deleteAll();

    public int update(ContentValues values);
}
