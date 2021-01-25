package com.vickikbt.fixitapp.ui.fragments.work

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentWorkBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkFragment : Fragment() {

    private lateinit var binding: FragmentWorkBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_work, container, false)

        return binding.root
    }

}