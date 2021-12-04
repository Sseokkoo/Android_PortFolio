package com.ko.ksj.portfolio.view.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ko.ksj.portfolio.R
import com.ko.ksj.portfolio.databinding.FragmentLoginLoginBinding
import com.ko.ksj.portfolio.model.DataInfo
import com.ko.ksj.portfolio.viewmodel.LoginViewModel

class HomeFrag : Fragment() {
    val TAG = "HomeFrag"

    private lateinit var mContext: Context
    lateinit var navController: NavController

    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: FragmentLoginLoginBinding

    var result = ""

    val dataInfo = DataInfo()

    val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val root = inflater.inflate(R.layout.fragment_login_login, container, false)
        var time: Long = 0

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_login, container, false)
        val root = binding.root

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val now = System.currentTimeMillis()
            val result = now - time

            if (result < 2000) {
                activity?.finish()
            } else {
                Toast.makeText(requireContext(), resources.getString(R.string.common_backpress), Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis()
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.TitleIcon("", "")

        mContext = requireContext()
        navController = Navigation.findNavController(view)

        pref = mContext.getSharedPreferences("Info", Context.MODE_PRIVATE)
        editor = pref.edit()
    }
}