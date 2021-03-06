package com.example.gosia.weightcounter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DB Flow
        FlowManager.init(new FlowConfig.Builder(this).build());

        //Fresco images
        Fresco.initialize(this);

        //set default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction()
                    .add(R.id.mainFragmentContainer, new MainFragment())
                    .commit();
        }
    }
}
