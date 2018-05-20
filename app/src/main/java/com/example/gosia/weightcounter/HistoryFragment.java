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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.fresco.processors.BlurPostprocessor;


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
        prepareBlurredImage(view);

        // define an adapter
        ListAdapter adapter = new ListAdapter(weightData, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void prepareWeightData() {
        weightData = SQLite.select().
                from(WeightData.class).queryList();

    }

    private void prepareBlurredImage(View view) {
        SimpleDraweeView simpleDraweeView = view.findViewById(R.id.blurredImage);

        Postprocessor postprocessor = new BlurPostprocessor(view.getContext());

        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithResourceId(R.drawable.running_black)
                .setPostprocessor(postprocessor)
                .build();

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(simpleDraweeView.getController())
                .build();

        simpleDraweeView.setController(controller);
    }
}
