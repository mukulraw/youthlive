package com.youthlive.youthlive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youthlive.youthlive.Activitys.CommentS;

/**
 * Created by TBX on 9/22/2017.
 */

public class Timeline extends Fragment {

    RecyclerView grid;
    RecyclerView grid2;
    LinearLayoutManager manager;
    LiveAdapter adapter;
    LiveAdapter2 adapter2;
    GridLayoutManager manager2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timeline , container , false);


        grid = (RecyclerView)view.findViewById(R.id.grid);
        grid2 = (RecyclerView)view.findViewById(R.id.grid2);

        manager = new LinearLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL , false);
        manager2 = new GridLayoutManager(getContext() , 1);

        adapter = new LiveAdapter(getContext());
        adapter2 = new LiveAdapter2(getContext());

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        grid2.setAdapter(adapter2);
        grid2.setLayoutManager(manager2);



        return view;
    }

    public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.ViewHolder>
    {

        Context context;

        public LiveAdapter(Context context)
        {
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.viewers_model , parent , false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 14;
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    public class LiveAdapter2 extends RecyclerView.Adapter<LiveAdapter2.ViewHolder>
    {

        Context context;

        public LiveAdapter2(Context context)
        {
            this.context = context;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.timeline_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 34;
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),CommentS.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }


}
