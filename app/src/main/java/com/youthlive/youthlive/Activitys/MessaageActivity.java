package com.youthlive.youthlive.Activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.youthlive.youthlive.Adapter.MassageAdapter;
import com.youthlive.youthlive.HotAdapter;
import com.youthlive.youthlive.MainActivity;
import com.youthlive.youthlive.R;

public class MessaageActivity extends AppCompatActivity {
    MassageAdapter holder;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaage);
        recyclerView = (RecyclerView)findViewById(R.id.messagerecycler);
       linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        holder=new MassageAdapter(this);
        recyclerView.setAdapter(holder);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    public void BackPress(View view) {
    finish();
    }
}
