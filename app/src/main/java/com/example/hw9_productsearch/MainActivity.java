package com.example.hw9_productsearch;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), 2);
        ViewPager mViewPager = findViewById(R.id.home_viewpage);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar toolbar = findViewById(R.id.toolbar);  // or however you need to do it for your code
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(0);  // clear all scroll flags

        createTabs();
    }

    private void createTabs() {
        final ViewPager homeviews = findViewById(R.id.home_viewpage);
        TabLayout hometabs = findViewById(R.id.home_tabs);

        TabLayout.Tab firstTab = hometabs.newTab();
        firstTab.setText("SEARCH");
        hometabs.addTab(firstTab);


        TabLayout.Tab secondTab = hometabs.newTab();
        secondTab.setText("WISHLIST");
        hometabs.addTab(secondTab);


        SectionsPagerAdapter adapt = new SectionsPagerAdapter(getSupportFragmentManager(), hometabs.getTabCount());
        homeviews.setAdapter(adapt);

        homeviews.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(hometabs));

        hometabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tabSelected)
            {
                homeviews.setCurrentItem(tabSelected.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tabSelected){}

            @Override
            public void onTabReselected(TabLayout.Tab tabSelected){

            }
        });
    }

    class SectionsPagerAdapter extends FragmentPagerAdapter {

        final int total__tabs;

        SectionsPagerAdapter(FragmentManager fm, int total__tabs) {
            super(fm);
            this.total__tabs = total__tabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SearchPage();
                case 1:
                    return new WishlistPage();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return total__tabs;
        }
    }
}
