package com.youthlive.youthlive;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.PlayerState;
import com.yasic.bubbleview.BubbleView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayerActivity extends AppCompatActivity {

    RecyclerView grid;
    RecyclerView grid2;
    LinearLayoutManager manager;
    LiveAdapter adapter;
    LiveAdapter2 adapter2;
    GridLayoutManager manager2;
    ImageButton heart;
    private BubbleView bubbleView;
    ImageButton close;

    SurfaceView mVideoSurface;
    BroadcastPlayer mBroadcastPlayer;
    //TextView mPlayerStatusTextView;
    final OkHttpClient mOkHttpClient = new OkHttpClient();

    String uri;

    private static final String APPLICATION_ID = "9Nl68X4uVmi5mu5REY3SxA";
    private static final String API_KEY = "374rnkqn332isfldjc8a3oki8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        uri = getIntent().getStringExtra("uri");

        mVideoSurface = (SurfaceView) findViewById(R.id.PreviewSurfaceView);

        grid = (RecyclerView)findViewById(R.id.grid);
        grid2 = (RecyclerView)findViewById(R.id.grid2);
        manager = new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false);
        manager2 = new GridLayoutManager(this , 1);
        heart = (ImageButton)findViewById(R.id.heart);
        close = (ImageButton)findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mBroadcaster.stopBroadcast();
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

    BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState playerState) {
            Toast.makeText(PlayerActivity.this , playerState.toString() , Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onBroadcastLoaded(boolean live, int width, int height) {
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        mOkHttpClient.dispatcher().cancelAll();
        mVideoSurface = null;
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoSurface = (SurfaceView) findViewById(R.id.PreviewSurfaceView);
        //mPlayerStatusTextView.setText("Loading latest broadcast");
        //getLatestResourceUri();

        initPlayer(uri);

    }

    public void BlockPersson(View view) {
        PersonBlock();
    }
    private  void PersonBlock(){
        final Dialog dialog = new Dialog(PlayerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.blockpersom_dialog);
        dialog.show();
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

    /*void getLatestResourceUri() {
        Request request = new Request.Builder()
                .url("https://api.irisplatform.io/broadcasts")
                .addHeader("Accept", "application/vnd.bambuser.v1+json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .get()
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() { @Override public void run() {
                    if (mPlayerStatusTextView != null)
                        mPlayerStatusTextView.setText("Http exception: " + e);
                }});
            }
            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                String body = response.body().string();
                String resourceUri = null;
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray results = json.getJSONArray("results");
                    JSONObject latestBroadcast = results.optJSONObject(0);
                    resourceUri = latestBroadcast.optString("resourceUri");
                } catch (Exception ignored) {}
                final String uri = resourceUri;
                runOnUiThread(new Runnable() { @Override public void run() {
                    initPlayer(uri);
                }});
            }
        });
    }*/

    void initPlayer(String resourceUri) {
        if (resourceUri == null) {
            //if (mPlayerStatusTextView != null)
             //   mPlayerStatusTextView.setText("Could not get info about latest broadcast");
            return;
        }
        if (mVideoSurface == null) {
            // UI no longer active
            return;
        }

        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = new BroadcastPlayer(this, resourceUri, APPLICATION_ID, mBroadcastPlayerObserver);
        mBroadcastPlayer.setSurfaceView(mVideoSurface);
        mBroadcastPlayer.load();

    }




}
