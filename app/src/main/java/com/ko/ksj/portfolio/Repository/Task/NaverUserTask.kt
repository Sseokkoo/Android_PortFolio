package com.ko.ksj.portfolio.Repository.Task

import android.content.Context
import android.util.Log
import android.widget.EditText
import androidx.navigation.NavController
import com.google.firebase.auth.UserInfo
import com.google.gson.JsonObject
import com.ko.ksj.portfolio.R
import com.ko.ksj.portfolio.Repository.Retrofit.NaverClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


open class NaverUserTask(ctx: Context) {
    private val TAG = "NaverUserTask"

//    private val pref = ctx.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
//    private val editor = pref.edit()

    val datainfo = DataInfo()

    fun login(token: String, LoginPageCheck: Boolean, navController: NavController) {
        val service = NaverClient.getService()

        val header = "Bearer " + token

        val call = service!!.NaverLogin(header)

        call.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {

                    val obj = response.body()

                    Log.e(TAG, "결과 : $obj \n ${obj!!.get("resultcode")}")

                    val result = obj!!.get("resultcode").toString().replace("\"", "")
                    if (result.equals("00")) {
//                        if (obj!!.getString("resultcode").equals("00")) {
                        val json = obj.getAsJsonObject("response")

                        Log.e(TAG, json.toString())

                        val email = json.get("email").toString().replace("\"", "")
                        val name = json.get("name").toString().replace("\"", "")
                        if (json.get("profile_image") != null) {
//                            val profile = json.get("profile_image").toString().replace("\"", "")
                            datainfo.infoSet("email", email)
                            datainfo.infoSet("name", name)
                            datainfo.infoSet("naver_email", email)
                            datainfo.infoSet("naver_name", name)
//                            editor.putString("sns_profile", profile)
//                            Log.e(TAG, "response : $email \n $name \n $profile")
                            Config.getInstance().dataInfo = datainfo
                        } else {
                            datainfo.infoSet("email", email)
                            datainfo.infoSet("name", name)
                            datainfo.infoSet("naver_email", email)
                            datainfo.infoSet("naver_name", name)
                            Log.e(TAG, "response : $email \n $name")
                            Config.getInstance().dataInfo = datainfo
                        }

                        UserInfo().email = email
                        SNS_SignupCheck = true
                    }

                    if (LoginPageCheck) {

                    } else {
                        navController.navigate(R.id.action_navigation_signup_to_navigation_joininfo)
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e(TAG, "실패 : $t")
            }
        })
    }

    fun resultEmail(editText: EditText){
        editText.setText(datainfo.infoGet("email"))
    }

}

