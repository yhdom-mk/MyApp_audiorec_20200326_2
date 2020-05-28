package com.example.myapp_audiorec_20200326_2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private ImageButton recordBtn;
    private ImageButton listBtn;
    private TextView filenameText;

    private boolean isRecording = false;
    private MediaRecorder mediaRecorder;
    private String recordFile;

    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE = 21;

    private Chronometer timer;


    public RecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        recordBtn = view.findViewById(R.id.record_btn);
        listBtn = view.findViewById(R.id.record_list_btn);
        filenameText = view.findViewById(R.id.record_file_name);
        timer = view.findViewById(R.id.record_timer);

        listBtn.setOnClickListener(this);
        recordBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_list_btn:
                if (isRecording){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            navController.navigate(R.id.action_recordFragment_to_audioListFragment);
                            isRecording = false;
                        }
                    });
                    alertDialog.setNegativeButton("CANCEL", null);
                    alertDialog.setTitle("AUDIO STILL RECORDING!");
                    alertDialog.setMessage("ARE YOU SURE,　YOU WANT TO STOP THE RECORDING?");
                    alertDialog.create().show();
                }else {
                    navController.navigate(R.id.action_recordFragment_to_audioListFragment);
                }
                break;

            case R.id.record_btn:
                if (isRecording) {
                    //Recording Stopped
                    stopRecording();

                    recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic_black, null));  //black color(at stopping)
                    isRecording = false;

                } else {
                    //Recording Start
                    if (checkPermissions()) {
                        startRecording();

                        recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic_off_blue, null));  //blue color(at recording)
                        isRecording = true;
                    }
                }
                break;
        }
    }
    private boolean checkPermissions () {
        if(ActivityCompat.checkSelfPermission(getContext(), recordPermission) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

    private void stopRecording() {
        timer.stop();

        filenameText.setText("RECODING STOPPED! FILE SAVED : " + recordFile);

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }


    private void startRecording () {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MMdd_hh:mm", Locale.JAPAN);
        Date now = new Date();
        recordFile = "Recording:" + formatter.format(now) +".3gp";

        filenameText.setText("RECODING!! FILENAME : " + recordFile);

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //start Recoding
        mediaRecorder.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isRecording) {
            stopRecording();
        }
    }
}
