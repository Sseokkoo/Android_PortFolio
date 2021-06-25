package com.ko.ksj.portfolio.Kakao

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.ko.ksj.portfolio.Kakao.Kakao_Init.Companion.TAG

class Kakao_Token : Application(){
    override fun onCreate() {
        super.onCreate()
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                        Toast.makeText(this, error.msg, Toast.LENGTH_SHORT).show()
//                        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                        Log.e(TAG, error.msg + "/" + error.message)
                        val intent = Intent(this, Kakao_Init::class.java)
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                    val intent = Intent(this, Kakao_Login::class.java)
                    startActivity(intent)
                }
            }
        }
        else {
            val intent = Intent(this, Kakao_Init::class.java)
            startActivity(intent)
        }
    }
}