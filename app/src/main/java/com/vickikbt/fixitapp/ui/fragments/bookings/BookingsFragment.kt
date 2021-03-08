package com.vickikbt.fixitapp.ui.fragments.bookings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentPostBookingBinding
import com.vickikbt.fixitapp.ui.adapters.PostBookingRecyclerviewAdapter
import com.vickikbt.fixitapp.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingsFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentPostBookingBinding
    private val viewModel by viewModels<BookingViewModel>()
    private val args: BookingsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_post_booking, container, false)
        viewModel.stateListener = this

        initUI()

        return binding.root
    }

    private fun initUI() {
        val postId = args.PostId
        val budget = args.Budget

        viewModel.getPostBooking(postId).observe(viewLifecycleOwner, { bookings ->
            binding.recyclerViewBookings.adapter =
                PostBookingRecyclerviewAdapter(requireActivity(), bookings, budget, viewModel)
        })
    }

    override fun onLoading() {
        binding.progressBarBookings.show()
    }

    override fun onSuccess(message: String) {
        binding.progressBarBookings.hide()
        if (isAdded) requireActivity().log(message)
    }

    override fun onFailure(message: String) {
        binding.progressBarBookings.hide()
        if (isAdded){
            requireActivity().toast(message)
            requireActivity().log(message)
        }

    }
}