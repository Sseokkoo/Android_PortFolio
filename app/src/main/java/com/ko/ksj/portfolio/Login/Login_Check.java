package com.ko.ksj.portfolio.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ko.ksj.portfolio.R;
import com.ko.ksj.portfolio.View.MainActivity;

public class Login_Check extends AppCompatActivity {

    String Email, Password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        SharedPreferences pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
        Email = pref.getString("email", "");
        Password = pref.getString("password","");
        if (!Email.contains("@") || !Email.contains(".")) {
            dialog.setMessage(getResources().getString(R.string.error_email_form));
            dialog.setPositiveButton("확인", null);
            AlertDialog alertDialog = dialog.create();
            alertDialog.setTitle(getResources().getString(R.string.common_email));
            alertDialog.show();
//        } else if (Email.equals()) {
//            dialog.setMessage(getResources().getString(R.string.error_email_use));
//            dialog.setPositiveButton("확인", null);
//            AlertDialog alertDialog = dialog.create();
//            alertDialog.setTitle(getResources().getString(R.string.common_email));
//            alertDialog.show();
        } else if (!Password.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*+#?&]{8,15}$")) {
            dialog.setMessage(getResources().getString(R.string.error_pass_form));
            dialog.setPositiveButton("확인", null);
            AlertDialog alertDialog = dialog.create();
            alertDialog.setTitle(getResources().getString(R.string.common_pw));
            alertDialog.show();
        } else if (Password.contains(" ") || Password.contains("")) {
            dialog.setMessage(getResources().getString(R.string.error_pass_no_empty));
            dialog.setPositiveButton("확인", null);
            AlertDialog alertDialog = dialog.create();
            alertDialog.setTitle(getResources().getString(R.string.common_pw));
            alertDialog.show();
        } else if (Email.contains(" ") || Email.contains("")) {
            dialog.setMessage(getResources().getString(R.string.error_pass_no_empty));
            dialog.setPositiveButton("확인", null);
            AlertDialog alertDialog = dialog.create();
            alertDialog.setTitle(getResources().getString(R.string.common_pw));
            alertDialog.show();
        } else {
            dialog.setMessage(getResources().getString(R.string.sign_success));
            dialog.setPositiveButton("확인", null);
            AlertDialog alertDialog = dialog.create();
            alertDialog.setTitle(getResources().getString(R.string.common_do_signup));
            alertDialog.show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
//        else {
//            dialog.setMessage("서버 오류입니다.\n보다 나은 서비스를 위해 문의 부탁드립니다.");
//            dialog.setPositiveButton("확인", null);
//            AlertDialog alertDialog = dialog.create();
//            alertDialog.setTitle("서버");
//            alertDialog.show();
//        }

    }
}
