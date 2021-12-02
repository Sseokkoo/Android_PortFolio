package com.ko.ksj.portfolio.view.login.findtoid

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ko.ksj.portfolio.R
import com.ko.ksj.portfolio.databinding.FragmentLoginFindtoidBinding
import com.ko.ksj.portfolio.interfaces.AsyncTaskResponse
import com.ko.ksj.portfolio.interfaces.ResponseMessage
import com.ko.ksj.portfolio.interfaces.ResponseStrData
import com.ko.ksj.portfolio.model.User
import com.ko.ksj.portfolio.repository.task.UserTask
import com.ko.ksj.portfolio.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login_findtoid.*

class FindToIdFrag : Fragment() {
    val TAG = "FindToId"

    lateinit var mContext: Context
    lateinit var navController: NavController

//    lateinit var pref: SharedPreferences
//    lateinit var editor: SharedPreferences.Editor

    private lateinit var binding: FragmentLoginFindtoidBinding
    var result = ""
    val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val root = inflater.inflate(R.layout.fragment_login_findtoid, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_findtoid, container, false)
        val root = binding.root
        viewModel.TitleIcon("back", "check")
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            navController.navigate(R.id.action_navigation_findtoid_to_navigation_login)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()
        navController = Navigation.findNavController(view)

        btnChange(false)
        Log.e("확인", "${viewModel.left_icon.value!!}")

        binding.EDTLoginFindToIDNickName.addTextChangedListener {
            binding.user = User(binding.EDTLoginFindToIDNickName.text.toString(), "", "", 0)
            if (binding.user?.nick_name!!.isNotEmpty()) {
                btnChange(true)
            } else {
                btnChange(false)
            }
        }

        viewModel.getTitle(true).setOnClickListener {
            navController.navigate(R.id.action_navigation_findtoid_to_navigation_login)
        }
        viewModel.getTitle(false).setOnClickListener {
            when (BTN_Login_FindToId_BTN.text.toString()) {
                resources.getString(R.string.common_complet) -> {
                    if (binding.user?.nick_name!!.contains(" ")) {
                        binding.EDTLoginFindToIDNickName.setText(binding.user?.nick_name!!.replace(" ", ""))
                        Toast.makeText(mContext, resources.getString(R.string.common_not_empty), Toast.LENGTH_SHORT).show()
                    } else {
                        UserTask(mContext).FindToId(
                            binding.user?.nick_name!!,
                            response,
                            message,
                            json
                        )
                    }
                }
                resources.getString(R.string.common_do_signup) -> {
                    navController.navigate(R.id.action_navigation_findtoid_to_navigation_signup)
                }
            }
        }

        binding.BTNLoginFindToIdBTN.setOnClickListener {
//                임시 버튼 전환
            when (BTN_Login_FindToId_BTN.text.toString()) {
                resources.getString(R.string.common_complet) -> {
                    when (BTN_Login_FindToId_BTN.text.toString()) {
                        resources.getString(R.string.common_complet) -> {
                            if (binding.user?.nick_name!!.contains(" ")) {
                                binding.EDTLoginFindToIDNickName.setText(binding.user?.nick_name!!.replace(" ", ""))
                                Toast.makeText(mContext, resources.getString(R.string.common_not_empty), Toast.LENGTH_SHORT).show()
                            } else {
                                UserTask(mContext).FindToId(
                                    binding.user?.nick_name!!,
                                    response,
                                    message,
                                    json
                                )
                            }
                        }
                        resources.getString(R.string.common_do_signup) -> {
                            navController.navigate(R.id.action_navigation_findtoid_to_navigation_signup)
                        }
                    }
                }
                resources.getString(R.string.common_do_signup) -> {
                    navController.navigate(R.id.action_navigation_findtoid_to_navigation_signup)
                }
            }
        }
    }

    fun btnChange(boolean: Boolean) {
        if (boolean) {
            binding.BTNLoginFindToIdBTN.isEnabled = true
            binding.BTNLoginFindToIdBTN.setBackgroundColor(ContextCompat.getColor(mContext, R.color.Main_Blue))
            viewModel.getTitle(false).isEnabled = true
        } else {
            binding.BTNLoginFindToIdBTN.isEnabled = false
            viewModel.getTitle(false).isEnabled = false
            binding.BTNLoginFindToIdBTN.setBackgroundColor(ContextCompat.getColor(mContext, R.color.Light_Gray))
        }
    }

    // TODO: Retrofit
    val response = AsyncTaskResponse {
        Log.e(TAG, "AsyncTaskPesponse : $it")
        result = it

    }
    val message = ResponseMessage {
        Log.e(TAG, "message : $it")
        Toast.makeText(mContext, it, Toast.LENGTH_SHORT).show()
    }
    val json = ResponseStrData {
        Log.e(TAG, "Json : $it")
        if (result.equals("success")) {
            viewModel.FindToId(false, mContext)
            binding.LLLoginFindToIdAuth1.visibility = View.VISIBLE

//            if (EDT_Login_FindToID_NickName.text.contains("gmail.com")){
//                Signup_kind = resources.getString(R.string.cmmn_join_gmail)
//            }else if (EDT_Login_FindToID_NickName.text.contains("naver.com")){
//                Signup_kind = resources.getString(R.string.cmmn_join_naver)
//            }else if (EDT_Login_FindToID_NickName.text.contains("kakao.com")){
////               카카오는 가입한 이메일이 고정적이지 않음
//            }
            binding.IVLoginFindToIDNotAuth1.setImageResource(viewModel.live_send_src.value!!)
            binding.TVLoginFindToIDNotAuth1.text = viewModel.live_send_string.value
            binding.TVLoginFindToIDNotAuth2.text = viewModel.live_send_string2.value
            val dialog = androidx.appcompat.app.AlertDialog.Builder(mContext)

            dialog.setTitle(resources.getString(R.string.common_email_find))
                dialog.setMessage("${resources.getString(R.string.findtoid_result)} '${it}' ${resources.getString(R.string.common_that)}")
                dialog.setPositiveButton(resources.getString(R.string.common_confirm), null)

            val alertDialog = dialog.create()

            alertDialog.setTitle(resources.getString(R.string.common_email))
            alertDialog.show()

            binding.BTNLoginFindToIdBTN.text = resources.getString(R.string.common_complet)
        } else {
            viewModel.FindToId(true, mContext)
            binding.LLLoginFindToIdAuth1.visibility = View.GONE

            binding.IVLoginFindToIDNotAuth1.setImageResource(viewModel.live_send_src.value!!)
            binding.TVLoginFindToIDNotAuth1.text = viewModel.live_send_string.value
            binding.TVLoginFindToIDNotAuth2.text = viewModel.live_send_string2.value
            BTN_Login_FindToId_BTN.text = resources.getString(R.string.common_do_signup)
            btnChange(true)
        }
    }

}