package com.cuberto.flashytabbarsampleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;

import com.cuberto.flashytabbarandroid.TabFlashyAnimator;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> mFragmentList = new ArrayList<>();
    private TabFlashyAnimator tabFlashyAnimator;
    private String[] titles = new String[]{"Events", "Highlights", "Search", "Settings"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentList.add(new TabFragment(titles[0]));
        mFragmentList.add(new TabFragment(titles[1]));
        mFragmentList.add(new TabFragment(titles[2]));
        mFragmentList.add(new TabFragment(titles[3]));
        ViewPager viewPager = findViewById(R.id.view_pager);
        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        };
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabFlashyAnimator = new TabFlashyAnimator(tabLayout);
        tabFlashyAnimator.addTabItem(titles[0], R.drawable.ic_events);
        tabFlashyAnimator.addTabItem(titles[1], R.drawable.ic_highlights);
        tabFlashyAnimator.addTabItem(titles[2], R.drawable.ic_search);
        tabFlashyAnimator.addTabItem(titles[3], R.drawable.ic_settings, R.color.colorAccent, getResources().getDimension(R.dimen.big_text));
        tabFlashyAnimator.highLightTab(0);
        viewPager.addOnPageChangeListener(tabFlashyAnimator);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tabFlashyAnimator.setBadge(1, 2);
            }
        }, 1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tabFlashyAnimator.setBadge(20, 2);
            }
        }, 2000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tabFlashyAnimator.setBadge(200, 2);
            }
        }, 3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tabFlashyAnimator.onStart((TabLayout) findViewById(R.id.tabLayout));
    }

    @Override
    protected void onStop() {
        super.onStop();
        tabFlashyAnimator.onStop();
    }
}
