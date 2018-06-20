package allan.com.viewpagetester;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BlankFragment.OnFragmentInteractionListener {

    private ScrollableViewPage mViewPage;

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            switch (i) {
                case 0:
                    //TODO 改变颜色
                    break;
                case 1:
                    //TODO 改变颜色
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private Button mFirstBtn, mSecondBtn;


    private List<Fragment> list;
    private ScrollableViewPage.MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPage = findViewById(R.id.myviewpager);
        mFirstBtn = findViewById(R.id.firstBtn);
        mSecondBtn = findViewById(R.id.secondBtn);

        mViewPage.addOnPageChangeListener(onPageChangeListener);

//把Fragment添加到List集合里面
        List list = new ArrayList<>();
        list.add(OneFragment.newInstance("fst",""));
        list.add(BlankFragment.newInstance("scd",""));
        adapter = new ScrollableViewPage.MyAdapter(getSupportFragmentManager(), list);
        mViewPage.setAdapter(adapter);
        mViewPage.setCurrentItem(0);  //初始化显示第一个页面
    }

    public void onClickMy(View v) {
        if (v.getId() == R.id.firstBtn) {
            mViewPage.setCurrentItem(0);
        } else if (v.getId() == R.id.secondBtn) {
            mViewPage.setCurrentItem(1);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
