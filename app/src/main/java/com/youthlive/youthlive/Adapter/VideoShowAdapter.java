package com.youthlive.youthlive.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.youthlive.youthlive.R;

/**
 * Created by Designer 3 on 26-10-2017.
 */

public class VideoShowAdapter extends RecyclerView.Adapter<VideoShowAdapter.VideoHolder> {
    Context context;

    public VideoShowAdapter( Context context)
    {
        this.context=context;
    }


    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_recyclerview_video , parent , false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoHolder videoHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 9;
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        public VideoHolder(View itemView) {
            super(itemView);
        }
    }

}
