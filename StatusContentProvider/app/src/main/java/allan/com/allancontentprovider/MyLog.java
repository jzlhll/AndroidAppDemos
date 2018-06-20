package allan.com.allancontentprovider;

import android.util.Log;

public class MyLog {
    public static void d(String s) {
        Log.w("MyProvider", s); //由于华为手机LOG打印级别太低不输出
    }

    public static void e(String s) {
        Log.e("MyProvider", s); //由于华为手机LOG打印级别太低不输出
    }
}
