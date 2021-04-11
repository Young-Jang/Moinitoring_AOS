package com.example.moinitoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static ArrayList<SMSData> SMSDataList = new ArrayList<SMSData>();
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requirePerms();

        Intent intent = getIntent();
        processCommand(intent);

        ListView listView = (ListView)findViewById(R.id.listView);
        final SMSAdapter myAdapter = new SMSAdapter(this,SMSDataList);

        listView.setAdapter(myAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processCommand(intent);
    }

    private void processCommand(Intent intent){

        if(intent != null){
            String sender = intent.getStringExtra("sender");
            String date = intent.getStringExtra("date");
            String content = intent.getStringExtra("content");
            Log.d(TAG,"sender: "+sender);
            Log.d(TAG,"date: "+date);
            Log.d(TAG,"content: "+content);

            SMSDataList.add(new SMSData(date,sender, content,R.drawable.ic_launcher_background));
        }
    }

    private void requirePerms(){
        String[] permissions = {Manifest.permission.RECEIVE_SMS};
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }
}