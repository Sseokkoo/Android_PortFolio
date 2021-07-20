package com.ko.ksj.portfolio.SignUp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ko.ksj.portfolio.Login;
import com.ko.ksj.portfolio.MainActivity;
import com.ko.ksj.portfolio.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Parameter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpActivity extends AppCompatActivity {

    String[] value = {"login_type", "email", "name", "id", "password"};

    EditText edit_name, edit_email, edit_pass, edit_pass_ok;

    ImageView back;
    Button submit;
    SharedPreferences pref;
    String logintype, email, name, pass, passok;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edit_name = (EditText) findViewById(R.id.SignIn_EDTName);
        edit_email = (EditText) findViewById(R.id.SignIn_EDTEmail);
        edit_pass = (EditText) findViewById(R.id.SignIn_EDTPass);
        edit_pass_ok = (EditText) findViewById(R.id.SignIn_EDTPassOk);
        back = findViewById(R.id.SignIn_IVBack);

        back.setOnClickListener(v -> {
            finish();
        });

        pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
        email = pref.getString("email", "");
        logintype = pref.getString("login_type","");

        if (!logintype.equals("default")) {
            edit_email.setVisibility(View.GONE);
            edit_name.setVisibility(View.GONE);
        }
        submit = (Button) findViewById(R.id.Sign_BTNSignIn);
        submit.setOnClickListener(v -> {

            if (logintype.equals("default")) {
                name = edit_name.getText().toString();
                email = edit_email.getText().toString();
                pass = edit_pass.getText().toString();
                passok = edit_pass_ok.getText().toString();

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                Log.e(getApplicationContext().toString(), edit_name.getText().toString());
                if (name.equals("") || name == null) {
                    dialog.setMessage(getResources().getString(R.string.NickName_Enter));
                    dialog.setPositiveButton(getResources().getString(R.string.confirm), null);
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.setTitle(getResources().getString(R.string.confirm));
                    alertDialog.show();
                } else if (edit_pass.getVisibility() == View.VISIBLE && !pass.equals(passok) && pass != null) {
                    dialog.setMessage(getResources().getString(R.string.Password_Enter));
                    dialog.setPositiveButton(getResources().getString(R.string.confirm), null);
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.setTitle(getResources().getString(R.string.pw));
                    alertDialog.show();

                } else {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("name",edit_name.toString());
                    editor.putString("email",edit_email.toString());
                    editor.putString("password", edit_pass.toString());
                    Intent intent = new Intent(this, SignUp_Check.class);
                    startActivity(intent);
                }
            } else {
                pass = edit_pass.getText().toString();
                passok = edit_pass_ok.getText().toString();

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                if (edit_pass.getVisibility() == View.VISIBLE && !pass.equals(passok) && pass != null) {
                    dialog.setMessage(getResources().getString(R.string.Password_Enter));
                    dialog.setPositiveButton(getResources().getString(R.string.confirm), null);
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.setTitle(getResources().getString(R.string.pw));
                    alertDialog.show();

                } else {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("name",edit_name.toString());
                    editor.putString("email",edit_email.toString());
                    editor.putString("password", edit_pass.toString());
                    Intent intent = new Intent(this, SignUp_Check.class);
                    startActivity(intent);
                }
            }

        });

    }

}