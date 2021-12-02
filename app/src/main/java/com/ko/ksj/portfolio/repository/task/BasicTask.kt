package com.ko.ksj.portfolio.repository.task

import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
import com.ko.ksj.portfolio.repository.model.ConfigData
import com.ko.ksj.portfolio.repository.model.QueryData
import retrofit2.Response

open class BasicTask(ctx: Context) {
    private var ctx: Context

    lateinit var queryData: QueryData
    lateinit var configData: ConfigData

    init {
        this.ctx = ctx
    }

    fun checkResponse(
        response: Response<JsonObject>,
        includeData: Boolean
    ): JsonObject {
        var data = JsonObject()

        // response is successful
        if (!response.isSuccessful) {
            data.addProperty("result", "failed")

//            Helpers.showToast(
//                ctx,
//                ctx!!.resources.getString(R.string.common_failed),
//                Toast.LENGTH_LONG
//            )

            Log.e("abc", "xxxxxxxxxxxxxx 리스폰 실패 = $response")

            return data
        }

        val obj = response.body()

        Log.e("abc", "obj = $obj")

        // result
        if (!obj!!.has("result")) {
            data.addProperty("result", "failed")

//                Helpers.showToast(
//                    ctx,
//                    ctx!!.resources.getString(R.string.common_failed),
//                    Toast.LENGTH_LONG
//                )

            Log.e("abc", "xxxxxxxxxxxxxx 리스폰 결과 미포함 = $response")

            return data
        }

        val result = obj["result"].asString
        val message = obj["message"].asString

        configData = ConfigData()
        queryData = QueryData(
            result,
            message,
            configData
        )

        // result is Not 0
        if (result != "success") {
            data.addProperty("result", "failed")

            Log.e("abc", "response failed $message")
//
//            if(result == "2001"
//            || result == "2002") return data
//
//            if(!message.equals("NO_DATA"))
//                SyncStateContract.Helpers.showToast(ctx, message, Toast.LENGTH_LONG)

            return data
        }

        if (includeData)
            data.addProperty("result", obj["data"].toString())
        else
            data.addProperty("result", "success")

        return data
    }

}