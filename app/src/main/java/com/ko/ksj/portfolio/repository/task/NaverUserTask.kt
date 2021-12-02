package com.ko.ksj.portfolio.repository.task

import android.content.Context
import android.util.Log
import androidx.navigation.NavController
import com.google.gson.JsonObject
import com.ko.ksj.portfolio.interfaces.AsyncTaskResponse
import com.ko.ksj.portfolio.interfaces.ResponseMessage
import com.ko.ksj.portfolio.interfaces.ResponseObjData
import com.ko.ksj.portfolio.model.Config
import com.ko.ksj.portfolio.model.DataInfo
import com.ko.ksj.portfolio.model.User
import com.ko.ksj.portfolio.R
import com.ko.ksj.portfolio.interfaces.ResponseArrayData
import com.ko.ksj.portfolio.repository.retrofit.NaverClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


open class NaverUserTask(ctx: Context, navController: NavController) {
    private val TAG = "NaverUserTask"

//    private val pref = ctx.getSharedPreferences("Info", Context.MODE_PRIVATE)
//    private val editor = pref.edit()

    val datainfo = DataInfo()
    var mContext = ctx

    var result = ""

    fun login(token: String) {
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

//                            val profile = json.get("profile_image").toString().replace("\"", "")
                        datainfo.infoSet("sns_email", email)
                        datainfo.infoSetInt("sns_type", 3)
                        Log.e(TAG, "response : $email")
                        Config.getInstance().dataInfo = datainfo

                        UserTask(mContext).SNS_Login(
                            email,
                            3,
                            resp,
                            message,
                            jsonaar
                        )
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

    val resp = AsyncTaskResponse {
        Log.e(TAG, "AsyncTaskResponse :$it")
        result = it
    }
    val message = ResponseMessage {
        Log.e(TAG, "message :$it")
    }
    val jsonaar = ResponseObjData {
        Log.e(TAG, "Json :$it")
        if (result.equals("success")) {
            UserTask(mContext).Login(
                it["email"].asString,
                it["password"].asString,
                resp, message, jsonLogin
            )
        } else {
            navController.navigate(R.id.action_navigation_login_to_navigation_signup)
        }
    }
    val jsonLogin = ResponseObjData {
        Log.e(TAG, "Json :$it")

        User(
            it["nick_name"].asString,
            it["email"].asString,
            it["password"].asString,
            it["type"].asInt
        )

        navController.navigate(R.id.action_navigation_login_to_navigation_home)
    }
}

