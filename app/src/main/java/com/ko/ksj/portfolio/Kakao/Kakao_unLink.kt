package com.ko.ksj.portfolio.Kakao

import android.app.Application
import android.util.Log
import com.kakao.sdk.user.UserApiClient
import com.ko.ksj.portfolio.Kakao.Kakao_Init.Companion.TAG

class Kakao_unLink : Application () {
    override fun onCreate() {
        super.onCreate()
        // 연결 끊기
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e(TAG, "연결 끊기 실패", error)
            }
            else {
                Log.e(TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }
    }
}