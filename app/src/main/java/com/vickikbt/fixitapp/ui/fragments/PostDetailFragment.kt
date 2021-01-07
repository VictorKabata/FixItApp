package com.vickikbt.fixitapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentPostDetailBinding
import com.vickikbt.fixitapp.ui.viewmodels.PostViewModel
import com.vickikbt.fixitapp.ui.viewmodels.UserViewModel
import com.vickikbt.fixitapp.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentPostDetailBinding
    private val postViewModel by viewModels<PostViewModel>()
    private val userViewModel by viewModels<UserViewModel>()
    private val args: PostDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_detail, container, false)
        postViewModel.stateListener = this

        binding.buttonContact.setOnClickListener {
            //val action = PostDetailFragmentDirections.actionPostDetailFragmentToChatFragment(user.id)
            //findNavController().navigate(action)
        }

        initUI()

        return binding.root
    }

    private fun initUI() {
        val postId = args.PostId

        postViewModel.getPost(postId).observe(viewLifecycleOwner, { post ->
            val user = post.user

            Glide.with(requireActivity()).load(post.imageUrl).into(binding.imageViewPostDetail)
            Glide.with(requireActivity()).load(user.imageUrl).into(binding.userImageView)

            binding.userUsername.text = user.username
            binding.userEmail.text = user.email
            binding.textViewPostCategory.text = post.category
            binding.textViewPostDescription.text = post.description
            binding.textViewPostBudget.text = post.budget

            val currentUserId = userViewModel.getLoggedInUser.value!!.id
            if (currentUserId != user.id) {
                binding.userImageView.setOnClickListener {
                    //val action = PostDetailFragmentDirections.actionPostDetailFragmentToUserProfileFragment(user.id)
                    //findNavController().navigate(action)
                }
            } else {
                binding.buttonBook.visibility = View.GONE
                binding.buttonContact.visibility = View.GONE
            }

        })

    }

    override fun onLoading() {
        binding.progressBarPostDetail.show()
    }

    override fun onSuccess(message: String) {
        binding.progressBarPostDetail.hide()
        requireActivity().log(message)
    }

    override fun onFailure(message: String) {
        binding.progressBarPostDetail.hide()
        requireActivity().toast(message)
        requireActivity().log(message)
    }
}