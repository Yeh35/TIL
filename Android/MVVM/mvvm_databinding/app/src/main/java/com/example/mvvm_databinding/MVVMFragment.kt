package com.example.mvvm_databinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mvvm_databinding.databinding.FragmentMvvmBinding

class MVVMFragment: Fragment() {

    private lateinit var binding: FragmentMvvmBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mvvm, container, false)
        val root = binding.root
        binding.user = User("김삿갓", "서울시", R.drawable.ic_launcher_background)
        
        return root
    }

}
