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
import kotlinx.android.synthetic.main.layout_login_title.*

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

    }

    fun Init() {
        Label = navController.currentDestination?.label.toString()

        val listener =
            NavController.OnDestinationChangedListener { controller, destination, arguments ->
                Label = navController.currentDestination?.label.toString()
            }

        navController.addOnDestinationChangedListener(listener)
        KakaoSdk.init(this, resources.getString(R.string.kakao_api))

        Title().label = TV_Login_Title_Name
        Title().leftIcon = IV_Login_Title_Left
        Title().rightIcon = IV_Login_Title_Right
    }
}