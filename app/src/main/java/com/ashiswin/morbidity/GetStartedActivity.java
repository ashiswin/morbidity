package com.ashiswin.morbidity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.ashiswin.morbidity.settingsfragments.BirthdayFragment;
import com.ashiswin.morbidity.settingsfragments.DietFragment;
import com.ashiswin.morbidity.settingsfragments.NameFragment;
import com.ashiswin.morbidity.settingsfragments.SexFragment;
import com.ashiswin.morbidity.settingsfragments.WorkoutFragment;
import com.ashiswin.morbidity.utils.LayoutUtils;

public class GetStartedActivity extends AppCompatActivity {
    ViewPager pgrSettings;
    TabLayout lytDots;
    PagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        setToolbar();

        pgrSettings = findViewById(R.id.pgrSettings);
        lytDots = findViewById(R.id.lytDots);

        adapter = new PagerAdapter(getSupportFragmentManager());

        pgrSettings.setAdapter(adapter);
        pgrSettings.setPageMargin(LayoutUtils.convertDip2Pixels(GetStartedActivity.this, 16));
        pgrSettings.setOffscreenPageLimit(3);
        lytDots.setupWithViewPager(pgrSettings);
    }

    public void nextFragment(int currentFragment) {
        TabLayout.Tab tab = lytDots.getTabAt(currentFragment + 1);
        tab.select();

        if(currentFragment == 0) {
            String name = ((NameFragment) adapter.fragments[0]).name;
            ((BirthdayFragment) adapter.fragments[1]).setName(name);
            ((SexFragment) adapter.fragments[2]).setName(name);
            ((DietFragment) adapter.fragments[3]).setName(name);
            ((WorkoutFragment) adapter.fragments[4]).setName(name);
        }
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        title.setText(R.string.get_started_title);
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        public Fragment[] fragments;

        public PagerAdapter(FragmentManager fm) {
            super(fm);

            fragments = new Fragment[5];

            fragments[0] = new NameFragment();
            fragments[1] = new BirthdayFragment();
            fragments[2] = new SexFragment();
            fragments[3] = new DietFragment();
            fragments[4] = new WorkoutFragment();
        }

        @Override
        public Fragment getItem(int pos) {
            return fragments[pos];
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
