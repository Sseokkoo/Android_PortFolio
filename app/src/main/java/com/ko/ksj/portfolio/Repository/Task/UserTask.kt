package com.ko.ksj.portfolio.Repository.Task

import android.content.Context
import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.ko.ksj.portfolio.Interfaces.*
import com.ko.ksj.portfolio.Repository.Model.ConfigData
import com.ko.ksj.portfolio.Repository.Model.QueryData
import com.ko.ksj.portfolio.Repository.Retrofit.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


open class UserTask(ctx: Context) : BasicTask(ctx) {
    private val TAG = "UserTask"

    private lateinit var ctx: Context

    init {
        this.ctx = ctx

        queryData = QueryData()
        configData = ConfigData()
    }


    fun login(
        email: String,
        pwd: String,
        deviceType: String,
        userToken: String,
        appVersion: String,
        deviceModel: String,
        deviceSerial: String,
        osVersion: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseObjData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()
        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("password", pwd)
        jsonObject.addProperty("deviceType", deviceType)
        jsonObject.addProperty("userToken", userToken)
        jsonObject.addProperty("appVersion", appVersion)
        jsonObject.addProperty("deviceModel", deviceModel)
        jsonObject.addProperty("deviceSerial", deviceSerial)
        jsonObject.addProperty("osVersion", osVersion)

        Log.w(TAG + " Login", jsonObject.toString())

        val call = service.login(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, true)
                val obj = response.body()
                if (obj!!["result"].asString.equals("0")) {
                    resp.onComplete(obj!!["result"].asString)


                    msg.onComplete(obj!!["message"].asString)
                    json.onComplete(obj!!["data"].asJsonObject)
                } else {
                    resp.onComplete(data.toString())
                    if (!obj!!["message"].asString.equals("")) {
                        msg.onComplete(obj!!["message"].asString)
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " Login", t.message.toString())
            }
        })

        return queryData
    }

    fun AuthPhone(
        telNo: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()
        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("telNo", telNo)

        Log.w(TAG + " AuthPhone", jsonObject.toString())

        val call = service.phoneauth(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    if (!obj!!.get("message").asString.equals("")) {
                        msg.onComplete(obj!!.get("message").toString())
                    }
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " AuthPhone", t.message.toString())
            }
        })


        return queryData
    }

    fun findtoid(
        telNo: String,
        userName: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseStrData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()
        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("telNo", telNo)
        jsonObject.addProperty("userName", userName)

        Log.w(TAG + " FindToId", jsonObject.toString())

        val call = service.findtoid(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, true)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                    json.onComplete(obj!!["data"].asString)

                } else {
                    resp.onComplete(data.toString())
                    val obj = response.body()
                    if (!obj!!["message"].asString.equals("")){
                        msg.onComplete(obj!!["message"].asString)
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " FindtoId", t.message.toString())
            }
        })


        return queryData
    }
    fun findtopwd(
        telNo: String,
        userName: String,
        email: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("telNo", telNo)
        jsonObject.addProperty("userName", userName)
        jsonObject.addProperty("email", email)

        Log.w(TAG + " FindToPwd", jsonObject.toString())

        val call = service.findtopwd(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " FindtoId", t.message.toString())
            }
        })

        return queryData
    }

    fun farmpin(
        farmpin: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseArrayData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()
        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmpin", farmpin)

        Log.w(TAG + " farmpin", jsonObject.toString())

        val call = service.farmpin(jsonObject)

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                val obj = response.body()
                val jsonarray = obj!!.getAsJsonArray("data")

                Log.e(TAG, "$obj , ${obj.getAsJsonArray("data")} ${obj.get("result")}")
                if (obj.get("result").toString().equals("0")) {
                    resp.onComplete(obj.get("result").toString())
                    msg.onComplete(obj["message"].asString)
                    json.onComplete(jsonarray)
                } else {
                    resp.onComplete(obj.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " farmpin", t.message.toString())
            }
        })

        return queryData
    }

    fun checkEmail(
        email: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()
        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("email", email)

        Log.w(TAG + " CheckEmail", jsonObject.toString())

        val call = service.checkEmail(jsonObject)

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                val obj = response.body()

                if (obj!!.get("result").toString().equals("0")) {
                    resp.onComplete(obj.get("result").toString())
                    msg.onComplete(obj.get("message").toString())
                } else {
                    resp.onComplete(obj.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " farmpin", t.message.toString())
            }
        })

        return queryData
    }

    fun domesticSignup(
        email: String,
        password: String,
        telNo: String,
        imageName: String,
        farmpin: String,
        farmName: String,
        owner: String,
        address: String,
        entityType: Int,
        referrerTelno: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()
        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("password", password)
        jsonObject.addProperty("telNo", telNo)
        jsonObject.addProperty("imageName", imageName)
        jsonObject.addProperty("farmpin", farmpin)
        jsonObject.addProperty("farmName", farmName)
        jsonObject.addProperty("owner", owner)
        jsonObject.addProperty("address", address)
        jsonObject.addProperty("entityType", entityType)
        jsonObject.addProperty("referrerTelno", referrerTelno)

        Log.w(TAG + " DomesticSignUp", jsonObject.toString())

        val call = service.domesticSignup(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    if (data["result"].asString.equals("success")) {
                        msg.onComplete(response.body()!!.get("message").asString)
                    }
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + "domesticSignup ", t.message.toString())
            }
        })


        return queryData
    }

    fun overseaSignup(
        email: String,
        password: String,
        imageName: String,
        farmName: String,
        owner: String,
        address: String,
        entityType: Int,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()
        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("password", password)
        jsonObject.addProperty("imageName", imageName)
        jsonObject.addProperty("farmName", farmName)
        jsonObject.addProperty("owner", owner)
        jsonObject.addProperty("address", address)
        jsonObject.addProperty("entityType", entityType)

        Log.w(TAG + " OverSeaSignUp", jsonObject.toString())

        val call = service.overSeaSignup(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)

                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " farmpin", t.message.toString())
            }
        })


        return queryData
    }



    //    Todo: More
    fun noticeList(
        farmSeq: Int,
        email: String,
        pageIndex: Int,
        listCount: Int,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseArrayData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()
        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("pageIndex", pageIndex)
        jsonObject.addProperty("listCount", listCount)

        Log.w(TAG + " Notice", jsonObject.toString())

        val call = service.noticeList(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                val obj = response.body()
                val jsonarray = obj!!.getAsJsonArray("data")

                Log.e(TAG, "$obj , ${obj.getAsJsonArray("data")} ${obj.get("result")}")

                resp.onComplete(obj.get("result").toString())
                msg.onComplete(obj["message"].asString)
                json.onComplete(jsonarray)

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " Login", t.message.toString())
            }
        })

        return queryData
    }

    fun webPageUrl(
        farmSeq: Int,
        email: String,
        searchType: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        data_str: ResponseStrData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()
        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("searchType", searchType)

        Log.w(TAG + " WebPageURL", jsonObject.toString())

        val call = service.webPageUrl(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                    data_str.onComplete(obj!!["data"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " Login", t.message.toString())
            }
        })

        return queryData
    }

    fun dueDayInfo(
        farmSeq: Int,
        email: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseObjData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()
        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)

        Log.w(TAG + " dueDayInfo", jsonObject.toString())

        val call = service.dueDayInfo(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                    json.onComplete(obj!!["data"].asJsonObject)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " Login", t.message.toString())
            }
        })

        return queryData
    }

    fun dueDayUpdate(
        farmSeq: Int,
        email: String,
        calveDueDay: JsonArray,
        estrusDueDay: JsonArray,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.add("calveDueDay", calveDueDay)
        jsonObject.add("estrusDueDay", estrusDueDay)

        Log.w(TAG + " dueDayUpdate", jsonObject.toString())

        val call = service.dueDayUpdate(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " Login", t.message.toString())
            }
        })

        return queryData
    }

    fun breedAlarmInfo(
        farmSeq: Int,
        email: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseObjData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)

        Log.w(TAG + " breedAlarmInfo", jsonObject.toString())

        val call = service.breedAlarmInfo(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)

                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                    json.onComplete(obj!!["data"].asJsonObject)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " FindtoId", t.message.toString())
            }
        })

        return queryData
    }

    fun farminfo(
        farmSeq: Int,
        email: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseObjData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)

        Log.w(TAG + " FarmInfo", jsonObject.toString())

        val call = service.farmInfo(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()

                    if (!data["result"].asString.equals("failed")) {
                        msg.onComplete(obj!!["message"].asString)
                        json.onComplete(obj!!["data"].asJsonObject)
                    }

                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " FarmInfo", t.message.toString())
            }
        })

        return queryData
    }

    fun breedSetting(
        farmSeq: Int,
        email: String,
        eaFlag: String,
        caFlag: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("eaFlag", eaFlag)
        jsonObject.addProperty("caFlag", caFlag)

        Log.w(TAG + " breedSetting", jsonObject.toString())

        val call = service.breedSetting(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!["result"].asString.equals("0")) {
                    resp.onComplete(data["result"].asString)
                } else {
                    resp.onComplete(data.toString())
                    val obj = response.body()
                    if (data["result"].asString.equals("success"))
                        msg.onComplete(obj!!["message"].asString)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " FindtoId", t.message.toString())
            }
        })

        return queryData
    }

    fun cowAlarmInfo(
        farmSeq: Int,
        email: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseObjData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)

        Log.w(TAG + " cowAlarmInfo", jsonObject.toString())

        val call = service.cowAlarmInfo(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                    json.onComplete(obj!!["data"].asJsonObject)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " FindtoId", t.message.toString())
            }
        })

        return queryData
    }


    fun cowSetting(
        farmSeq: Int,
        email: String,
        haFlag: String,
        laFlag: String,
        aiFlag: String,
        onFlag: String,
        anFlag: String,
        knFlag: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("haFlag", haFlag)
        jsonObject.addProperty("laFlag", laFlag)
        jsonObject.addProperty("aiFlag", aiFlag)
        jsonObject.addProperty("onFlag", onFlag)
        jsonObject.addProperty("anFlag", anFlag)
        jsonObject.addProperty("knFlag", knFlag)

        Log.w(TAG + " cowSetting", jsonObject.toString())

        val call = service.cowSetting(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!["result"].asString.equals("0")) {
                    resp.onComplete(data["result"].asString)
                } else {
                    resp.onComplete(data.toString())
                    val obj = response.body()
                    if (data["result"].asString.equals("success"))
                        msg.onComplete(obj!!["message"].asString)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " FindtoId", t.message.toString())
            }
        })

        return queryData
    }

    fun calfAlarmInfo(
        farmSeq: Int,
        email: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseObjData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)

        Log.w(TAG + " calfAlarmInfo", jsonObject.toString())

        val call = service.calfAlarmInfo(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                    json.onComplete(obj!!["data"].asJsonObject)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " FindtoId", t.message.toString())
            }
        })

        return queryData
    }


    fun calfSetting(
        farmSeq: Int,
        email: String,
        haFlag: String,
        laFlag: String,
        aiFlag: String,
        onFlag: String,
        anFlag: String,
        knFlag: String,
        mnFlag: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("haFlag", haFlag)
        jsonObject.addProperty("laFlag", laFlag)
        jsonObject.addProperty("aiFlag", aiFlag)
        jsonObject.addProperty("onFlag", onFlag)
        jsonObject.addProperty("anFlag", anFlag)
        jsonObject.addProperty("knFlag", knFlag)
        jsonObject.addProperty("mnFlag", mnFlag)
        Log.w(TAG + " calfSetting", jsonObject.toString())

        val call = service.calfSetting(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!["result"].asString.equals("0")) {
                    resp.onComplete(data["result"].asString)
                } else {
                    resp.onComplete(data.toString())
                    val obj = response.body()
                    if (data["result"].asString.equals("success"))
                        msg.onComplete(obj!!["message"].asString)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " FindtoId", t.message.toString())
            }
        })

        return queryData
    }

    fun dueDayAlarmInfo(
        farmSeq: Int,
        email: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseObjData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)

        Log.w(TAG + "DayAlarmInfo", jsonObject.toString())

        val call = service.dueDayAlarmInfo(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                    json.onComplete(obj!!["data"].asJsonObject)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " FindtoId", t.message.toString())
            }
        })

        return queryData
    }


    fun dueDaySetting(
        farmSeq: Int,
        email: String,
        edFlag: String,
        idFlag: String,
        adFlag: String,
        ddFlag: String,
        deFlag: String,
        cdFlag: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("edFlag", edFlag)
        jsonObject.addProperty("idFlag", idFlag)
        jsonObject.addProperty("adFlag", adFlag)
        jsonObject.addProperty("ddFlag", ddFlag)
        jsonObject.addProperty("deFlag", deFlag)
        jsonObject.addProperty("cdFlag", cdFlag)
        Log.w(TAG + " dueDaySetting", jsonObject.toString())

        val call = service.dueDaySetting(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!["result"].asString.equals("0")) {
                    resp.onComplete(data["result"].asString)
                } else {
                    resp.onComplete(data.toString())
                    val obj = response.body()
                    if (data["result"].asString.equals("success"))
                        msg.onComplete(obj!!["message"].asString)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " FindtoId", t.message.toString())
            }
        })

        return queryData
    }

    fun noticeAlarmInfo(
        farmSeq: Int,
        email: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseObjData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)

        Log.w(TAG + "noticeAlarmInfo", jsonObject.toString())

        val call = service.noticeAlarmInfo(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                    json.onComplete(obj!!["data"].asJsonObject)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " FindtoId", t.message.toString())
            }
        })

        return queryData
    }


    fun noticeSetting(
        farmSeq: Int,
        email: String,
        ntFlag: String,
        sbFlag: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("ntFlag", ntFlag)
        jsonObject.addProperty("sbFlag", sbFlag)
        Log.w(TAG + " noticeSetting", jsonObject.toString())

        val call = service.noticeSetting(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!["result"].asString.equals("0")) {
                    resp.onComplete(data["result"].asString)
                } else {
                    resp.onComplete(data.toString())
                    val obj = response.body()
                    if (data["result"].asString.equals("success"))
                        msg.onComplete(obj!!["message"].asString)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " FindtoId", t.message.toString())
            }
        })

        return queryData
    }

    fun userAlarmInfo(
        farmSeq: Int,
        email: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseArrayData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()
        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)

        Log.w(TAG + " userAlarmInfo", jsonObject.toString())

        val call = service.userAlarmInfo(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                val obj = response.body()
                val jsonarray = obj!!.getAsJsonArray("data")

                Log.e(TAG, "$obj , ${obj.getAsJsonArray("data")} ${obj.get("result")}")

                resp.onComplete(obj.get("result").toString())
                msg.onComplete(obj["message"].asString)
                json.onComplete(jsonarray)

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " userAlarmInfo", t.message.toString())
            }
        })

        return queryData
    }

    fun userSetting(
        farmSeq: Int,
        email: String,
        userSetting: JsonArray,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.add("userSetting", userSetting)

        Log.w(TAG + " userSetting", jsonObject.toString())

        val call = service.userSetting(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " userSetting", t.message.toString())
            }
        })

        return queryData
    }

    fun userAlarmUpdate(
        farmSeq: Int,
        email: String,
        alarmSeq: Int,
        alarmTitle: String,
        baseDate: String,
        baseType: String,
        alarmDay: Int,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("alarmSeq", alarmSeq)
        jsonObject.addProperty("alarmTitle", alarmTitle)
        jsonObject.addProperty("baseDate", baseDate)
        jsonObject.addProperty("baseType", baseType)
        jsonObject.addProperty("alarmDay", alarmDay)

        Log.w(TAG + "AlarmUpdate", jsonObject.toString())

        val call = service.userAlarmUpdate(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    if (!data["result"].asString.equals("failed"))
                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " AlarmUpdate", t.message.toString())
            }
        })

        return queryData
    }

    fun userAlarmDelete(
        farmSeq: Int,
        email: String,
        alarmSeq: Int,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("alarmSeq", alarmSeq)

        Log.w(TAG + "AlarmDelete", jsonObject.toString())

        val call = service.userAlarmDelete(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " AlarmDelete", t.message.toString())
            }
        })

        return queryData
    }


    fun userAlarmInsert(
        farmSeq: Int,
        email: String,
        alarmTitle: String,
        baseDate: String,
        baseType: String,
        alarmDay: Int,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("alarmTitle", alarmTitle)
        jsonObject.addProperty("baseDate", baseDate)
        jsonObject.addProperty("baseType", baseType)
        jsonObject.addProperty("alarmDay", alarmDay)

        Log.w(TAG + "AlarmInsert", jsonObject.toString())

        val call = service.userAlarmInsert(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " AlarmInsert", t.message.toString())
            }
        })

        return queryData
    }

    fun UserList(
        farmSeq: Int,
        email: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseArrayData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)

        Log.w(TAG + " UserList", jsonObject.toString())

        val call = service.UserList(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    if (!data["result"].asString.equals("failed")) {
                        msg.onComplete(obj!!["message"].asString)
                        json.onComplete(obj["data"].asJsonArray)
                    }
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " UserList", t.message.toString())
            }
        })

        return queryData
    }

    fun UserCreate(
        farmSeq: Int,
        email: String,
        subEmail: String,
        subPassword: String,
        subUserName: String,
        subTelNo: String,
        subImageName: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("subEmail", subEmail)
        jsonObject.addProperty("subPassword", subPassword)
        jsonObject.addProperty("subUserName", subUserName)
        jsonObject.addProperty("subTelNo", subTelNo)
        jsonObject.addProperty("subImageName", subImageName)

        Log.w(TAG + "UserCreate", jsonObject.toString())

        val call = service.UserCreate(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " UserCreate", t.message.toString())
            }
        })

        return queryData
    }

    fun imageUpload(
        farmSeq: Int,
        email: String,
        imageKind: String,
        imageName: String,
        imageData: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("imageKind", imageKind)
        jsonObject.addProperty("imageName", imageName)
        jsonObject.addProperty("imageData", imageData)


        Log.w(TAG + "imageUpload", jsonObject.toString())

        val call = service.imageUpload(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " imageUpload", t.message.toString())
            }
        })

        return queryData
    }


    fun Userdelete(
        farmSeq: Int,
        email: String,
        subEmail: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("subEmail", subEmail)


        Log.w(TAG + "Userdelete", jsonObject.toString())

        val call = service.Userdelete(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " Userdelete", t.message.toString())
            }
        })

        return queryData
    }
    fun withdraw(
        farmSeq: Int,
        email: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)


        Log.w(TAG + "withdraw", jsonObject.toString())

        val call = service.withdraw(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " withdraw", t.message.toString())
            }
        })

        return queryData
    }

    fun diagnosisList(
        farmSeq: Int,
        email: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseArrayData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)

        Log.w(TAG + " diagnosisList", jsonObject.toString())

        val call = service.diagnosisList(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                    json.onComplete(obj["data"].asJsonArray)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " diagnosisList", t.message.toString())
            }
        })

        return queryData
    }

    fun diagnosisUpdate(
        farmSeq: Int,
        email: String,
        diagnosisSeq: Int,
        diagnosisName: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("diagnosisSeq", diagnosisSeq)
        jsonObject.addProperty("diagnosisName", diagnosisName)


        Log.w(TAG + "diagnosisUpdate", jsonObject.toString())

        val call = service.diagnosisUpdate(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    if (data["result"].asString.equals("success") || data["result"].asString.equals("0"))
                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + "diagnosisUpdate", t.message.toString())
            }
        })

        return queryData
    }

    fun diagnosisInsert(
        farmSeq: Int,
        email: String,
        diagnosisName: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("diagnosisName", diagnosisName)


        Log.w(TAG + "diagnosisInsert", jsonObject.toString())

        val call = service.diagnosisInsert(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + "diagnosisInsert", t.message.toString())
            }
        })

        return queryData
    }
    fun diagnosisDelete(
        farmSeq: Int,
        email: String,
        diagnosisSeq: Int,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("diagnosisSeq", diagnosisSeq)


        Log.w(TAG + "diagnosisDelete", jsonObject.toString())

        val call = service.diagnosisDelete(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + "diagnosisDelete", t.message.toString())
            }
        })

        return queryData
    }

    fun prescriptionList(
        farmSeq: Int,
        email: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseArrayData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)

        Log.w(TAG + "presList", jsonObject.toString())

        val call = service.prescriptionList(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                    json.onComplete(obj["data"].asJsonArray)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " presList", t.message.toString())
            }
        })

        return queryData
    }

    fun prescriptionUpdate(
        farmSeq: Int,
        email: String,
        prescriptionSeq: Int,
        prescriptionName: String,
        prescriptionType: String,
        ingredient: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("prescriptionSeq", prescriptionSeq)
        jsonObject.addProperty("prescriptionName", prescriptionName)
        jsonObject.addProperty("prescriptionType", prescriptionType)
        jsonObject.addProperty("ingredient", ingredient)


        Log.w(TAG + "presUpdate", jsonObject.toString())

        val call = service.prescriptionUpdate(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + "presUpdate", t.message.toString())
            }
        })

        return queryData
    }

    fun prescriptionInsert(
        farmSeq: Int,
        email: String,
        prescriptionName: String,
        prescriptionType: String,
        ingredient: String,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("prescriptionName", prescriptionName)
        jsonObject.addProperty("prescriptionType", prescriptionType)
        jsonObject.addProperty("ingredient", ingredient)


        Log.w(TAG + "presInsert", jsonObject.toString())

        val call = service.prescriptionInsert(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + "presInsert", t.message.toString())
            }
        })

        return queryData
    }
    fun prescriptionDelete(
        farmSeq: Int,
        email: String,
        prescriptionSeq: Int,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("prescriptionSeq", prescriptionSeq)


        Log.w(TAG + "presDelete", jsonObject.toString())

        val call = service.prescriptionDelete(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + "presDelete", t.message.toString())
            }
        })

        return queryData
    }

    fun entityList(
        farmSeq: Int,
        email: String,
        entityNo: String,
        pageIndex:Int,
        listCount:Int,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseArrayData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("entityNo", entityNo)
        jsonObject.addProperty("pageIndex", pageIndex)
        jsonObject.addProperty("listCount", listCount)

        Log.w(TAG + "presList", jsonObject.toString())

        val call = service.entityList(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                    json.onComplete(obj["data"].asJsonArray)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " entityList", t.message.toString())
            }
        })

        return queryData
    }

    fun serviceEnd(
        farmSeq: Int,
        email: String,
        seqList: JsonArray,
        resp: AsyncTaskResponse,
        msg: ResponseMessage
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()

        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.add("seqList", seqList)

        Log.w(TAG + " serviceEnd", jsonObject.toString())

        val call = service.serviceEnd(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val data = checkResponse(response, false)
                if (data!!.has("result")) {
                    resp.onComplete(data["result"].asString)

                    val obj = response.body()
                    msg.onComplete(obj!!["message"].asString)
                } else {
                    resp.onComplete(data.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " serviceEnd", t.message.toString())
            }
        })

        return queryData
    }

    fun noticeInfo(
        farmSeq: Int,
        email: String,
        noticeSeq : Int,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseObjData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()
        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("noticeSeq", noticeSeq)

        Log.w(TAG + " Notice", jsonObject.toString())

        val call = service.noticeInfo(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                val obj = response.body()

                resp.onComplete(obj!!.get("result").toString())
                msg.onComplete(obj["message"].asString)
                json.onComplete(obj["data"].asJsonObject)

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " Login", t.message.toString())
            }
        })

        return queryData
    }
    fun noticeNew(
        farmSeq: Int,
        email: String,
        noticeSeq : Int,
        resp: AsyncTaskResponse,
        msg: ResponseMessage,
        json: ResponseObjData
    ): QueryData {

        val service = RestClient.getService()

        val jsonObject = JsonObject()
        jsonObject.addProperty("langCode", Config.getInstance().langCode)
        jsonObject.addProperty("farmSeq", farmSeq)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("noticeSeq", noticeSeq)

        Log.w(TAG + " Notice", jsonObject.toString())

        val call = service.noticeNew(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                val obj = response.body()

                resp.onComplete(obj!!.get("result").toString())
                msg.onComplete(obj["message"].asString)
                json.onComplete(obj["data"].asJsonObject)

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w(TAG + " Login", t.message.toString())
            }
        })

        return queryData
    }
}
