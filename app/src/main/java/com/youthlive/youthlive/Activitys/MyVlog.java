package com.youthlive.youthlive.Activitys;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.youthlive.youthlive.Adapter.Pager2Adapter;
import com.youthlive.youthlive.Adapter.PagerAdapter;
import com.youthlive.youthlive.R;

public class MyVlog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vlog);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layoutvlog);
        tabLayout.addTab(tabLayout.newTab().setText("Vlog"));
        tabLayout.addTab(tabLayout.newTab().setText("Message"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pagervlog);
        final Pager2Adapter adapter = new Pager2Adapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
