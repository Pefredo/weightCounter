package com.example.gosia.weightcounter;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = WeightDatabase.NAME, version = WeightDatabase.VERSION)
public class WeightDatabase {

    public static final String NAME = "WeightDataBase";

    //update when change table
    public static final int VERSION = 1;
}