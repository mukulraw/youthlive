package com.youthlive.youthlive;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yasic.bubbleview.BubbleView;

import java.util.ArrayList;
import java.util.List;

public class LiveScreen extends AppCompatActivity {

    RecyclerView grid;
    RecyclerView grid2;
    LinearLayoutManager manager;
    LiveAdapter adapter;
    LiveAdapter2 adapter2;
    GridLayoutManager manager2;
    ImageButton heart;
    private BubbleView bubbleView;
    ImageButton close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_screen);
        grid = (RecyclerView)findViewById(R.id.grid);
        grid2 = (RecyclerView)findViewById(R.id.grid2);
        manager = new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false);
        manager2 = new GridLayoutManager(this , 1);
        heart = (ImageButton)findViewById(R.id.heart);
        close = (ImageButton)findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());

            }
        });

        adapter = new LiveAdapter(this);
        adapter2 = new LiveAdapter2(this);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        grid2.setAdapter(adapter2);
        grid2.setLayoutManager(manager2);


        bubbleView = (BubbleView) findViewById(R.id.bubble);
        List<Drawable> drawableList = new ArrayList<>();
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_indigo_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_deep_purple_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_cyan_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_blue_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_deep_purple_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_light_blue_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_lime_a200_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_pink_900_24dp));
        drawableList.add(getResources().getDrawable(R.drawable.ic_favorite_red_900_24dp));
        bubbleView.setDrawableList(drawableList);


    }

    public void BlockPersson(View view) {
        PersonBlock();
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
            View view = inflater.inflate(R.layout.chat_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.index.setText(String.valueOf(position + 1));

        }

        @Override
        public int getItemCount() {
            return 34;
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView index;

            public ViewHolder(View itemView) {
                super(itemView);

                index = (TextView)itemView.findViewById(R.id.index);

            }
        }
    }
    private  void PersonBlock(){
        final Dialog dialog = new Dialog(LiveScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.blockpersom_dialog);
        dialog.show();
    }

}
