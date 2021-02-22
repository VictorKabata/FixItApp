package com.vickikbt.fixitapp.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.BottomSheetPaymentBinding
import com.vickikbt.fixitapp.ui.MpesaViewModel
import com.vickikbt.fixitapp.utils.*

class PaymentBottomSheet : BottomSheetDialogFragment(),StateListener {

    private lateinit var binding: BottomSheetPaymentBinding
    private val viewModel by viewModels<MpesaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_payment, container, false)
        binding.viewModel=viewModel
        viewModel.stateListener=this

        //initUI()

        return binding.root
    }

    private fun initUI() {
        if (binding.editTextPaymentPhone.text.isNullOrEmpty()) {
            requireActivity().toast("Enter worker phone number") //TODO: Add from string res
        } else if (binding.editTextPaymentAmount.text.isNullOrEmpty()) {
            requireActivity().toast("Enter amount to send") //TODO: Add from string res
        }

    }

    override fun onLoading() {
        binding.progressBarPayment.show()
    }

    override fun onSuccess(message: String) {
        binding.progressBarPayment.hide()
        //requireActivity().toast(message)
        requireActivity().log(message)
    }

    override fun onFailure(message: String) {
        binding.progressBarPayment.hide()
        requireActivity().toast(message)
        requireActivity().log(message)
    }

}