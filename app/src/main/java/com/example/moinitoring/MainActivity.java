package com.example.moinitoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private static ArrayList<SMSData> SMSDataList;
    private static final String TAG = "MainActivity";
    private static final String SETTINGS_PLAYER_JSON = "settings_item_json";
    static Boolean imsi = false;
    Button resetButton, loginButton;
    ListView listView;
    SMSAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requirePerms();

        Log.d(TAG,"start");
        SMSDataList = new ArrayList<SMSData>();
        resetButton = (Button)findViewById(R.id.button);
        loginButton = (Button)findViewById(R.id.login_button);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPreferences(getApplicationContext(),SETTINGS_PLAYER_JSON);
                getStringArrayPref(getApplicationContext(),SETTINGS_PLAYER_JSON);
                SMSDataList.clear();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("move activity","to Login");
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                Log.d("move activity","to Login success");
            }
        });

        Intent intent = getIntent();
        processCommand(intent);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processCommand(intent);
    }

    private void processCommand(Intent intent){
        SMSDataList.clear();
        getStringArrayPref(this,SETTINGS_PLAYER_JSON);
        if(intent != null){
            String sender = intent.getStringExtra("sender");
            String date = intent.getStringExtra("date");
            String content = intent.getStringExtra("content");
            Log.d(TAG,"sender: "+sender);
            Log.d(TAG,"date: "+date);
            Log.d(TAG,"content: "+content);

            SMSDataList.add(new SMSData(date,sender, content,R.drawable.ic_launcher_background));
            setStringArrayPref(this,SETTINGS_PLAYER_JSON,SMSDataList);
        }
    }

    private void setStringArrayPref(Context context, String key, ArrayList<SMSData> values) {
        Log.d(TAG,"Json start");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();

        for(int i =0;i<values.size();i++) {

            JSONObject jsonObject = new JSONObject();
            try {
                Log.d(TAG,"setStringJson start!"+i);
                Log.d(TAG,"sender: "+values.get(i).getSendNumber());
                Log.d(TAG,"date: "+values.get(i).getDate());
                Log.d(TAG,"content: "+values.get(i).getContent());
                jsonObject.put("sender", values.get(i).getSendNumber());
                jsonObject.put("date", values.get(i).getDate());
                jsonObject.put("content", values.get(i).getContent());
                jsonObject.put("warning", values.get(i).getWarning());
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            a.put(jsonObject);
        }
//        for (int i = 0; i < values.size(); i++) {
//            a.put(values.get(i));
//        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        Log.d(TAG,"toJson: "+a.toString());
        editor.apply();
    }

    private void getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<SMSData> urls = new ArrayList<SMSData>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    JSONObject url = a.getJSONObject(i);

                    Log.d(TAG,"getStringJson start!");
                    Log.d(TAG,"sender: "+url.getString("sender"));
                    Log.d(TAG,"date: "+url.getString("date"));
                    Log.d(TAG,"content: "+url.getString("content"));
                    SMSDataList.add(new SMSData(url.getString("date"),url.getString("sender"),url.getString("content"),url.getInt("warning")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        listView = (ListView)findViewById(R.id.listView);
        myAdapter = new SMSAdapter(this,SMSDataList);

        listView.setAdapter(myAdapter);
    }

    private void resetPreferences(Context context, String key){
        Log.d(TAG,"memory reset start");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        editor.putString(key, null);
        editor.apply();
    }

    private void requirePerms(){
        String[] permissions = {Manifest.permission.RECEIVE_SMS};
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    public class NetworkTask2 extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

        // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://10.0.2.2:5000/allUser");

        // Parameter 를 전송한다.
            http.addAllParameters(maps[0]);


        //Http 요청 전송
            HttpClient post = http.create();
            post.request();

        // 응답 상태코드 가져오기
            int statusCode = post.getHttpStatusCode();

        // 응답 본문 가져오기
            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("JSON_RESULT: ",s);
        }
    }

}