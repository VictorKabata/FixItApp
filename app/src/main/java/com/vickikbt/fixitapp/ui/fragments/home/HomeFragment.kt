package com.vickikbt.fixitapp.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentHomeBinding
import com.vickikbt.fixitapp.ui.adapters.HomeRecyclerviewAdapter
import com.vickikbt.fixitapp.ui.fragments.auth.UserViewModel
import com.vickikbt.fixitapp.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel.stateListener = this

        binding.fabHome.setOnClickListener {
            findNavController().navigate(R.id.home_to_upload)
        }

        binding.swipeRefreshLayoutHome.setOnRefreshListener {
            initUI()
        }

        initUI()

        return binding.root
    }

    private fun initUI() {
        viewModel.posts.observe(viewLifecycleOwner, { posts ->
            if (posts.isNullOrEmpty()) requireActivity().toast("No posts") //TODO: Replace with no post imageview

            else {
                userViewModel.getCurrentUser.observe(viewLifecycleOwner, { user ->
                    binding.recyclerViewHome.adapter = HomeRecyclerviewAdapter(requireActivity(), posts, user.id)
                })
            }
        })

        binding.swipeRefreshLayoutHome.isRefreshing = false
    }

    override fun onLoading() {
        binding.progressBarHome.show()
    }

    override fun onSuccess(message: String) {
        binding.progressBarHome.hide()
        //binding.swipeRefreshLayoutHome.
        if (isAdded) requireActivity().log(message)
    }

    override fun onFailure(message: String) {
        binding.progressBarHome.hide()

        binding.swipeRefreshLayoutHome.isRefreshing = false
        if (isAdded) {
            requireActivity().toast("Error fetching posts")
            requireActivity().log("Error: $message")
        }
    }
}