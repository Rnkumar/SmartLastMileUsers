package com.hackthon.here.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.hackthon.here.R;
import com.hackthon.here.adapters.HomeViewPagerAdapter;
import com.hackthon.here.fragments.NotificationsFragment;
import com.hackthon.here.fragments.OrdersFragment;
import com.hackthon.here.fragments.PostsFragment;
import com.hackthon.here.fragments.ProfileFragment;
import com.hackthon.here.fragments.ReturnListFragment;
import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;

import java.io.File;

public class HomeActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private MenuItem prevMenuItem;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                        mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_profile:
                        mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                        mViewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_posts:
                        mViewPager.setCurrentItem(3);
                    return true;
                case R.id.navigation_return_list:
                        mViewPager.setCurrentItem(4);
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

        PostsFragment postsFragment = new PostsFragment();
        adapter.addFragment(postsFragment);

        ReturnListFragment returnListFragment = new ReturnListFragment();
        adapter.addFragment(returnListFragment);

        viewPager.setAdapter(adapter);
    }

}
