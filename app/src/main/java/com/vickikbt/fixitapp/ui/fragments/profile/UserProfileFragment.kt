package com.vickikbt.fixitapp.ui.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentUserProfileBinding
import com.vickikbt.fixitapp.ui.fragments.auth.UserViewModel
import com.vickikbt.fixitapp.utils.log
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private lateinit var binding:FragmentUserProfileBinding
    private val args by navArgs<UserProfileFragmentArgs>()
    private val viewModel by viewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_user_profile, container, false)

        initUI()

        return binding.root
    }

    private fun initUI(){
        var ratings =0
        val userId=args.userId

        viewModel.fetchUserReviews(userId).observe(viewLifecycleOwner,{reviews->
            reviews.forEach { review->
                ratings+=review.rating/reviews.size
                requireActivity().log("Rating: $ratings")
            }
            binding.ratingBarProfile.rating=ratings.toFloat()
        })

        viewModel.fetchUser(userId).observe(viewLifecycleOwner,{user->
            Glide.with(requireActivity()).load(user.imageUrl).into(binding.userProfileImageView)
            binding.userProfileUsername.text=user.username
            binding.userProfileEmailAddress.text=user.email
            binding.userProfilePhoneNumber.text=user.phoneNumber
            binding.userProfileSpecialisation.text=user.specialisation

            binding.userProfileAddress.text=user.address
            binding.userProfileRegion.text=user.region
            binding.userProfileCountry.text=user.country
        })
    }
}