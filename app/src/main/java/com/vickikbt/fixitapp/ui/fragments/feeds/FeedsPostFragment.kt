package com.vickikbt.fixitapp.ui.fragments.feeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentFeedsPostBinding
import com.vickikbt.fixitapp.ui.adapters.FeedPostRecyclerviewAdapter
import com.vickikbt.fixitapp.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedsPostFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentFeedsPostBinding
    private val viewModel: FeedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feeds_post, container, false)
        viewModel.stateListener = this

        initRecyclerview()

        return binding.root
    }

    private fun initRecyclerview() {
        viewModel.posts.observe(viewLifecycleOwner, { posts ->
            if (posts.isNullOrEmpty()) requireActivity().toast("No posts")
            else {
                binding.recyclerviewFeedPosts.adapter =
                    FeedPostRecyclerviewAdapter(requireActivity(), posts)
            }
        })
    }

    override fun onLoading() {
        binding.progressBarFeedPosts.show()
    }

    override fun onSuccess(message: String) {
        binding.progressBarFeedPosts.hide()
        requireActivity().toast(message)
    }

    override fun onFailure(message: String) {
        binding.progressBarFeedPosts.hide()
        requireActivity().toast(message)
        requireActivity().log(message)
    }
}