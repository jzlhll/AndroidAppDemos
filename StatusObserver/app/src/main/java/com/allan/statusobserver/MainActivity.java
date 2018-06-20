package com.allan.statusobserver;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements WeakHandler.WeakCallback{
    private MyScrollColorTextView mInfoTv;

    private ContentObserver mAllObserver;

    private ContentObserver mOneObserver;
    private WeakHandler mHandler;

    @Override
    public void onHandler(Message msg) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mAllObserver);
        getContentResolver().unregisterContentObserver(mOneObserver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // adb logcat -v time | grep -e "MyObserver" -e "MyProvider"
        setContentView(R.layout.activity_main);
        mHandler = new WeakHandler(this, Looper.getMainLooper());

        mInfoTv = findViewById(R.id.info);

        mAllObserver = new StatusObserver("All", mHandler); //注册了一个全变化的监听
        //这里第二个给true，让All监听者能全部收到
        getContentResolver().registerContentObserver(Constant.CONTENT_URI, true, mAllObserver);
        //具体接收的给false，不让他接收其他的父节点
        mOneObserver = new StatusObserver("One", mHandler); //注册了一个只监听token变化的Observer
        getContentResolver().registerContentObserver(Constant.CONTENT_URI_TOKEN, false, mOneObserver);
    }

    private void queryAllTest() {
        Cursor cursor = getContentResolver().query(Constant.CONTENT_URI, null, null, null, null);
        if (cursor == null) {
            mInfoTv.addTimerText("query All Cursor = null");
            return;
        }

        cursor.moveToFirst();
        int columnCount = cursor.getColumnCount();
        int count = cursor.getCount();
        //由于使用的cursor不是很规范，所以这里获取不支持getColumnIndex
        //请直接使用0代表somethingexist，1代表token，2代表cid
        String s = "";
        s += "somethingExist:" + cursor.getInt(0);
        cursor.moveToNext();
        s += ", token:" + cursor.getString(1);
        cursor.moveToNext();
        s += ", cid:" + cursor.getInt(2);
        cursor.close();
        mInfoTv.addTimerText(s);
    }

    private void queryTokenTest() {
        Uri uri = Constant.CONTENT_URI_TOKEN;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            mInfoTv.addTimerText("queryTokenTest cursor= null");
            return;
        }

        cursor.moveToFirst();
        int columnCount = cursor.getColumnCount();
        int count = cursor.getCount();
        mInfoTv.addTimerText("query Token :" + cursor.getString(0));
        cursor.close();
    }

    private void updateAllTest() {
        ContentValues values = new ContentValues();
        int cid = (int) (Math.random() * 100);

        values.put(Constant.KEY_NAME_CID, cid);
        values.put(Constant.KEY_NAME_SOMETHING, cid % 2);
        values.put(Constant.KEY_NAME_TOKEN, "token:" + cid);

        try {
            getContentResolver().update(Constant.CONTENT_URI, values, null, null);
        } catch (Exception e) {
            mInfoTv.addTimerText("update AllTest error! 不支持");
        }
    }

    private void updateTokenTest() {
        ContentValues values = new ContentValues();
        int uriType = (int) (Math.random() * 3);
        Uri uri = Uri.parse(Constant.CONTENT_URI_STRING + "/" + uriType);
        int cid = (int) (Math.random() * 100);
        switch (uriType) {
            case 0:
            {
                values.put(Constant.KEY_NAME_SOMETHING, cid % 2 == 0);
            }
                break;
            case 1:
            {
                values.put(Constant.KEY_NAME_TOKEN, "token:" + cid);
            }
            break;
            case 2:
            {
                values.put(Constant.KEY_NAME_CID, cid);
            }
            break;
        }

        mInfoTv.addTimerText("click-change-"+uri + ";" + values);

        getContentResolver().update(uri, values, null, null);
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.queryBtn:
                queryAllTest();
                break;
            case R.id.updateAllBtn:
                updateAllTest();
                break;
            case R.id.updateTokenBtn:
                updateTokenTest();
                break;
            case R.id.queryOneBtn:
                queryTokenTest();
                break;
        }
    }

    private class StatusObserver extends ContentObserver {
        private static final String TAG = "StatusObServer";
        private String mTag = "";
        public StatusObserver(String tag, Handler handler) {
            super(handler);
            mTag = tag;
        }
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mInfoTv.addTimerText(mTag + " 2onChanged " + selfChange + " " + uri);
        }
    }
}
