package com.vickikbt.fixitapp.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentProfileBinding
import com.vickikbt.fixitapp.ui.viewmodels.UserViewModel
import com.vickikbt.fixitapp.utils.Coroutines
import com.vickikbt.fixitapp.utils.log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.viewModel = viewModel //TODO: Check here for errors
        binding.lifecycleOwner = this //TODO: Check if removing brings in errors

        binding.buttonLogout.setOnClickListener {
            logoutUser()
        }

        Coroutines.main { initUI() }

        return binding.root
    }

    //Load up user ratings
    private suspend fun initUI(){
        var ratings =0

        viewModel.getCurrentUserReviews().observe(viewLifecycleOwner,{reviews->
            reviews.forEach { review->
                ratings+=review.rating/reviews.size
                requireActivity().log("Rating: $ratings")
            }
            binding.ratingBarProfile.rating=ratings.toFloat()
        })

        viewModel.getCurrentUser.observe(viewLifecycleOwner,{user->
            Glide.with(requireActivity()).load(user.imageUrl).into(binding.profileImageView)
        })
    }

    private fun logoutUser() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_options)

        val buttonYes: Button = dialog.findViewById(R.id.button_dialog_yes)
        val buttonNo: TextView = dialog.findViewById(R.id.textView_dialog_no)

        buttonNo.setOnClickListener { dialog.dismiss() }

        buttonYes.setOnClickListener {
            viewModel.logoutUser()
            dialog.dismiss()
            findNavController().navigate(R.id.profile_to_login)
        }

        dialog.show()
    }


}