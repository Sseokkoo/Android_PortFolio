package com.ko.ksj.portfolio.Kakao

import android.app.Application
import android.content.Intent
import android.util.Log
import com.kakao.sdk.user.UserApiClient
import com.ko.ksj.portfolio.Kakao.Kakao_Init.Companion.TAG

class Kakao_Logout : Application() {
    override fun onCreate() {
        super.onCreate()
        // 로그아웃
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            }
            else {
                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                val intent = Intent(this, Kakao_unLink::class.java)
                startActivity(intent)
            }
        }
    }
}