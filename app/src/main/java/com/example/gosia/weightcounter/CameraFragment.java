package com.example.gosia.weightcounter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gosia.weightcounter.model.WeightData;
import com.example.gosia.weightcounter.util.DialogCreator;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CameraFragment extends Fragment {

    private final int REQUEST_CAMERA_PERMISSION_ID = 1001;
    private CameraSource cameraSource;
    private SurfaceView cameraView;
    private TextView textWeight;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION_ID:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        Log.e("CameraFragment", e.getLocalizedMessage(), e);
                    }
                }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, viewGroup, false);


        startCamera(view);
        buttonsOnClick(view);

        return view;
    }

    private void startCamera(View view) {
        cameraView = view.findViewById(R.id.surface_view);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity().getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Logger.e(this.getClass().getSimpleName(), "Detector dependencies aren't available yet");
        } else {
            cameraSource = new CameraSource.Builder(getActivity().getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();

            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {

                    try {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA_PERMISSION_ID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();

                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {

                    final SparseArray<TextBlock> items = detections.getDetectedItems();

                    if (items.size() != 0) {

                        textWeight = getView().findViewById(R.id.text_weight);
                        textWeight.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); ++i) {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                textWeight.setText(stringBuilder.toString());
                            }
                        });
                    }
                }
            });
        }
    }


    private void buttonsOnClick(final View v) {

        final Button typeButton = v.findViewById(R.id.button_type);
        final Button saveButton = v.findViewById(R.id.button_save);
        final EditText weightEditText = v.findViewById(R.id.edit_text_weight);
        textWeight = v.findViewById(R.id.text_weight);


        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (weightEditText.getVisibility() == View.VISIBLE) {
                    weightEditText.setVisibility(View.GONE);
                    typeButton.setVisibility(View.VISIBLE);
                } else {
                    weightEditText.setVisibility(View.VISIBLE);
                    typeButton.setVisibility(View.GONE);
                    weightEditText.requestFocus();
                    // Show soft keyboard for the user to enter the value.
                    if (getActivity() != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.showSoftInput(weightEditText, InputMethodManager.SHOW_IMPLICIT);
                        }
                    }
                }
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String weightType = weightEditText.getText().toString();
                String weightCamera = textWeight.getText().toString();

                if (weightType.length() != 0 && isNumeric(weightType)) {
                    Logger.d("TYPE " + weightType);
                    saveWeightValue(weightType, v);
                } else if (weightCamera.length() != 0 && weightType.length() == 0 && isNumeric(weightCamera)) {
                    Logger.d("CAMERA " + weightCamera);
                    saveWeightValue(weightCamera, v);
                }
            }
        });
    }

    private static boolean isNumeric(String str) {
        try {
            double num = Double.parseDouble(str);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    private void saveWeightValue(String value, View v) {
        String actualDate = getActualDate();
        List<WeightData> data = SQLite.select().from(WeightData.class).queryList();
        final EditText weightEditText = v.findViewById(R.id.edit_text_weight);
        final Button typeButton = v.findViewById(R.id.button_type);


        int lastDayNumber;

        if (data.size() == 0) {
            lastDayNumber = 1;
        } else {
            String previousDate = data.get(data.size() - 1).getLastDayWeightMeasurement();
            int previousDayNumber = data.get(data.size() - 1).getDayNumber();

            int differenceBetweenDays = calculateDifferenceBetweenDates(actualDate, previousDate);
            lastDayNumber = previousDayNumber + differenceBetweenDays;
        }

        WeightData weightData = new WeightData();
        weightData.setLastDayWeightMeasurement(actualDate);
        weightData.setWeight(value);
        weightData.setDayNumber(lastDayNumber);

        if (!data.isEmpty()) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getLastDayWeightMeasurement().equals(actualDate)) {
                    DialogCreator.getInstance().showErrorDialog(getContext(), R.string.saved_data_already);
                    break;
                } else {
                    weightData.save();
                    DialogCreator.getInstance().showDialog(getContext(), R.string.saved, R.string.added);
                }
            }
        } else {
            weightData.save();
            DialogCreator.getInstance().showDialog(getContext(), R.string.saved, R.string.added);
        }

        weightEditText.setText("");
        weightEditText.setVisibility(View.GONE);
        typeButton.setVisibility(View.VISIBLE);
    }

    private String getActualDate() {
        DateFormat df = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        return df.format(Calendar.getInstance().getTime());
    }

    private int calculateDifferenceBetweenDates(String stringActualDate, String stringLastDate) {
        DateFormat df = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        Date actualDate = null;
        Date lastDate = null;
        long differenceBetweenDays = -1;

        try {
            actualDate = df.parse(stringActualDate);
            lastDate = df.parse(stringLastDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if ((actualDate != null) && (lastDate != null)) {
            differenceBetweenDays = ((((((actualDate.getTime() - lastDate.getTime()) / 1000) / 60)) / 60) / 24);
        }
        return (int) differenceBetweenDays;
    }
}
