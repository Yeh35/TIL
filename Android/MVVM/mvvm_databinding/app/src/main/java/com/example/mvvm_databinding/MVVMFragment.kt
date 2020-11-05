package com.example.mvvm_databinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvm_databinding.databinding.FragmentMvvmBinding

class MVVMFragment: Fragment() {

    private lateinit var binding: FragmentMvvmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mvvm, container, false)
        val root = binding.root

        val user = User()
        val address = Address("서울시")

        binding.user = user
        binding.address = address

        user.setProfileURL(R.drawable.ic_launcher_foreground)

        binding.tvAddress.setOnClickListener {
            address.address = "서울시 서대문구"
            // 화면 업데이트가 안됨!
        }

        return root
    }

}
