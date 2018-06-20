package com.allan.statusobserver;

import android.util.Log;

public class MyLog {
    public static void d(String s) {
        Log.w("MyObserver", s); //由于华为手机LOG打印级别太低不输出
    }
}
