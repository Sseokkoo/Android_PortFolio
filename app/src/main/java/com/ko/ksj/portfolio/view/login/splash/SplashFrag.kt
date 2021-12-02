package com.ko.ksj.portfolio.view.login.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import com.ko.ksj.portfolio.R
import com.ko.ksj.portfolio.interfaces.AsyncTaskResponse
import com.ko.ksj.portfolio.interfaces.ResponseArrayData
import com.ko.ksj.portfolio.interfaces.ResponseMessage
import com.ko.ksj.portfolio.interfaces.ResponseObjData
import com.ko.ksj.portfolio.model.Config
import com.ko.ksj.portfolio.model.Title
import com.ko.ksj.portfolio.model.User
import com.ko.ksj.portfolio.repository.task.UserTask
import com.ko.ksj.portfolio.util.LocaleHelper
import java.util.*

class SplashFrag : Fragment() {
    val TAG = "SplashFrag"

    private lateinit var mContext: Context
    lateinit var navController: NavController

    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    var resp = ""
//    val viewModel by lazy {
//        ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_login_splash, container, false)

        return root
    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()
        navController = Navigation.findNavController(view)

//        val animation: Animation = AnimationUtils.loadAnimation(ApplicationProvider.getApplicationContext(), R.anim.scale_splash)
//        view.startAnimation(animation)

        pref = mContext.getSharedPreferences("Info", Context.MODE_PRIVATE)
        editor = pref.edit()

        val languageCode: String
        val countryCode: String

        if (pref.getString("lang", "").toString().isNotEmpty()) {
            languageCode = pref.getString("lang", "").toString()
            countryCode = pref.getString("country", "").toString()
        } else {
            val locale: Locale =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) resources.configuration.locales[0] else resources.configuration.locale

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                languageCode = Locale.getDefault(Locale.Category.DISPLAY).language
                countryCode = Locale.getDefault(Locale.Category.DISPLAY).country
            } else {
                languageCode = locale.language
                countryCode = Locale.getDefault().country
            }
        }

        LocaleHelper.setLocale(requireContext(), languageCode, countryCode)

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            if (pref.getBoolean("AutoCheck", false)) {
                UserTask(mContext).Login(
                    pref.getString("AutoEmail", "").toString(),
                    pref.getString("AutoPassword", "").toString(),
                    response, message, json
                )
            } else {
                navController.navigate(com.ko.ksj.portfolio.R.id.action_navigation_splash_to_navigation_intro)
            }
        }, 2000)

    }

    //    TODO: Retrofit
    val response = AsyncTaskResponse {
        Log.e(TAG, "AsyncTaskResponse :$it")
        resp = it
    }
    val message = ResponseMessage {
        Log.e(TAG, "message :$it")
    }
    val json = ResponseObjData {
        Log.e(TAG, "Json :$it")
        if (resp.equals("success")) {

//            val dataInfo = DataInfo()
//            dataInfo.infoSet("nick_name",it.get("nick_name").asString)
//            dataInfo.infoSet("email",it.get("email").asString)
//            dataInfo.infoSet("password",it.get("password").asString)
//            dataInfo.infoSetInt("type",it.get("type").asInt)
//            Config.getInstance().dataInfo = dataInfo

            User(
                it["nick_name"].asString,
                it["email"].asString,
                it["password"].asString,
                it["type"].asInt
            )

            navController.navigate(com.ko.ksj.portfolio.R.id.action_navigation_splash_to_navigation_home)
        } else {
            navController.navigate(com.ko.ksj.portfolio.R.id.action_navigation_splash_to_navigation_intro)
        }
    }
}