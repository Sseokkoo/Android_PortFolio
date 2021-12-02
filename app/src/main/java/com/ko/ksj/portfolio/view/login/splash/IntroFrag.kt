package com.ko.ksj.portfolio.view.login.splash

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ko.ksj.portfolio.R
import com.ko.ksj.portfolio.model.Title
import com.ko.ksj.portfolio.view.MainActivity

class IntroFrag : Fragment() {
    val TAG = "IntroFrag"

    private lateinit var mContext: Context
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_login_intro, container, false)

        return root
    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()
        navController = Navigation.findNavController(view)

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            navController.navigate(R.id.action_navigation_intro_to_navigation_login)
        }, 3000)

    }

}