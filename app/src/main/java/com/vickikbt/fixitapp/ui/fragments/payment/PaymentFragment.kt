package com.vickikbt.fixitapp.ui.fragments.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentPaymentBinding
import com.vickikbt.fixitapp.ui.MpesaViewModel
import com.vickikbt.fixitapp.utils.*
import com.vickikbt.fixitapp.utils.DataFormatter.Companion.phoneNumberFormatter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentPaymentBinding
    private val viewModel by viewModels<MpesaViewModel>()
    private val args:PaymentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false)
        viewModel.stateListener=this

        initUI()

        binding.buttonPaymentMpesa.setOnClickListener {
            makePayment()
        }

        return binding.root
    }

    private fun initUI() {
        //binding.editTextPaymentPhone.setText(phoneNumberFormatter(args.phoneNumber))
        binding.editTextPaymentPhone.setText("714091304")
        binding.editTextPaymentAmount.setText(args.budget)

    }

    private fun makePayment(){
        if (binding.editTextPaymentPhone.text.isNullOrEmpty()) {
            requireActivity().toast("Enter worker phone number") //TODO: Add from string res
        } else if (binding.editTextPaymentAmount.text.isNullOrEmpty()) {
            requireActivity().toast("Enter amount to send") //TODO: Add from string res
        }

        val phoneNumber = binding.countryCodePicker.fullNumber + binding.editTextPaymentPhone.text.toString()
        val amount = binding.editTextPaymentAmount.text.toString()

        requireActivity().log(phoneNumber)
        requireActivity().log(amount)

        viewModel.makePayment(phoneNumber, amount)
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