package com.example.moinitoring;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText join_email, join_password, join_name, join_pwck;
    private Button join_button, check_button, cancel_button;
    private AlertDialog dialog;
    private static boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //아이디값 찾아주기
        join_email = findViewById( R.id.join_email );
        join_password = findViewById( R.id.join_password );
        join_name = findViewById( R.id.join_name );
        join_pwck = findViewById(R.id.join_pwck);

        check_button = findViewById(R.id.check_button);
        join_button = findViewById(R.id.join_button);
        cancel_button = findViewById(R.id.delete);

        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValidateId validateId = new ValidateId();
                Map<String, String> params = new HashMap<String, String>();

                params.put("id",join_email.getText().toString());

                validateId.execute(params);
            }
        });

        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate)
                {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("id",join_email.getText().toString());
                    /*
                     * spring 서버 통신 시작*/
                    Register networkTask = new Register();

                    Log.d("parmas","id: "+join_email.getText().toString()+"  name: "+join_name.getText().toString()+ "   password: "+join_password.getText().toString());
                    params.put("name",join_name.getText().toString());
                    params.put("password",join_password.getText().toString());
                    Log.d("parmas","id: "+params.get("id")+" name: "+params.get("name")+" password: "+params.get("password"));
                    networkTask.execute(params);
                    successDialog();
                }
                else
                {
                    Log.d("Login","계정 중복체크 미완료");
                    Toast.makeText(RegisterActivity.this, "계정 중복체크를 해주세요.", Toast.LENGTH_LONG).show();
                }
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("move activity","to Main");
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                Log.d("move activity","to Main success");
            }
        });
    }

    public class ValidateId extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://10.0.2.2:5000/user/availableId");

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
            int cnt = Integer.parseInt(s);
            if(cnt==0)
            {
                Toast.makeText(RegisterActivity.this, "사용가능 계정입니다.", Toast.LENGTH_SHORT).show();
                validate = true;
            }
            else
                Toast.makeText(RegisterActivity.this, "이미 사용중인 계정입니다.", Toast.LENGTH_SHORT).show();
        }
    }


    public class Register extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://10.0.2.2:5000/user/register");

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

    void successDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(RegisterActivity.this)
                .setTitle("회원가입 완") .setMessage("로그인 창으로 이동하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("move activity","to RegisterPhone");
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        Log.d("move activity","to RegisterPhone success");                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("move activity","to Main");
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        Log.d("move activity","to Main success");
                        //Toast.makeText(LoginActivity.this, "메인으로 이동", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create(); msgDlg.show(); }
}