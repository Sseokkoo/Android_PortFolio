package com.ko.ksj.portfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    String Email, Password,EDTId, EDTPassword;
    EditText Login_EDTId, Login_EDTPassword;
    Button Login_BTNLogin, Login_BTNPassReset, Login_BTNSignIn;
    LinearLayout Login_BTNNaver, Login_BTNGoogle, Login_BTNKakao;
    CheckBox Login_AutoCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        pref.getString("email", Email);
        pref.getString("password", Password);

        Login_EDTId = findViewById(R.id.Login_EDTId);
        Login_EDTPassword = findViewById(R.id.Login_EDTPassword);
        Login_BTNLogin = findViewById(R.id.Login_BTNLogin);
        Login_AutoCheck = findViewById(R.id.Login_AutoCheck);

        Login_BTNPassReset = findViewById(R.id.Login_BTNPassReset);
        Login_BTNSignIn = findViewById(R.id.Login_BTNSignIn);

        Login_BTNNaver = findViewById(R.id.Login_BTNNaver);
        Login_BTNGoogle = findViewById(R.id.Login_BTNGoogle);
        Login_BTNKakao = findViewById(R.id.Login_BTNKakao);


        Login_BTNLogin.setOnClickListener(v -> {
            EDTId = Login_EDTId.getText().toString();
            EDTPassword = Login_EDTPassword.getText().toString();

            if (EDTId.equals(Email) && EDTPassword.equals(Password)){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
