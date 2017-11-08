package com.youthlive.youthlive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.getLivePOJO.Result;
import com.youthlive.youthlive.getLivePOJO.getLiveBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class Live extends Fragment {

    RecyclerView grid;
    GridLayoutManager manager;
    LiveAdapter adapter;
    ProgressBar progress;
    List<Result> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_layout , container , false);

        list = new ArrayList<>();
        grid = (RecyclerView)view.findViewById(R.id.grid);
        manager = new GridLayoutManager(getContext() , 2);

        progress = (ProgressBar)view.findViewById(R.id.progress);


        adapter = new LiveAdapter(getContext() , list);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getContext().getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.irisplatform.io/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<getLiveBean> call = cr.getLiveList();

        call.enqueue(new Callback<getLiveBean>() {
            @Override
            public void onResponse(Call<getLiveBean> call, Response<getLiveBean> response) {

                list.clear();

                for (int i = 0 ; i < response.body().getResults().size() ; i++)
                {
                    if (Objects.equals(response.body().getResults().get(i).getType(), "live"))
                    {
                        list.add(response.body().getResults().get(i));
                    }
                }

                adapter.setGridData(list);
                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<getLiveBean> call, Throwable throwable) {
                progress.setVisibility(View.GONE);
            }
        });


    }

    public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.ViewHolder>
    {

        Context context;
        List<Result> list = new ArrayList<>();

        public LiveAdapter(Context context , List<Result> list)
        {
            this.context = context;
            this.list = list;
        }

        public void setGridData(List<Result> list)
        {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.live_list_model , parent , false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            final Result item = list.get(position);

            holder.title.setText(item.getTitle());

            ImageLoader loader = ImageLoader.getInstance();

            loader.displayImage(item.getAuthor() , holder.image);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context , PlayerActivity.class);
                    intent.putExtra("uri" , item.getResourceUri());
                    startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            ImageView image;
            TextView title;

            public ViewHolder(View itemView) {
                super(itemView);

                image = (ImageView)itemView.findViewById(R.id.image);
                title = (TextView) itemView.findViewById(R.id.title);

            }
        }
    }

}
