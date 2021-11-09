package com.ko.ksj.portfolio.Repository.Retrofit

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetroInterface {

    // Alarm
    @POST("Alarm/entityAlarmList")
    fun getEntityAlarmList(@Body body: JsonObject): Call<JsonObject>

}
