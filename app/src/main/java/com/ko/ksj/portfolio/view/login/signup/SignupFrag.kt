package com.ko.ksj.portfolio.view.login.signup

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
import com.ko.ksj.portfolio.databinding.FragmentLoginSignUpBinding
import com.ko.ksj.portfolio.interfaces.AsyncTaskResponse
import com.ko.ksj.portfolio.interfaces.ResponseMessage
import com.ko.ksj.portfolio.interfaces.ResponseStrData
import com.ko.ksj.portfolio.model.Config
import com.ko.ksj.portfolio.model.User
import com.ko.ksj.portfolio.repository.task.UserTask
import com.ko.ksj.portfolio.viewmodel.LoginViewModel

class SignupFrag : Fragment() {
    val TAG = "Signup"

    lateinit var mContext: Context
    lateinit var navController: NavController

//    lateinit var pref: SharedPreferences
//    lateinit var editor: SharedPreferences.Editor

    private lateinit var binding: FragmentLoginSignUpBinding

    var btncheck1 = false
    var btncheck2 = false
    var btncheck3 = false
    var btncheck4 = false

    val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val root = inflater.inflate(R.layout.fragment_login_findtoid, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_sign_up, container, false)
        val root = binding.root

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            navController.navigate(R.id.action_navigation_signup_to_navigation_login)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()
        navController = Navigation.findNavController(view)

        viewModel.TitleIcon("back", "check")
Log.e("확인","${Config.getInstance().dataInfo.infoGet("sns_email")} ${Config.getInstance().dataInfo.infoGet("sns_type")}")
        if (Config.getInstance().dataInfo.infoGet("sns_email").isNotEmpty()) {
            binding.user = User(
                binding.EDTLoginSignupNickName.text.toString(),
                Config.getInstance().dataInfo.infoGet("sns_email"),
                binding.EDTLoginSignupPassword.text.toString(),
                Config.getInstance().dataInfo.infoGetInt("sns_type")
            )
            binding.EDTLoginSignupEmail.isEnabled = false
        }
//        Config.getInstance().dataInfo.infoGetInt("sns_type")

        viewModel.IV_Left.setOnClickListener {
            navController.navigate(R.id.action_navigation_signup_to_navigation_login)
        }
        viewModel.IV_Right.setOnClickListener {
            UserTask(mContext).SignUp(
                binding.user?.nick_name!!, binding.user?.email!!, binding.user?.password!!, binding.user?.type!!,
                response, message
            )
        }

        btnChange()

        binding.BTNLoginSignupBTN.setOnClickListener {
            UserTask(mContext).SignUp(
                binding.user?.nick_name!!, binding.user?.email!!, binding.user?.password!!, binding.user?.type!!,
                response, message
            )
        }
        binding.EDTLoginSignupNickName.addTextChangedListener {
            binding.user = User(
                binding.EDTLoginSignupNickName.text.toString(),
                binding.EDTLoginSignupEmail.text.toString(),
                binding.EDTLoginSignupPassword.text.toString(),
                Config.getInstance().dataInfo.infoGetInt("sns_type")
            )

            if (binding.user?.nick_name!!.contains(" ")) {
                binding.EDTLoginSignupNickName.setText(binding.user!!.nick_name.replace(" ", ""))
                Toast.makeText(mContext, resources.getString(R.string.common_not_empty), Toast.LENGTH_SHORT).show()
                binding.invalidateAll()
            } else {
                if (binding.user?.nick_name!!.isEmpty()) {
                    binding.TVLoginSignupNickNameSUb.setTextColor(ContextCompat.getColor(mContext, R.color.Red))
                    btncheck1 = false
                    btnChange()
                } else {
                    binding.TVLoginSignupNickNameSUb.setTextColor(ContextCompat.getColor(mContext, R.color.Light_Gray))
                    btncheck1 = true
                    btnChange()
                }
            }
        }

        binding.EDTLoginSignupEmail.addTextChangedListener {
            binding.user = User(
                binding.EDTLoginSignupNickName.text.toString(),
                binding.EDTLoginSignupEmail.text.toString(),
                binding.EDTLoginSignupPassword.text.toString(),
                Config.getInstance().dataInfo.infoGetInt("sns_type")
            )

            if (binding.user!!.email.contains(" ")) {
                binding.EDTLoginSignupEmail.setText(binding.user!!.email.replace(" ", ""))
                Toast.makeText(mContext, resources.getString(R.string.error_email_no_empty), Toast.LENGTH_SHORT).show()
            } else {
                if (viewModel.SignupEmail(binding.user!!.email, mContext)) {
                    btncheck2 = true
                    btnChange()
                    binding.TVLoginSignupEmailSUb.text = viewModel.live_send_string.value
                    binding.TVLoginSignupEmailSUb.setTextColor(ContextCompat.getColor(mContext, R.color.Light_Gray))
                } else {
                    btncheck2 = false
                    btnChange()
                    binding.TVLoginSignupEmailSUb.text = viewModel.live_send_string.value
                    binding.TVLoginSignupEmailSUb.setTextColor(ContextCompat.getColor(mContext, R.color.Red))
                }
            }
        }
        binding.EDTLoginSignupPassword.addTextChangedListener {
            binding.user = User(
                binding.EDTLoginSignupNickName.text.toString(),
                binding.EDTLoginSignupEmail.text.toString(),
                binding.EDTLoginSignupPassword.text.toString(),
                Config.getInstance().dataInfo.infoGetInt("sns_type")
            )

            if (binding.user!!.password.contains(" ")) {
                binding.EDTLoginSignupPassword.setText(binding.user!!.password.replace(" ", ""))
                Toast.makeText(mContext, resources.getString(R.string.error_pass_no_empty), Toast.LENGTH_SHORT).show()
            } else {
                if (viewModel.SignupPass(binding.user!!.password)) {
                    btncheck3 = true
                    btnChange()
                    binding.TVLoginSignupPasswordSub.setTextColor(ContextCompat.getColor(mContext, R.color.Light_Gray))
                } else {
                    btncheck3 = false
                    btnChange()
                    binding.TVLoginSignupPasswordSub.setTextColor(ContextCompat.getColor(mContext, R.color.Red))
                }
            }
        }
        binding.EDTLoginSignupPassOk.addTextChangedListener {
            binding.user = User(
                binding.EDTLoginSignupNickName.text.toString(),
                binding.EDTLoginSignupEmail.text.toString(),
                binding.EDTLoginSignupPassword.text.toString(),
                Config.getInstance().dataInfo.infoGetInt("sns_type")
            )

            if (binding.EDTLoginSignupPassOk.text.contains(" ")) {
                binding.EDTLoginSignupPassOk.setText(binding.EDTLoginSignupPassOk.text.toString().replace(" ", ""))
                Toast.makeText(mContext, resources.getString(R.string.common_not_empty), Toast.LENGTH_SHORT).show()
            } else {
                if (viewModel.SignupPassOk(binding.user?.password!!, binding.EDTLoginSignupPassOk.text.toString())) {
                    btncheck4 = true
                    btnChange()
                    binding.TVLoginSignupPassOkSub.setTextColor(ContextCompat.getColor(mContext, R.color.Light_Gray))
                } else {
                    btncheck4 = false
                    btnChange()
                    binding.TVLoginSignupPassOkSub.setTextColor(ContextCompat.getColor(mContext, R.color.Red))
                }
            }
        }
    }

    fun btnChange() {
        if (btncheck1 && btncheck2 && btncheck3 && btncheck4) {
            binding.BTNLoginSignupBTN.isEnabled = true
            viewModel.getTitle(false).isEnabled = true
            binding.BTNLoginSignupBTN.setBackgroundColor(ContextCompat.getColor(mContext, R.color.Main_Blue))
        } else {
            binding.BTNLoginSignupBTN.isEnabled = false
            viewModel.getTitle(false).isEnabled = false
            binding.BTNLoginSignupBTN.setBackgroundColor(ContextCompat.getColor(mContext, R.color.Light_Gray))
        }
    }

    // TODO: Retrofit
    val response = AsyncTaskResponse {
        Log.e(TAG, "AsyncTaskPesponse : $it")
        if (it.equals("success")) {
            val dialog = androidx.appcompat.app.AlertDialog.Builder(mContext)

            dialog.setTitle(resources.getString(R.string.common_signup))
                .setMessage(resources.getString(R.string.sign_success))
                .setPositiveButton(resources.getString(R.string.common_confirm), null)

            val alertDialog = dialog.create()

            alertDialog.setTitle(resources.getString(R.string.common_success))
            alertDialog.show()

            alertDialog.setOnDismissListener {
                navController.navigate(R.id.action_navigation_signup_to_navigation_login)
            }
        }
    }
    val message = ResponseMessage {
        Log.e(TAG, "message : $it")
        Toast.makeText(mContext, it, Toast.LENGTH_SHORT).show()
        if (it.contains("Duplicate entry")){
            val str = it.split("'")
            val other_user = str[1].split("-")
            val dialog = androidx.appcompat.app.AlertDialog.Builder(mContext)

            dialog.setTitle(resources.getString(R.string.common_signup))
                .setMessage("${resources.getString(R.string.sign_use)}\n${resources.getString(R.string.common_nickname)} : ${other_user[0]}\n${resources.getString(R.string.common_email)} : ${other_user[1]}")
                .setPositiveButton(resources.getString(R.string.common_confirm), null)

            val alertDialog = dialog.create()

            alertDialog.setTitle(resources.getString(R.string.common_failed))
            alertDialog.show()

        }
    }

}