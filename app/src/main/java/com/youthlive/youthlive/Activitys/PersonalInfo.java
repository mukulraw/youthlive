package com.youthlive.youthlive.Activitys;

import android.app.Dialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.youthlive.youthlive.Adapter.Pager1Adapter;
import com.youthlive.youthlive.Adapter.PagerAdapter;
import com.youthlive.youthlive.R;
import com.youthlive.youthlive.Signin;

public class PersonalInfo extends AppCompatActivity {
    LinearLayout cancel_layout,cancel_ok;
    EditText title,dteducation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        FloatingActionButton floatingActionButton=findViewById(R.id.button_EditEducation);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(PersonalInfo.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.aducation_layout);
                cancel_layout=dialog.findViewById(R.id.cancel_layout);
                title=(EditText)dialog.findViewById(R.id.titleEducation);
                dteducation=(EditText)dialog.findViewById(R.id.Year_mention);
                cancel_ok=dialog.findViewById(R.id.txtok);
                cancel_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layoutpinfo);
        tabLayout.addTab(tabLayout.newTab().setText("Address"));
        tabLayout.addTab(tabLayout.newTab().setText("Education"));
        tabLayout.addTab(tabLayout.newTab().setText("Career"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager1);
        final Pager1Adapter adapter = new Pager1Adapter
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
