package com.ko.ksj.portfolio.repository.model

import java.io.Serializable

class QueryData : Serializable {
    var result: String? = null
    var message: String? = null

    private var data: ConfigData? = null

    constructor() {}
    constructor(result: String?, message: String?, data: ConfigData?) {
        this.result = result
        this.message = message
        this.data = data
    }

    fun getData(): ConfigData? {
        return data
    }

    fun setData(data: ConfigData?) {
        this.data = data
    }
}