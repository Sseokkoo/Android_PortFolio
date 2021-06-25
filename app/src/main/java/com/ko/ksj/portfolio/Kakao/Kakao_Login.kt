package com.ko.ksj.portfolio.Kakao

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.ko.ksj.portfolio.Kakao.Kakao_Init.Companion.TAG
import com.ko.ksj.portfolio.MainActivity
import com.ko.ksj.portfolio.R

class Kakao_Login : Application() {
    override fun onCreate() {
        super.onCreate()
        // 로그인 조합 예제
// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인

        // 로그인 공통 callback 구성
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "로그인 실패", error)
            } else if (token != null) {
                Log.e(TAG, "로그인 성공 ${token.accessToken}")
                // 토큰 정보 보기
                UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                    if (error != null) {
                        Log.e(TAG, "토큰 정보 보기 실패", error)
                        val intent = Intent(this, Kakao_Token::class.java)
                        startActivity(intent)
                    } else if (tokenInfo != null) {
                        Log.i(TAG, "토큰 정보 보기 성공" +
                                "\n회원번호: ${tokenInfo.id}" +
                                "\n만료시간: ${tokenInfo.expiresIn} 초")
                    }
                }

                // 사용자 정보 요청 (기본)
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                        // 사용자 정보 요청 (추가 동의)

                        // 사용자가 로그인 시 제3자 정보제공에 동의하지 않은 개인정보 항목 중 어떤 정보가 반드시 필요한 시나리오에 진입한다면
                        // 다음과 같이 추가 동의를 받고 해당 정보를 획득할 수 있습니다.

                        //  * 주의: 선택 동의항목은 사용자가 거부하더라도 서비스 이용에 지장이 없어야 합니다.

                        // 사용 가능한 모든 동의 항목을 대상으로 추가 동의 필요 여부 확인 및 추가 동의를 요청하는 예제입니다.
                        UserApiClient.instance.me { user, error ->
                            if (error != null) {
                                Log.e(TAG, "사용자 정보 요청 실패", error)
                            }
                            else if (user != null) {
                                var scopes = mutableListOf<String>()

                                if (user.kakaoAccount?.emailNeedsAgreement == true) { scopes.add("account_email") }
                                if (user.kakaoAccount?.profileNeedsAgreement == true) { scopes.add("profile") }

                                if (scopes.count() > 0) {
                                    Log.d(TAG, "사용자에게 추가 동의를 받아야 합니다.")

                                    UserApiClient.instance.loginWithNewScopes(applicationContext, scopes) { token, error ->
                                        if (error != null) {
                                            Log.e(TAG, "사용자 추가 동의 실패", error)
                                            Toast.makeText(this, resources.getString(R.string.kakao_signfail), Toast.LENGTH_SHORT).show()
                                            return@loginWithNewScopes
                                        } else {
                                            Log.e(TAG, "allowed scopes: ${token!!.scopes}")

                                            // 사용자 정보 재요청
                                            UserApiClient.instance.me { user, error ->
                                                if (error != null) {
                                                    Log.e(TAG, "사용자 정보 요청 실패", error)
                                                    Toast.makeText(this, resources.getString(R.string.kakao_signfail), Toast.LENGTH_SHORT).show()
                                                    return@me
                                                }
                                                else if (user != null) {
                                                    Log.e(TAG, "사용자 정보 요청 성공")
                                                    val pref: SharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                                                    val editor: SharedPreferences.Editor = pref.edit()

                                                    editor.putString("email", user.kakaoAccount?.email)
                                                    editor.putString("name", user.kakaoAccount?.profile?.nickname)
                                                    editor.apply()

                                                    val intent = Intent(this, MainActivity::class.java)
                                                    startActivity(intent)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (user != null) {
                        Log.i(TAG, "사용자 정보 요청 성공" +
                                "\n회원번호: ${user.id}" +
                                "\n이메일: ${user.kakaoAccount?.email}" +
                                "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                        val pref: SharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = pref.edit()

                        editor.putString("email", user.kakaoAccount?.email)
                        editor.putString("name", user.kakaoAccount?.profile?.nickname)
                        editor.apply()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }

                // 사용자 정보 저장

                // 변경할 내용
//                val properties = mapOf("nickname" to "${System.currentTimeMillis()}")
//
//                UserApiClient.instance.updateProfile(properties) { error ->
//                    if (error != null) {
//                        Log.e(TAG, "사용자 정보 저장 실패", error)
//                    }
//                    else {
//                        Log.i(TAG, "사용자 정보 저장 성공")
//                    }
//                }
            }
        }


    }
}