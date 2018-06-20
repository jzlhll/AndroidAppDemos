package allan.com.viewpagetester;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.List;

public class ScrollableViewPage extends ViewPager {
    private boolean mScrollable = false;

    public ScrollableViewPage(Context context) {
        super(context);
    }

    public ScrollableViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mScrollable) {
            return true;
        }
        return super.onTouchEvent(ev);
    }

    public boolean isScrollable() {
        return mScrollable;
    }

    public void setScrollable(boolean mScrollable) {
        this.mScrollable = mScrollable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (mScrollable) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        private List<Fragment> mlist;

        MyAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            mlist = list;
        }


        @Override
        public Fragment getItem(int arg0) {
            return mlist.get(arg0);
        }

        @Override
        public int getCount() {
            return mlist.size();
        }

    }
}
