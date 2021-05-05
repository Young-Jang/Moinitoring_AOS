package com.example.moinitoring;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText join_email, join_password, join_name, join_pwck;
    private Button join_button, check_button;
    private AlertDialog dialog;
    private boolean validate = false;

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


        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * spring 서버 통신 시작*/
                ValidateId networkTask = new ValidateId();

                Map<String, String> params = new HashMap<String, String>();

                Log.d("parmas","id: "+join_email.getText().toString()+"  name: "+join_name.getText().toString()+ "   password: "+join_password.getText().toString());
                params.put("id",join_email.getText().toString());
                params.put("name",join_name.getText().toString());
                params.put("password",join_password.getText().toString());
                Log.d("parmas","id: "+params.get("id")+" name: "+params.get("name")+" password: "+params.get("password"));
                networkTask.execute(params);
            }
        });
    }

    public class ValidateId extends AsyncTask<Map<String, String>, Integer, String> {
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
}