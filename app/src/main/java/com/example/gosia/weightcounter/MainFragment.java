package com.example.gosia.weightcounter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, viewGroup, false);

        Button buttonStart = view.findViewById(R.id.button_start);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraFragment fragmentCamera = new CameraFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.mainFragmentContainer, fragmentCamera)
                        .addToBackStack(null)
                        .commit();
            }
        });

        LinearLayout chartLayout = view.findViewById(R.id.chart_layout);
        chartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryFragment fragmentHistory = new HistoryFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainFragmentContainer, fragmentHistory)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //createDiagram();

    }
}
