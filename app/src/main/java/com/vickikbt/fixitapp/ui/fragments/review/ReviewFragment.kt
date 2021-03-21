package com.vickikbt.fixitapp.ui.fragments.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentReviewBinding
import com.vickikbt.fixitapp.ui.adapters.ReviewsRecyclerviewAdapter
import com.vickikbt.fixitapp.ui.fragments.auth.UserViewModel

class ReviewFragment : Fragment() {

    private val viewModel by activityViewModels<UserViewModel>()
    private lateinit var binding:FragmentReviewBinding
    private val args by navArgs<ReviewFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_review, container, false)

        initUI()

        return binding.root
    }

    private fun initUI(){
        viewModel.fetchUserReviews(args.userId).observe(viewLifecycleOwner,{reviews->
            if (reviews.isNullOrEmpty()) binding.layoutNoReview.visibility=VISIBLE
            else{
                binding.layoutNoReview.visibility=GONE
                binding.recyclerviewReviews.adapter=ReviewsRecyclerviewAdapter(reviews)
            }
        })
    }

}