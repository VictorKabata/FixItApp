package com.vickikbt.fixitapp.ui.fragments.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentPaymentBinding
import com.vickikbt.fixitapp.utils.DataFormatter.Companion.phoneNumberFormatter
import com.vickikbt.fixitapp.utils.log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private lateinit var binding:FragmentPaymentBinding
    private val args:PaymentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_payment, container, false)

        initUI()

        return binding.root
    }

    private fun initUI(){
        val phone=args.PhoneNumber
        val formattedPhoneNumber=phoneNumberFormatter(phone)
        val phoneNumber=binding.countryCodePicker.selectedCountryCodeWithPlus+formattedPhoneNumber
        requireActivity().log(phoneNumber)

        binding.editTextPaymentPhone.apply {
            this.setText(formattedPhoneNumber)
            /*val editTextValue=this.toString()
            for (i in 0..3){
                this.setLineSpacing(0.2f,0f)
            }*/ //TODO: Add line spacing after every 3 characters
        }

    }


}