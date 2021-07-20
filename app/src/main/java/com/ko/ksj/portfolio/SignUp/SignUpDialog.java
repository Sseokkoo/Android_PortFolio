package com.ko.ksj.portfolio.SignUp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ko.ksj.portfolio.Google.Google_Init;
import com.ko.ksj.portfolio.Kakao.Kakao_Init;
import com.ko.ksj.portfolio.Login;
import com.ko.ksj.portfolio.R;

public class SignUpDialog extends AppCompatActivity {
    TextView email, naver, google, kakao, cancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_dialog);

        email = findViewById(R.id.SignIn_ActEmail);
        naver = findViewById(R.id.SignIn_ActNaver);
        google = findViewById(R.id.SignIn_ActGoogle);
        kakao = findViewById(R.id.SignIn_ActKakao);
        cancle = findViewById(R.id.SignIn_ActCancle);

        email.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            SharedPreferences pref = getSharedPreferences("UserInfo",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("login_type","default");
            editor.commit();
            startActivity(intent);
            finish();
        });
        naver.setOnClickListener(v -> {
            Toast.makeText(this, getResources().getString(R.string.Naver_Sign), Toast.LENGTH_SHORT).show();
            Login login = new Login();
            login.Naver_start();
            finish();
        });
        google.setOnClickListener(v -> {
            Intent intent = new Intent(this, Google_Init.class);
            startActivity(intent);
            finish();
        });
        kakao.setOnClickListener(v -> {
            Toast.makeText(this, getResources().getString(R.string.Kakao_Sign), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Kakao_Init.class);
            startActivity(intent);
            finish();
        });
        cancle.setOnClickListener(v -> {
            finish();
        });
    }
    //매니패스트 스타일 적용
}