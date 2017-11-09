package com.youthlive.youthlive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.singleVideoPOJO.Comment;
import com.youthlive.youthlive.singleVideoPOJO.singleVideoBean;
import com.youthlive.youthlive.vlogListPOJO.vlogListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SingleVideoActivity extends AppCompatActivity {

    Toolbar toolbar;
    CircleImageView profile;
    TextView name , time , views , comments , likes;
    VideoView video;
    RecyclerView grid;
    GridLayoutManager manager;
    EditText comment;
    CommentsAdapter adapter;
    List<Comment> list;
    ProgressBar progress;
    String videoId , url;
    ImageButton send;
    ProgressBar videoProgress;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_video);

        videoId = getIntent().getStringExtra("videoId");
        url = getIntent().getStringExtra("url");

        list = new ArrayList<>();

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        progress = (ProgressBar)findViewById(R.id.progress);
        videoProgress = (ProgressBar)findViewById(R.id.video_progress);

        DoubleBounce doubleBounce = new DoubleBounce();
        videoProgress.setIndeterminateDrawable(doubleBounce);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });


        profile = (CircleImageView)findViewById(R.id.profile);
        name = (TextView)findViewById(R.id.name);

        time = (TextView)findViewById(R.id.time);
        views = (TextView)findViewById(R.id.views);
        comments = (TextView)findViewById(R.id.comments);
        likes = (TextView)findViewById(R.id.like);
        comment = (EditText)findViewById(R.id.comment);
        video = (VideoView)findViewById(R.id.video);
        grid = (RecyclerView)findViewById(R.id.grid);
        manager = new GridLayoutManager(this , 1);
        adapter = new CommentsAdapter(this , list);

        grid.setLayoutManager(manager);
        grid.setAdapter(adapter);


        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                progress.setVisibility(View.VISIBLE);


                final bean b = (bean) getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<singleVideoBean> call = cr.getsingleVideo(b.userId , videoId);

                call.enqueue(new Callback<singleVideoBean>() {
                    @Override
                    public void onResponse(Call<singleVideoBean> call, Response<singleVideoBean> response) {



                        Call<singleVideoBean> call1 = cr.getsingleVideo(b.userId , videoId);

                        call1.enqueue(new Callback<singleVideoBean>() {
                            @Override
                            public void onResponse(Call<singleVideoBean> call, Response<singleVideoBean> response) {



                                views.setText(response.body().getData().getViewsCount());
                                likes.setText(response.body().getData().getLikesCount());
                                comments.setText(response.body().getData().getCommentCount());

                                progress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<singleVideoBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });


                    }

                    @Override
                    public void onFailure(Call<singleVideoBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });




            }
        });


        comment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getRawX() >= (comment.getRight() - comment.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()) - 60)
                {
                    // your action here

                    //og.d("asdasd" , "clicked");



                    String mess = comment.getText().toString();

                    if (mess.length() > 0)
                    {
                        progress.setVisibility(View.VISIBLE);

                        final bean b = (bean) getApplicationContext();

                        final Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.BASE_URL)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        final AllAPIs cr = retrofit.create(AllAPIs.class);


                        Call<vlogListBean> call = cr.comment(b.userId , videoId , mess);

                        call.enqueue(new Callback<vlogListBean>() {
                            @Override
                            public void onResponse(Call<vlogListBean> call, Response<vlogListBean> response) {


                                if (Objects.equals(response.body().getMessage(), "Video Comment Success"))
                                {
                                    comment.setText("");
                                }

                                progress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<vlogListBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });
                    }


                    return true;
                }
                return false;
            }
        });



        /*send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String mess = comment.getText().toString();

                if (mess.length() > 0)
                {
                    progress.setVisibility(View.VISIBLE);

                    final bean b = (bean) getApplicationContext();

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                    Call<vlogListBean> call = cr.comment(b.userId , videoId , mess);

                    call.enqueue(new Callback<vlogListBean>() {
                        @Override
                        public void onResponse(Call<vlogListBean> call, Response<vlogListBean> response) {

                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<vlogListBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });
                }

            }
        });*/


        videoProgress.setVisibility(View.VISIBLE);


        Uri uri = Uri.parse(url);
        video.setVideoURI(uri);
        video.start();

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //close the progress dialog when buffering is done
                videoProgress.setVisibility(View.GONE);
                mp.setLooping(true);
            }
        });





    }


    @Override
    protected void onResume() {
        super.onResume();


        progress.setVisibility(View.VISIBLE);


        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<singleVideoBean> call = cr.getsingleVideo(b.userId , videoId);

        call.enqueue(new Callback<singleVideoBean>() {
            @Override
            public void onResponse(Call<singleVideoBean> call, Response<singleVideoBean> response) {

                try {
                    if (response.body().getData().getComments()!=null)
                    {
                        adapter.setGridData(response.body().getData().getComments());
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);


                views.setText(response.body().getData().getViewsCount());
                likes.setText(response.body().getData().getLikesCount());
                comments.setText(response.body().getData().getCommentCount());


                ImageLoader loader = ImageLoader.getInstance();
                loader.displayImage(response.body().getData().getTimelineProfileImage() , profile);

                name.setText(response.body().getData().getTimelineName());

                time.setText(response.body().getData().getUploadTime());

                schedule();

            }

            @Override
            public void onFailure(Call<singleVideoBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }


    public void schedule()
    {

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                final bean b = (bean) getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<singleVideoBean> call = cr.getsingleVideo(b.userId , videoId);

                call.enqueue(new Callback<singleVideoBean>() {
                    @Override
                    public void onResponse(Call<singleVideoBean> call, Response<singleVideoBean> response) {


                        try {
                            if (response.body().getData().getComments()!=null)
                            {
                                adapter.setGridData(response.body().getData().getComments());
                            }

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        views.setText(response.body().getData().getViewsCount());
                        likes.setText(response.body().getData().getLikesCount());
                        comments.setText(response.body().getData().getCommentCount());


                    }

                    @Override
                    public void onFailure(Call<singleVideoBean> call, Throwable t) {

                    }
                });

            }
        } , 0 , 1000);

    }


    public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>
    {

        List<Comment> list = new ArrayList<>();
        Context context;

        public CommentsAdapter(Context context , List<Comment> list)
        {
            this.context = context;
            this.list = list;
        }


        public void setGridData(List<Comment> list)
        {
            this.list = list;
            notifyDataSetChanged();
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.comment_list_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Comment item = list.get(position);

            holder.message.setText(item.getComment());
            holder.time.setText(item.getTime());

        }

        @Override
        public int getItemCount() {
            //return list.size();
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {

            TextView message , time;

            public ViewHolder(View itemView) {
                super(itemView);

                message = (TextView)itemView.findViewById(R.id.message);
                time = (TextView)itemView.findViewById(R.id.time);

            }
        }

    }


}
