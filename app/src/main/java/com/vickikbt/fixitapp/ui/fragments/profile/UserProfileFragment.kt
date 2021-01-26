package com.vickikbt.fixitapp.ui.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentUserProfileBinding
import com.vickikbt.fixitapp.ui.fragments.auth.UserProfileFragmentArgs
import com.vickikbt.fixitapp.ui.fragments.auth.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private lateinit var binding: FragmentUserProfileBinding
    private val viewModel by viewModels<UserViewModel>()
    private val args: UserProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_user_profile, container, false)

        initUI()

        return root
    }

    private fun initUI() {
        viewModel.fetchUser(args.UserId).observe(viewLifecycleOwner, { user ->
            Glide.with(requireActivity()).load(user.imageUrl).into(binding.userProfileImageView)
            binding.userProfileUsername.text = user.username
            binding.userProfileEmailAddress.text = user.email
            binding.userProfilePhoneNumber.text = user.phoneNumber
            binding.userProfileSpecialisation.text = user.specialisation
            binding.userProfileAddress.text = user.address
            binding.userProfileRegion.text = user.region
            binding.userProfileCountry.text = user.country
        })
    }

}