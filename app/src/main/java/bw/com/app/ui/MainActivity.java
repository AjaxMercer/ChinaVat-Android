package bw.com.app.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

import org.viewutils.ViewUtil;
import org.viewutils.annotation.ContentView;
import org.viewutils.annotation.ViewInject;

import java.util.ArrayList;

import bw.com.app.R;
import bw.com.app.model.bean.TestBean;
import bw.com.app.utils.CommonAdapter;
import bw.com.app.utils.CommonViewHolder;
import bw.com.app.widgets.TSFilterSortView;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getName();
    @ViewInject(R.id.li)
    ListView list;
    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.ts_filter_sort_view)
    TSFilterSortView mTSFilterSortView;

    public CommonAdapter<TestBean> commonAdapter;
    public ArrayList<TestBean> arrayList;
    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.view().inject(this);
        setSupportActionBar(toolbar);
        gestureDetector = new GestureDetector(new MyGestureDetector());
        arrayList = new ArrayList<>();
        for (int i = 0; i < 120; i++) {
            TestBean testBean = new TestBean();
            testBean.setTitle("第"+i+"条");
            testBean.setDesc("天气,哈哈哈哈");
            testBean.setPhone("135202025");
            testBean.setTime(System.nanoTime()+"");
            arrayList.add(testBean);
        }
//        list = (ListView) findViewById(R.id.li);
        list.setAdapter(commonAdapter = new CommonAdapter<TestBean>(getLayoutInflater(), getApplicationContext(), arrayList, R.layout.item_list) {
            @Override
            public void convert(CommonViewHolder viewHolder, TestBean item, int position) {
                viewHolder.setText(R.id.tv_title, item.getTitle());
                viewHolder.setText(R.id.tv_describe, item.getDesc());
                viewHolder.setText(R.id.tv_phone, item.getPhone());
                viewHolder.setText(R.id.tv_time, item.getTime());
            }
        });
        list.setOnTouchListener(gestureTouchListener);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private View.OnTouchListener gestureTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            return false;
        }
    };

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 50;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH) {
                return false;
            }

            // up to down swipe
            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) {
                if (mTSFilterSortView.getVisibility() != View.GONE) {
                    if (commonAdapter.getCount() > 5) {
                        Animation animation = new TranslateAnimation(0, 0, 0, mTSFilterSortView.getMeasuredHeight());
                        animation.setDuration(300);
                        mTSFilterSortView.startAnimation(animation);
                        mTSFilterSortView.setVisibility(View.GONE);
                        Log.i(TAG,"Fling down");
                    }
                }
                return true;
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) {
                if (mTSFilterSortView.getVisibility() == View.GONE) {
                    Animation animation = new TranslateAnimation(0, 0, mTSFilterSortView.getMeasuredHeight(), 0);
                    animation.setDuration(300);
                    mTSFilterSortView.startAnimation(animation);
                    mTSFilterSortView.setVisibility(View.VISIBLE);
                    Log.i(TAG, "Fling up");
                }


                return true;
            }

            return false;
        }
    }

}
