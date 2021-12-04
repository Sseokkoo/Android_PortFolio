package com.ko.ksj.portfolio.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.ko.ksj.portfolio.model.Title
import com.ko.ksj.portfolio.R
import com.ko.ksj.portfolio.databinding.ActivityMainBinding
import com.ko.ksj.portfolio.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    lateinit var navController: NavController
    var TAG = "MainAct"

    lateinit var binding: ActivityMainBinding

    val viewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        Init()
    }

    fun Init() {
//        Label = navController.currentDestination?.label.toString()
        navController = FR_Main_nav_host.findNavController()
        viewModel.TitleIcon("", "")
        val listener =
            NavController.OnDestinationChangedListener { controller, destination, arguments ->
                viewModel.TitleUi(IV_Main_Title_Left, IV_Main_Title_Right)

                binding.title = Title(navController.currentDestination?.label.toString())
                viewModel.left_icon.observe(this, Observer {
                    binding.IVMainTitleLeft.setImageResource(viewModel.left_icon.value!!)
                })
                viewModel.right_icon.observe(this, Observer {
                    binding.IVMainTitleRight.setImageResource(viewModel.right_icon.value!!)
                })
//                binding.IVMainTitleLeft.setImageResource(viewModel.left_icon.value!!)
//                binding.IVMainTitleRight.setImageResource(viewModel.right_icon.value!!)
                binding.invalidateAll()
            }

        navController.addOnDestinationChangedListener(listener)
        val keyHash = Utility.getKeyHash(this)
        Log.e("확인", keyHash)
        KakaoSdk.init(this, resources.getString(R.string.kakao_api))

        binding.BNMainBottomNavigation.visibility = View.VISIBLE
        binding.BNMainBottomNavigation.setupWithNavController(navController)
    }

}