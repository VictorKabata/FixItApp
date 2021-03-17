package com.vickikbt.fixitapp.ui.fragments.feeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentFeedsWorkBinding
import com.vickikbt.fixitapp.ui.adapters.FeedWorksRecyclerviewAdapter
import com.vickikbt.fixitapp.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedsWorkFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentFeedsWorkBinding
    private val viewModel: FeedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feeds_work, container, false)
        viewModel.stateListener = this

        initRecyclerview()

        return binding.root
    }

    private fun initRecyclerview() {
        viewModel.works.observe(viewLifecycleOwner, { works ->
            if (works.isNullOrEmpty()) binding.layoutNoWork.visibility=VISIBLE
            else {
                binding.layoutNoWork.visibility= GONE
                binding.recyclerviewFeedWork.adapter = FeedWorksRecyclerviewAdapter(requireActivity(), works)
            }
        })
    }

    override fun onLoading() {
        binding.progressBarFeedWorks.show()
    }

    override fun onSuccess(message: String) {
        binding.progressBarFeedWorks.hide()
        requireActivity().toast(message)
    }

    override fun onFailure(message: String) {
        binding.progressBarFeedWorks.hide()
        requireActivity().toast(message)
        requireActivity().log(message)
    }

}