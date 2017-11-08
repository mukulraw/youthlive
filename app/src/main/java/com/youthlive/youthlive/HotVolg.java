package com.youthlive.youthlive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class HotVolg extends Fragment {

RecyclerView recyclerView;
GridLayoutManager manager;
HotAdapter adapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.hot , container , false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler);
        manager = new GridLayoutManager(getContext() , 2);
        adapter = new HotAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        return view;
    }
}
