package com.youthlive.youthlive.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youthlive.youthlive.R;

/**
 * Created by Designer 3 on 14-10-2017.
 */

public class following_adapter extends RecyclerView.Adapter<following_adapter.followingadapter>{

    Context context;
    public following_adapter(Context context)
    {

        this.context=context;
    }


    @Override
    public followingadapter onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.following_recycler , parent , false);
        return new followingadapter(view);
    }
    @Override
    public void onBindViewHolder(followingadapter followingadapter, int position) {

    }

    @Override
    public int getItemCount() {
        return 14;
    }

    public class followingadapter extends RecyclerView.ViewHolder{


    public followingadapter(View itemView) {
        super(itemView);
    }
}
}
