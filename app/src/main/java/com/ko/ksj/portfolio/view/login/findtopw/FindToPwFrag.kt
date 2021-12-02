package com.ko.ksj.portfolio.view.login.findtopw

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ko.ksj.portfolio.R
import com.ko.ksj.portfolio.databinding.FragmentLoginFindtopwBinding
import com.ko.ksj.portfolio.interfaces.AsyncTaskResponse
import com.ko.ksj.portfolio.interfaces.ResponseMessage
import com.ko.ksj.portfolio.interfaces.ResponseStrData
import com.ko.ksj.portfolio.model.User
import com.ko.ksj.portfolio.repository.task.UserTask
import com.ko.ksj.portfolio.util.EmailHelper
import com.ko.ksj.portfolio.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login_findtopw.*

class FindToPwFrag : Fragment() {
    val TAG = "FindToPw"

    private lateinit var mContext: Context
    lateinit var navController: NavController

    private lateinit var binding: FragmentLoginFindtopwBinding

    var result = ""

    val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val root = inflater.inflate(R.layout.fragment_login_findtopw, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_findtopw, container, false)
        val root = binding.root
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            navController.navigate(R.id.action_navigation_findtopw_to_navigation_login)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()
        navController = Navigation.findNavController(view)

        viewModel.TitleIcon("back", "check")
        viewModel.getTitle(true).setOnClickListener {
            navController.navigate(R.id.action_navigation_findtopw_to_navigation_login)
        }
        viewModel.getTitle(false).setOnClickListener {
            UserTask(mContext).FindToPw(
                binding.user?.email!!,
                response, message, json
            )
        }
        btnChange(false)

        binding.EDTLoginFindToPwEmail.addTextChangedListener {
            binding.user = User("", binding.EDTLoginFindToPwEmail.text.toString(), "", 0)
            if (binding.user?.email!!.contains(" ")) {
                binding.EDTLoginFindToPwEmail.setText(binding.user?.email!!.replace(" ", ""))
                Toast.makeText(mContext, resources.getString(R.string.error_email_no_empty), Toast.LENGTH_SHORT).show()
            } else {
                if (!viewModel.EmailError(binding.user?.email!!, mContext)) {
                    btnChange(false)

                    binding.TVLoginFindToPwError.text = viewModel.live_send_string.value
                } else {
                    btnChange(true)
                    binding.TVLoginFindToPwError.text = viewModel.live_send_string.value
                }
                binding.invalidateAll()
            }
        }

        binding.BTNLoginFindToPwBTN.setOnClickListener {
            UserTask(mContext).FindToPw(
                binding.user?.email!!,
                response, message, json
            )
        }


    }

    fun btnChange(boolean: Boolean) {
        if (boolean) {
            binding.BTNLoginFindToPwBTN.isEnabled = true
            viewModel.getTitle(false).isEnabled = true
            binding.BTNLoginFindToPwBTN.setBackgroundColor(ContextCompat.getColor(mContext, R.color.Main_Blue))
        } else {
            binding.BTNLoginFindToPwBTN.isEnabled = false
            viewModel.getTitle(false).isEnabled = false
            binding.BTNLoginFindToPwBTN.setBackgroundColor(ContextCompat.getColor(mContext, R.color.Light_Gray))
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
            EmailHelper(
                resources.getString(R.string.findtopw_email_title),
                "'${binding.user?.email!!}' ${resources.getString(R.string.findtopw_email_body)}'$it' ${resources.getString(R.string.common_that)}",
                binding.user?.email!!
            ).sendEmail(mContext)

            val dialog = androidx.appcompat.app.AlertDialog.Builder(mContext)

            dialog.setTitle(resources.getString(R.string.common_findtopw))
                .setMessage(resources.getString(R.string.findtopw_send_email))
                .setPositiveButton(resources.getString(R.string.common_confirm), null)

            val alertDialog = dialog.create()

            alertDialog.setTitle(resources.getString(R.string.common_email))
            alertDialog.show()

            var pw = it
            alertDialog.setOnDismissListener {
                val dialog = androidx.appcompat.app.AlertDialog.Builder(mContext)

                dialog.setTitle(resources.getString(R.string.common_findtopw))
                    .setMessage("${resources.getString(R.string.findtopw_smtp_dont)} $pw ${resources.getString(R.string.common_that)}")
                    .setPositiveButton(resources.getString(R.string.common_confirm), null)

                val alertDialog = dialog.create()

                alertDialog.setTitle(resources.getString(R.string.common_email))
                alertDialog.show()

            }
        } else {
            Toast.makeText(mContext, resources.getString(R.string.findtopw_not_match), Toast.LENGTH_SHORT).show()
        }
    }
}