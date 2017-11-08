package com.youthlive.youthlive.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youthlive.youthlive.Adapter.VideoShowAdapter;
import com.youthlive.youthlive.R;
import com.youthlive.youthlive.RecyclerviewItemspace.GridSpacingItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class VlogFragment extends Fragment {
    RecyclerView Vlog;
    VideoShowAdapter adapterVlog;
    GridLayoutManager linearLayoutManagervlog;
    public VlogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_vlog, container, false);
        Vlog = (RecyclerView) view.findViewById(R.id.video);
        int spanCount = 2; // 2 columns
        int spacing = 3; // 3px
        boolean includeEdge = true;
        Vlog.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        adapterVlog = new VideoShowAdapter(getContext());
        Vlog.setAdapter(adapterVlog);
        Vlog.setLayoutManager(mLayoutManager);
        return  view;
    }

}
