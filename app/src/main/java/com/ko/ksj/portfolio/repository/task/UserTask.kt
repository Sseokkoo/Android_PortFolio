package com.ko.ksj.portfolio.repository.task

import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
import com.ko.ksj.portfolio.interfaces.*
import com.ko.ksj.portfolio.repository.model.ConfigData
import com.ko.ksj.portfolio.repository.model.QueryData
import com.ko.ksj.portfolio.repository.retrofit.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


open class UserTask(ctx: Context) : BasicTask(ctx) {
    private val TAG = "UserTask"

    private lateinit var ctx: Context

    init {
        this.ctx = ctx

        queryData = QueryData()
        configData = ConfigData()
    }


    fun SignUp(
        nick_name: String,
        email: String,
        password: String,
        type: Int,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()
        jsonObject.addProperty("nick_name", nick_name)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("password", password)
        jsonObject.addProperty("type", type)

        Log.w(TAG + " SignUp", jsonObject.toString())

        val call = service.SignUp(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, true)
                val obj = response.body()
                if (obj!!["result"].asString.equals("success")) {
                    resp.onComplete(obj!!["result"].asString)

                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                    if (!obj!!["message"].asString.equals("")) {
                        msg.onComplete(obj!!["message"].asString)
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " SignUp", t.message.toString())
            }
        })

        return queryData
    }

    fun Login(
        email: String,
        password: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseObjData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("email", email)
        jsonObject.addProperty("password", password)

        Log.w(TAG + " password", jsonObject.toString())

        val call = service.Login(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    if (obj!!.get("message").asString.isNotEmpty()) {
                        msg.onComplete(obj!!.get("message").toString())
                    }
                    json.onComplete(obj!!["data"].asJsonObject)
                } else {
                    val obj = response.body()
                    resp.onComplete(data.toString())
                    msg.onComplete(obj!!.get("message").toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " password", t.message.toString())
            }
        })


        return queryData
    }

    fun Update(
        nick_name: String,
        password: String,
        email: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseObjData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()
        jsonObject.addProperty("nick_name", nick_name)
        jsonObject.addProperty("password", password)
        jsonObject.addProperty("email", email)

        Log.w(TAG + " Update", jsonObject.toString())

        val call = service.Update(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, true)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    if (obj!!.get("message").asString.isNotEmpty()) {
                        msg.onComplete(obj!!.get("message").toString())
                    }
                    json.onComplete(obj!!["data"].asJsonObject)

                } else {
                    resp.onComplete(data.toString())
                    val obj = response.body()
                    if (!obj!!["message"].asString.equals("")) {
                        msg.onComplete(obj!!["message"].asString)
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " Update", t.message.toString())
            }
        })


        return queryData
    }

    fun Delete(
        email: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("email", email)

        Log.w(TAG + " Delete", jsonObject.toString())

        val call = service.Delete(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    if (obj!!.get("message").asString.isNotEmpty()) {
                        msg.onComplete(obj!!.get("message").toString())
                    }
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " Delete", t.message.toString())
            }
        })

        return queryData
    }

    fun FindToId(
        nick_name: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseStrData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("nick_name", nick_name)

        Log.w(TAG + " FindToId", jsonObject.toString())

        val call = service.FindToId(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    if (obj!!.get("message").asString.isNotEmpty()) {
                        msg.onComplete(obj!!.get("message").toString())
                    }
                    json.onComplete(obj!!["data"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " FindToId", t.message.toString())
            }
        })

        return queryData
    }

    fun FindToPw(
        email: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseStrData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("email", email)

        Log.w(TAG + " FindToPw", jsonObject.toString())

        val call = service.FindToPw(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    if (obj!!.get("message").asString.isNotEmpty()) {
                        msg.onComplete(obj!!.get("message").toString())
                    }
                    json.onComplete(obj!!["data"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " FindToPw", t.message.toString())
            }
        })

        return queryData
    }

    fun AES(
        resp: AsyncTaskResponse,
        json: ResponseStrData
    ): QueryData {

        val service = RestClient.getService()

        val call = service.AES()
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    json.onComplete(obj!!["data"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " AES", t.message.toString())
            }
        })

        return queryData
    }

    fun SNS_Login(
        email: String,
        type: Int,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseObjData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("email", email)
        jsonObject.addProperty("type", type)

        Log.w(TAG + " SNS_Login", jsonObject.toString())

        val call = service.SNS_Login(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    if (obj!!.get("message").asString.isNotEmpty()) {
                        msg.onComplete(obj!!.get("message").toString())
                    }

                    json.onComplete(obj!!["data"].asJsonObject)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " SNS_Login", t.message.toString())
            }
        })

        return queryData
    }
}
