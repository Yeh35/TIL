package com.example.mvvm_databinding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Address: ViewModel {

    private val _address = MutableLiveData<String>()
//    val address: LiveData<String>
//        get() = _address
    var address: String = ""

    constructor(address: String): super() {
        _address.value = address
        this.address = address
    }

}
