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
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpActivity extends AppCompatActivity {

    String[] value = {"login_type", "email", "name" , "id", "password"};

    EditText edit_name,edit_email, edit_pass, edit_pass_ok;

    ImageView back;
    Button  submit;

    String logintype,email,name,id,pass,passok;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edit_name = (EditText)findViewById(R.id.SignIn_EDTName);
        edit_email = (EditText)findViewById(R.id.SignIn_EDTEmail);
        edit_pass = (EditText)findViewById(R.id.SignIn_EDTPass);
        edit_pass_ok = (EditText)findViewById(R.id.SignIn_EDTPassOk);
        back = findViewById(R.id.SignIn_IVBack);

        back.setOnClickListener(v -> {
            finish();
        });

        SharedPreferences pref = getSharedPreferences("UserInfo",MODE_PRIVATE);
        email = pref.getString("email", "");
        id = pref.getString("id","");

        if(!logintype.equals("default")){
            edit_email.setVisibility(View.GONE);
            edit_pass.setVisibility(View.GONE);
            edit_pass_ok.setVisibility(View.GONE);
        }
        submit = (Button)findViewById(R.id.Sign_BTNSignIn);
        submit.setOnClickListener(v -> {
//            SharedPreferences pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
//            SharedPreferences.Editor editor = pref.edit();
//
//            editor.putString("login_type",logintype);
//            editor.putString("login_type",name);
//            editor.putString("login_type",email;
//            editor.putString("login_type",pass);
//            editor.putString("login_type",passok);
            if(logintype.equals("default")) {
                name = edit_name.getText().toString();
                email = edit_email.getText().toString();
                pass = edit_pass.getText().toString();
                passok = edit_pass_ok.getText().toString();
            }else{
                name = edit_name.getText().toString();
            }
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            Log.e(getApplicationContext().toString(),edit_name.getText().toString());
            if (name.equals("") || name == null){
                dialog.setMessage(getResources().getString(R.string.NickName_Enter));
                dialog.setPositiveButton(getResources().getString(R.string.confirm), null);
                AlertDialog alertDialog = dialog.create();
                alertDialog.setTitle(getResources().getString(R.string.confirm));
                alertDialog.show();
            }else if(edit_pass.getVisibility() == View.VISIBLE && !pass.equals(passok) && pass != null){
                    dialog.setMessage(getResources().getString(R.string.Password_Enter));
                    dialog.setPositiveButton(getResources().getString(R.string.confirm), null);
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.setTitle(getResources().getString(R.string.pw));
                    alertDialog.show();

            }else {
                SignUp task = new SignUp();
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://20.194.57.6/fineev/SignUp.php",
                        logintype, email, name, id , pass);
            }
        });

    }

    class SignUp extends AsyncTask<String, Void, String> {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SignUpActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject obj = new JSONObject(result);
                String Cheak = obj.getString("Response");
                Log.e("확인", Cheak);
                if (Cheak.equals("Nomalization_Email")) {
                    dialog.setMessage("올바른 E-Mail 형식이 아닙니다.\n다시 시도해주세요.");
                    dialog.setPositiveButton("확인", null);
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.setTitle("이메일");
                    alertDialog.show();
                } else if (Cheak.equals("EMail_Duplication")){
                    dialog.setMessage("이미 사용중인 이메일입니다.\n로그인 해주세요!");
                    dialog.setPositiveButton("확인", null);
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.setTitle("이메일");
                    alertDialog.show();
                } else if (Cheak.equals("Password_815")){
                    dialog.setMessage("비밀번호는 영문, 숫자, 특수문자를 혼합하여 최소 8자리 ~ 최대 15자리 이내로 입력해주세요.");
                    dialog.setPositiveButton("확인", null);
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.setTitle("비밀번호");
                    alertDialog.show();
                }else if (Cheak.equals("Password_Space")){
                    dialog.setMessage("비밀번호는 공백없이 입력해주세요.");
                    dialog.setPositiveButton("확인", null);
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.setTitle("비밀번호");
                    alertDialog.show();
                }else if (Cheak.equals("Nomalization_Password")){
                    dialog.setMessage("영문, 숫자, 특수문자를 혼합하여 입력해주세요.");
                    dialog.setPositiveButton("확인", null);
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.setTitle("비밀번호");
                    alertDialog.show();
                }else if (Cheak.equals("Success")){
                    dialog.setMessage("가입에 성공하셨습니다.\n가입을 축하드립니다~!");
                    dialog.setPositiveButton("확인", null);
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.setTitle("가입하기");
                    alertDialog.show();

                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    dialog.setMessage("서버 오류입니다.\n보다 나은 서비스를 위해 문의 부탁드립니다.");
                    dialog.setPositiveButton("확인", null);
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.setTitle("서버");
                    alertDialog.show();
                }


            } catch (JSONException ex) {
                ex.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... params) {

            String Parameter = "";

            for (int i = 0; i < value.length; i++) {
                Parameter += value[i] + "=" + (String)params[i+1] + "&";
                Log.e("확인",Parameter);
            }
Log.e("확인2",Parameter);
            Parameter = Parameter.substring(0, Parameter.length()-1);
            Log.e("확인3",Parameter);

            try {

                URL url = new URL("http://20.194.57.6/fineev/SignUp.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(Parameter.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();


                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {


                return new String("Error: " + e.getMessage());
            }
        }
    }
}