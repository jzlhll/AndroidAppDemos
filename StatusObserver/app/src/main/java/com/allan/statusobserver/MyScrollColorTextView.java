package com.allan.statusobserver;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;

import java.util.Queue;

public class MyScrollColorTextView extends android.support.v7.widget.AppCompatTextView implements WeakHandler.WeakCallback {
    private static final int LOG_LINE = 10;
    private WeakHandler mTimerHandler;

    private static final int MSG_SET = 1;
    private static final int MSG_ADD = 2;

    @Override
    public void onHandler(Message msg) {
        switch (msg.what) {
            case MSG_ADD: {
                mQueue.offer(msg.obj.toString());
                setText(Html.fromHtml(mQueue.toString()));
            }
            break;
            case MSG_SET:
                mQueue.clear();
                setText(msg.obj.toString());
                break;
        }
    }

    private void init() {
        mTimerHandler = new WeakHandler(MyScrollColorTextView.this, Looper.getMainLooper());
    }

    MyScrollColorTextView(Context context) {
        super(context);
        init();
    }

    public MyScrollColorTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyScrollColorTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private final Queue<String> mQueue = new LimitQueue<String>(LOG_LINE) {
        private final String[] COLORS = new String[]{
                "<font color='#FF0000'>", "<font color='#CDCD00'>", "<font color='#90EE90'>",
                "<font color='#8FBC8F'>", "<font color='#A4D3EE'>", "<font color='#A4D3EE'>",
                "<font color='#A4D3EE'>", "<font color='#828282'>", "<font color='#A8A8A8'>",
                "<font color='#CCCCCC'>"
        };

        private String getColorString(String s, int colorNum) {
            return String.format("%s%s</font>", COLORS[colorNum], s);
        }

        private String getSizeString(String s, int index) {
            if (index < 3) {
                return String.format("<small>%s</small>", s);
            }
            if (index < 7) {
                return s;
            }
            return String.format("<big>%s</big>", s);
        }

        @Override
        public String toString() {
            String htmlRet = "";
            int count = queue.size();
            for (String s : queue) {
                count--;
                htmlRet = htmlRet + getColorString(getSizeString(s, LOG_LINE - count), count) + "<br>";
            }
            return htmlRet;
        }
    };

    /**
     * 调用这个，将直接显示，清理之前所有的信息
     */
    public void setTimerText(String s) {
        synchronized (mQueue) {
            MyLog.d(s);
            mTimerHandler.removeCallbacksAndMessages(null);
            mTimerHandler.sendMessage(mTimerHandler.obtainMessage(MSG_SET, s));
        }
    }

    public void addTimerText(String s) {
        synchronized (mQueue) {
            MyLog.d(s);
            mTimerHandler.sendMessage(mTimerHandler.obtainMessage(MSG_ADD, s));
        }
    }

}
