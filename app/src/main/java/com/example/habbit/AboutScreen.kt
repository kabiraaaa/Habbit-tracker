package com.example.habbit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.habbit.databinding.AboutScreenBinding

class AboutScreen: Fragment(){

    private lateinit var binding: AboutScreenBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //val binding: MainScreenBinding = DataBindingUtil.inflate(inflater, R.layout.main_screen, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.about_screen, container, false)
        return binding.root
    }
}

