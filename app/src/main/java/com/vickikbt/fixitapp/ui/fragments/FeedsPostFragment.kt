package com.vickikbt.fixitapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vickikbt.fixitapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedsPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_feeds_post, container, false)
    }
}