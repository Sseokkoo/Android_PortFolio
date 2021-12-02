package com.ko.ksj.portfolio.repository.retrofit

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface RetroInterface {

    @POST("User/SignUp.php")
    fun SignUp(@Body body: JsonObject): Call<JsonObject>

    @POST("User/Login.php")
    fun Login(@Body body: JsonObject): Call<JsonObject>

    @POST("User/Update.php")
    fun Update(@Body body: JsonObject): Call<JsonObject>

    @POST("User/Delete.php")
    fun Delete(@Body body: JsonObject): Call<JsonObject>

    @POST("User/FindToId.php")
    fun FindToId(@Body body: JsonObject): Call<JsonObject>

    @POST("User/FindToPw.php")
    fun FindToPw(@Body body: JsonObject): Call<JsonObject>

    @GET("User/AES.php")
    fun AES(): Call<JsonObject>

    @POST("User/SNS_Login.php")
    fun SNS_Login(@Body body: JsonObject): Call<JsonObject>
}
