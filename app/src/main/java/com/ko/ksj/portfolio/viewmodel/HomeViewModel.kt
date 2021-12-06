package com.ko.ksj.portfolio.viewmodel

import android.content.Context
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.ko.ksj.portfolio.R

class HomeViewModel : ViewModel() {

    private val live_string = MutableLiveData<String>().apply {}
    private val left_live = MutableLiveData<Int>().apply {}
    private val right_live = MutableLiveData<Int>().apply {}

    val left_icon : LiveData<Int> = left_live
    val right_icon : LiveData<Int> = right_live
    val live_send_string: LiveData<String> = live_string


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
    lateinit var NV_Controller: NavController
    lateinit var bottomNavigationView: BottomNavigationView

    fun TitleUi(left: ImageView, right: ImageView, navController: NavController){
        IV_Left = left
        IV_Right = right
        NV_Controller = navController
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