package com.vickikbt.fixitapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentUserProfileBinding
import com.vickikbt.fixitapp.ui.viewmodels.UserViewModel
import com.vickikbt.fixitapp.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentUserProfileBinding
    private val viewModel by viewModels<UserViewModel>()
    private val args: UserProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)

        initUI()

        return binding.root
    }

    private fun initUI() {
        val userId = args.UserId
        var rating=0

        viewModel.fetchUser(userId).observe(viewLifecycleOwner, { user ->
            Glide.with(requireActivity()).load(user.imageUrl).into(binding.userProfileImageView)
            binding.userProfileUsername.text = user.username
            binding.userProfileEmailAddress.text = user.email
            binding.userProfilePhoneNumber.text = user.phoneNumber
            binding.userProfileSpecialisation.text = user.specialisation
            binding.userProfileAddress.text = user.address
            binding.userProfileRegion.text = user.region
            binding.userProfileCountry.text = user.country
        })

        viewModel.fetchUserReviews(userId)
        viewModel.userReviews.observe(viewLifecycleOwner, {reviews->
            reviews.forEach {review->
                rating+=review.rating/reviews.size

            }
            binding.userProfileRating.rating=rating.toFloat()
        })
    }

    override fun onLoading() {
        binding.progressBarProfile.show()
    }

    override fun onSuccess(message: String) {
        binding.progressBarProfile.hide()
        requireActivity().log(message)
    }

    override fun onFailure(message: String) {
        binding.progressBarProfile.hide()
        requireActivity().toast(message)
        requireActivity().log(message)
    }


}