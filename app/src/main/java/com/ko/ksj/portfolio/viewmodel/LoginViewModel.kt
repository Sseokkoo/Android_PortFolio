package com.ko.ksj.portfolio.viewmodel

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ko.ksj.portfolio.R

class LoginViewModel : ViewModel() {

    private val live_string = MutableLiveData<String>().apply {}
    private val live_string2 = MutableLiveData<String>().apply {}
    private val live_src = MutableLiveData<Int>().apply {}
    private val left_live = MutableLiveData<Int>().apply {}
    private val right_live = MutableLiveData<Int>().apply {}

    val left_icon : LiveData<Int> = left_live
    val right_icon : LiveData<Int> = right_live
    val live_send_src : LiveData<Int> = live_src
    val live_send_string: LiveData<String> = live_string
    val live_send_string2: LiveData<String> = live_string2

    fun EmailError(email: String, context: Context): Boolean {
        return if(!email.contains("@") || email.isEmpty() || !email.contains(".")){
            live_string.value = context.getString(R.string.error_email_form)
            false
        }else{
            live_string.value = context.getString(R.string.common_empty)
            true
        }
    }

    fun PwError(password: String, context: Context): Boolean {
        return if(!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[[A-Z][a-z][0-9]$@$!%*+#?&]{8,20}$".toRegex())
            || password.contains(" ") || password.isEmpty()){
            live_string2.value = context.resources.getString(R.string.error_pass_form)
            false
        }else{
            live_string2.value = context.resources.getString(R.string.common_empty)
            true
        }
    }

    fun FindToId(boolean: Boolean, context: Context){
        if (boolean) {
            live_string.value = context.resources.getString(R.string.findtoid_have_not)
            live_string2.value = context.resources.getString(R.string.findtoid_main)
            live_src.value = R.drawable.ic_baseline_block_80
        }else{
            live_string.value = context.resources.getString(R.string.common_empty)
            live_string2.value = context.resources.getString(R.string.common_empty)
            live_src.value = 0
        }
    }
    fun SignupEmail(email: String, context: Context): Boolean {
        return if(!email.contains("@") || email.isEmpty() || !email.contains(".")){
            live_string.value = context.getString(R.string.error_email_form)
            false
        }else{
            live_string.value = context.getString(R.string.sign_email_sub)
            true
        }
    }
    fun SignupPass(password: String): Boolean {
        return !(!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[[A-Z][a-z][0-9]$@$!%*+#?&]{8,20}$".toRegex())
                || password.contains(" ") || password.isEmpty())
    }
    fun SignupPassOk(password: String, passok : String): Boolean {
        return passok.equals(password)
    }

    fun TitleIcon(left: String, right: String){
        when(left) {
            "" -> left_live.value = 0
            "back" -> left_live.value = R.drawable.ic_baseline_arrow_back_24
            "close" -> left_live.value = R.drawable.ic_baseline_close_24
        }
        when (right){
            "" -> right_live.value = 0
            "check" -> right_live.value = R.drawable.ic_baseline_check_24
            "edit" -> right_live.value = R.drawable.ic_baseline_edit_24
        }
    }

    lateinit var IV_Left : ImageView
    lateinit var IV_Right : ImageView

    fun TitleUi(left: ImageView, right: ImageView){
        IV_Left = left
        IV_Right = right
    }
//    @BindingAdapter({"iamgeUrl"})
//    fun imgload(imageView: ImageView, resid : Int) {
//        imageView.setImageResource(resid)
//    }

    fun getTitle(left_true_right_false : Boolean): ImageView {
        if (left_true_right_false){
            return IV_Left
        }else{
            return IV_Right
        }
    }
}