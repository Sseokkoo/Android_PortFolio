package com.ko.ksj.portfolio.Repository.Retrofit

import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header

interface NaverAPI {
    @GET("nid/me")
    fun NaverLogin(@Header("Authorization")token: String): Call<JsonObject>

//    fun checkEmail(@Body body: JsonObject): Call<JsonObject>

}