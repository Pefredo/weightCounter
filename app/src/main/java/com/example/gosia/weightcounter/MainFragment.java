package com.example.gosia.weightcounter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.gosia.weightcounter.model.WeightData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

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

        createDiagram(view);
    }

    private void createDiagram(View v) {
        List<Entry> data = getSavedData();

        if (!data.isEmpty()) {

            LineDataSet dataDiagram = new LineDataSet(data, "");
            dataDiagram.setFillColor(Color.TRANSPARENT);

            //line
            dataDiagram.setColor(getResources().getColor(R.color.colorGold));
            dataDiagram.setLineWidth(2f);
            dataDiagram.setDrawCircleHole(false);
            dataDiagram.setValueTextSize(9f);
            dataDiagram.setDrawFilled(true);

            //disable values and circle
            dataDiagram.setDrawValues(true);
            dataDiagram.setValueTextColor(getResources().getColor(R.color.colorGold));

            dataDiagram.setDrawCircles(true);
            dataDiagram.setCircleColor(getResources().getColor(R.color.colorGold));
            //circle size
            dataDiagram.setCircleRadius(5f);


            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataDiagram); // add the datasets

            // set data
            LineChart mLineChart = v.findViewById(R.id.chart);


            //NOT WORKING :(
            mLineChart.setNoDataText(getResources().getString(R.string.no_text_data));
            mLineChart.setNoDataTextColor(getResources().getColor(R.color.colorGold));

            mLineChart.setTouchEnabled(true);


            LineData lineData = new LineData(dataSets);

            // remove axis
            YAxis leftAxis = mLineChart.getAxisLeft();
            leftAxis.setEnabled(false);
            YAxis rightAxis = mLineChart.getAxisRight();
            rightAxis.setEnabled(false);

            XAxis xAxis = mLineChart.getXAxis();
            xAxis.setEnabled(false);

            // hide legend
            Legend legend = mLineChart.getLegend();
            legend.setEnabled(false);

            //hide description
            Description description = new Description();
            description.setText("");
            mLineChart.setDescription(description);

            //disable touch gestures
            mLineChart.setTouchEnabled(false);

            //set smooth line
            //dataDiagram.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataDiagram.setMode(LineDataSet.Mode.LINEAR);

            mLineChart.setData(lineData);
            mLineChart.invalidate(); // refresh
        }
    }

    private List<Entry> getSavedData() {
        List<Entry> entryData = new ArrayList<>();

        List<WeightData> dataList = SQLite.select().
                from(WeightData.class).queryList();

        if (!dataList.isEmpty()) {
            for (int i = 0; i < dataList.size(); i++) {
                Logger.e("DAY NUMBER " + dataList.get(i).getDayNumber() + " WEIGHT " + dataList.get(i).getWeight());
                entryData.add(new Entry(dataList.get(i).getDayNumber(), Float.parseFloat(dataList.get(i).getWeight())));
            }
        }
        return entryData;
    }
}
