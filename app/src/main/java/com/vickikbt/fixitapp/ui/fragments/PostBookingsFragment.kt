package com.vickikbt.fixitapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentPostBookingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostBookingsFragment : Fragment() {

    private lateinit var binding:FragmentPostBookingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_post_booking, container, false)

        return binding.root
    }
}