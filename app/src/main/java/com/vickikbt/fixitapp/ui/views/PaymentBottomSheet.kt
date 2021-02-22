package com.vickikbt.fixitapp.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.BottomSheetPaymentBinding
import com.vickikbt.fixitapp.utils.toast

class PaymentBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_payment, container, false)

        initUI()

        return binding.root
    }

    private fun initUI() {
        if (binding.editTextPaymentPhone.text.isNullOrEmpty()) {
            requireActivity().toast("Enter worker phone number") //TODO: Add from string res
        } else if (binding.editTextPaymentAmount.text.isNullOrEmpty()) {
            requireActivity().toast("Enter amount to send") //TODO: Add from string res
        }

    }

}