package com.youthlive.youthlive.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youthlive.youthlive.HotAdapter;
import com.youthlive.youthlive.R;

/**
 * Created by Designer 3 on 13-10-2017.
 */

public class MassageAdapter extends RecyclerView.Adapter<MassageAdapter.MsgViewHolder>{

    Context context;
    public MassageAdapter(Context context)
    {
        this.context = context;
    }



    @Override
    public MsgViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.messageformate , parent , false);
        return new MsgViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MsgViewHolder msgViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public  class MsgViewHolder extends RecyclerView.ViewHolder {
        public MsgViewHolder(View itemView) {
            super(itemView);
        }
    }
}
