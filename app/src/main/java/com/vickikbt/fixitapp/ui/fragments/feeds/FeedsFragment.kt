package com.vickikbt.fixitapp.ui.fragments.feeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentFeedsBinding
import com.vickikbt.fixitapp.ui.adapters.FeedsViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedsFragment : Fragment() {

    private lateinit var binding: FragmentFeedsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feeds, container, false)

        initViewPager()

        return binding.root
    }

    private fun initViewPager() {
        binding.viewPagerFeeds.adapter = FeedsViewPagerAdapter(childFragmentManager)
        binding.tabsFeeds.setupWithViewPager(binding.viewPagerFeeds)
    }

}