package com.ko.ksj.portfolio.View

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.kakao.sdk.common.KakaoSdk
import com.ko.ksj.portfolio.Model.Title
import com.ko.ksj.portfolio.Model.User
import com.ko.ksj.portfolio.R
import com.ko.ksj.portfolio.ViewModel.MainViewModel

class MainActivity : AppCompatActivity() {


    lateinit var navController: NavController
    var Label = ""

    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    var TAG = "MainAct"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Init()

        val model: MainViewModel by viewModels()
//        model.getUsers().observe(this, Observer<List<User>> { users ->
//            // update UI
//        })
navController
        model.getTitle().observe(this, Observer{

        })
    }

    fun Init() {
        Label = navController.currentDestination?.label.toString()

        val listener =
            NavController.OnDestinationChangedListener { controller, destination, arguments ->
                Label = navController.currentDestination?.label.toString()
                setting()
            }

        navController.addOnDestinationChangedListener(listener)

        KakaoSdk.init(this, resources.getString(R.string.kakao_api))
    }

    fun setting() {

    }
}