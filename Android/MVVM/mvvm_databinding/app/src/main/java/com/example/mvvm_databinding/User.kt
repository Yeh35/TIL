package com.example.mvvm_databinding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// this view model
class User: ViewModel() {
    private val _name: MutableLiveData<String> = MutableLiveData()
    val name: LiveData<String>
        get() = _name

    private val _profileURL = MutableLiveData<Int>()
    val profileURL: LiveData<Int>
        get() = _profileURL

    init {
        _name.value = "김삿갓"
        _profileURL.value = -1
    }

    fun setProfileURL(i: Int) {
        _profileURL.value = i
    }
}
