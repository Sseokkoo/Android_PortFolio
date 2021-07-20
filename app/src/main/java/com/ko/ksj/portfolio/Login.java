package com.ko.ksj.portfolio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ko.ksj.portfolio.SignUp.SignUpActivity;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {

    String Email, Password,EDTId, EDTPassword;
    EditText Login_EDTId, Login_EDTPassword;
    Button Login_BTNLogin, Login_BTNPassReset, Login_BTNSignIn, Login_BTNNaver, Login_BTNGoogle, Login_BTNKakao;
    CheckBox Login_AutoCheck;
    public static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = Login.this;
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
                finish();
            }
        });

        Login_BTNNaver.setOnClickListener(v -> {
            Log.e("네이버버튼", "호출");
            Naver_start();
        });
    }

    // TODO: 2021-07-20 네이버

    public String OAUTH_CLIENT_ID = "JV9TxKcClc3tDB2eOCHm";
    public String OAUTH_CLIENT_SECRET = "dn0_WXAPqd";
    public String OAUTH_CLIENT_NAME = "포트폴리오";

    public OAuthLogin mOAuthLoginInstance;

    public void Naver_start(){
        Log.e("네이버", "class실행");
//        String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
        //초기화
        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);

//        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.btnNaver);
//        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);

        mOAuthLoginInstance.startOauthLoginActivity((Activity) mContext, mOAuthLoginHandler);
    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                String tokenType = mOAuthLoginInstance.getTokenType(mContext);

//              Toast.makeText(mContext, "success:" + accessToken, Toast.LENGTH_SHORT).show();
                Log.e("확인", "" + accessToken + "   /   " + refreshToken + "   /   " + expiresAt + "   /   " + tokenType);
                ProfileTask task = new ProfileTask();
                //본인이 이동할 액티비티를 입력
                task.execute(accessToken);
                finish();
            } else {
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_LONG).show();
            }
        }
    };

    class ProfileTask extends AsyncTask<String, Void, String> {
        String result;

        @Override
        protected String doInBackground(String... strings) {
            String token = strings[0];// 네이버 로그인 접근 토큰;
            String header = "Bearer " + token; // Bearer 다음에 공백 추가
            try {
                String apiURL = "https://openapi.naver.com/v1/nid/me";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization", header);
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                result = response.toString();
                br.close();
                System.out.println(response.toString());
            } catch (Exception e) {
                System.out.println(e);
            }
            //result 값은 JSONObject 형태로 넘어옵니다.
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                //넘어온 result 값을 JSONObject 로 변환해주고, 값을 가져오면 되는데요.
                // result 를 Log에 찍어보면 어떻게 가져와야할 지 감이 오실거에요.
                String Email,Id;
                JSONObject object = new JSONObject(result);
                Log.d("확인", "결과 : " + result);
                if (object.getString("resultcode").equals("00")) {
                    JSONObject jsonObject = new JSONObject(object.getString("response"));
                    Email = jsonObject.getString("email");
                    Id = jsonObject.getString("id");
                    Log.d("jsonObject", jsonObject.toString());

                    SharedPreferences pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("email", jsonObject.getString("email"));
                    editor.putString("name", jsonObject.getString("name"));
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
