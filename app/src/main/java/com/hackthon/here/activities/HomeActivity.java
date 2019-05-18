package com.hackthon.here.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hackthon.here.R;
import com.hackthon.here.Utils;
import com.hackthon.here.adapters.HomeViewPagerAdapter;
import com.hackthon.here.fragments.NotificationsFragment;
import com.hackthon.here.fragments.OrdersFragment;
import com.hackthon.here.fragments.PostsFragment;
import com.hackthon.here.fragments.ProfileFragment;
import com.hackthon.here.fragments.ReturnListFragment;
import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;

import java.io.File;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private MenuItem prevMenuItem;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.orderslist));
                        mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_profile:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.profiletext));
                        mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.notifications));
                        mViewPager.setCurrentItem(2);
                    return true;
//                case R.id.navigation_posts:
//                        mViewPager.setCurrentItem(3);
//                    return true;
                case R.id.navigation_return_list:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.returns));
                        mViewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mViewPager = findViewById(R.id.home_viewpager);



        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                navigation.getMenu().getItem(i).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        setupViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getSupportFragmentManager());

        OrdersFragment ordersFragment = new OrdersFragment();
        adapter.addFragment(ordersFragment);

        ProfileFragment profileFragment = new ProfileFragment();
        adapter.addFragment(profileFragment);

        NotificationsFragment notificationsFragment = new NotificationsFragment();
        adapter.addFragment(notificationsFragment);

//        PostsFragment postsFragment = new PostsFragment();
//        adapter.addFragment(postsFragment);

        ReturnListFragment returnListFragment = new ReturnListFragment();
        adapter.addFragment(returnListFragment);

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                break;
            case R.id.track:
                Intent trackIntent = new Intent(HomeActivity.this,TrackActivity.class);
                trackIntent.putExtra("driverId","Eqk3ERkjzNNnggCYdc5CsXDWuxR2");
                trackIntent.putExtra("itemName","Headphones with 2 buds");
                trackIntent.putExtra("driverName","AK");
                trackIntent.putExtra("driverMobile","9941980802");
                startActivity(trackIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
