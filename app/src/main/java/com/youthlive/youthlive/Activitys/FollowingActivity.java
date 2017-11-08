package com.youthlive.youthlive.Activitys;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.youthlive.youthlive.Adapter.following_adapter;
import com.youthlive.youthlive.Fragments.FollowingFragment;
import com.youthlive.youthlive.LiveScreen;
import com.youthlive.youthlive.R;

public class FollowingActivity extends AppCompatActivity {
    RecyclerView recycler_following;
    following_adapter recAdapter;
    LinearLayoutManager layoutmanager;
    ImageView notification_drawer;
    public FrameLayout fl_toplayoutt;
    ImageView backpress_flow;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        recycler_following = (RecyclerView)findViewById(R.id.recycler_following);
        layoutmanager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false);
        recAdapter = new following_adapter(this);
        recycler_following.setLayoutManager(layoutmanager);
        recycler_following.setAdapter(recAdapter);
        recycler_following.setHasFixedSize(true);
        backpress_flow=findViewById(R.id.backpress_flow);
        backpress_flow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fl_toplayoutt= (FrameLayout) findViewById(R.id.fl_toplayoutt);
        fl_toplayoutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl_toplayoutt.setVisibility(View.GONE);

            }
        });

        notification_drawer=findViewById(R.id.notification_drawer);
        notification_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl_toplayoutt.setVisibility(View.VISIBLE);
                android.support.v4.app.FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
                FollowingFragment fragmentHome=new FollowingFragment();
                fm.replace(R.id.fl_toplayoutt,fragmentHome);
                fm.commit();

            }
        });
    }
}
