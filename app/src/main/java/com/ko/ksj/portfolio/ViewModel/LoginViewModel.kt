package com.ko.ksj.portfolio.ViewModel

import androidx.lifecycle.ViewModel
import com.ko.ksj.portfolio.Model.LoginInfo

class LoginViewModel : ViewModel() {

    val loginInfo = LoginInfo()

    fun infoSet(key: String, obj: String) {
        loginInfo.datalist.addProperty(key, obj)
    }

    fun infoGet(key: String): String {
        if (loginInfo.datalist[key] != null) {
            return loginInfo.datalist[key].asString
        } else {
            return ""
        }
    }

    fun infoSetInt(key: String, obj: Int) {
        loginInfo.datalist.addProperty(key, obj)
    }

    fun infoGetInt(key: String): Int {
        if (loginInfo.datalist[key] != null) {
            return loginInfo.datalist[key].asInt
        } else {
            return 0
        }
    }
}