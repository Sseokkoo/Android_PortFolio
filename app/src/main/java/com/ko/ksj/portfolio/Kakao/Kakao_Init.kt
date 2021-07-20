package com.ko.ksj.portfolio.Kakao

import android.app.Application
import android.content.Intent
import android.util.Log
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.ko.ksj.portfolio.Kakao.Kakao_Init.Companion.TAG
import com.ko.ksj.portfolio.MainActivity
import com.ko.ksj.portfolio.SignUp.SignUpActivity

class Kakao_Init : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "cf23d7b83f2ba7af13f7bf203a950183")

        //카카오톡으로 사용자 인증

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(applicationContext)) {
            UserApiClient.instance.loginWithKakaoTalk(applicationContext) { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
//                    val intent = Intent(applicationContext, Kakao_Token::class.java)
//                    startActivity(intent)
                    val kakaoToken = Kakao_Token::class.java
                    kakaoToken.newInstance()
                }
                else if (token != null) {
                    Log.e(TAG, "로그인 성공 ${token.accessToken}")
//                    val intent = Intent(applicationContext, SignUpActivity::class.java)
//                    startActivity(intent)
                    val signUpActivity = SignUpActivity::class.java
                    signUpActivity.newInstance()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(applicationContext) { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
//                    val intent = Intent(applicationContext, Kakao_Token::class.java)
//                    startActivity(intent)
                    val kakaoToken = Kakao_Token::class.java
                    kakaoToken.newInstance()
                }
                else if (token != null) {
                    Log.i(TAG, "로그인 성공 ${token.accessToken}")
//                    val intent = Intent(applicationContext, SignUpActivity::class.java)
//                    startActivity(intent)
                    val signUpActivity = SignUpActivity::class.java
                    signUpActivity.newInstance()
                }
            }
        }
//        // 카카오계정으로 로그인
//        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
//            if (error != null) {
//                Log.e(TAG, "로그인 실패", error)
//            }
//            else if (token != null) {
//                Log.i(TAG, "로그인 성공 ${token.accessToken}")
//            }
//        }

    }

    companion object {
        const val TAG: String = "KAKAO"
    }
}