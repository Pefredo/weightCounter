package com.example.gosia.weightcounter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gosia.weightcounter.model.WeightData;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    private List<WeightData> weightData = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, viewGroup, false);


        //recycler
        RecyclerView recyclerView = view.findViewById(R.id.history_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        prepareWeightData();

        // define an adapter
        ListAdapter adapter = new ListAdapter(weightData, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void prepareWeightData() {
        weightData = SQLite.select().
                from(WeightData.class).queryList();

        Logger.d(weightData.toString());

    }
}
