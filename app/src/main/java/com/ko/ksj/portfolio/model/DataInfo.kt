package com.ko.ksj.portfolio.model

import com.google.gson.JsonObject

class DataInfo{

    val datalist = JsonObject()

    fun infoSet(key: String, obj: String) {
        datalist.addProperty(key, obj)
    }

    fun infoGet(key: String): String {
        if (datalist[key] != null) {
            return datalist[key].asString
        } else {
            return ""
        }
    }

    fun infoSetInt(key: String, obj: Int) {
        datalist.addProperty(key, obj)
    }

    fun infoGetInt(key: String): Int {
        if (datalist[key] != null) {
            return datalist[key].asInt
        } else {
            return 0
        }
    }


    fun infoSetBool(key: String, obj: Boolean) {
        datalist.addProperty(key, obj)
    }

    fun infoGetBool(key: String): Boolean {
        if (datalist[key] != null) {
            return datalist[key].asBoolean
        } else {
            return false
        }
    }
}