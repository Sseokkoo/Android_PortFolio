package com.ko.ksj.portfolio.ViewModel

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ko.ksj.portfolio.Model.Title
import com.ko.ksj.portfolio.Model.User

class MainViewModel : ViewModel() {

    private val users: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().also {
            loadUsers()
        }
    }

    fun getUsers(): LiveData<List<User>> {
        return users
    }

    private fun loadUsers() {
        // Do an asynchronous operation to fetch users.
        var a = 1
    }

    private val titleSetting : MutableLiveData<List<Title>> by lazy {
        MutableLiveData<List<Title>>().also {
            getLabel()
            getIcon(true)
            getIcon(false)
        }
    }
    fun getTitle() : LiveData<List<Title>>{
        return titleSetting
    }
    private fun getLabel() : TextView {
        return Title().label
    }
    private fun getIcon(leftCheck : Boolean) : ImageView {
        return if(leftCheck){
            Title().leftIcon
        }else{
            Title().rightIcon
        }
    }

}