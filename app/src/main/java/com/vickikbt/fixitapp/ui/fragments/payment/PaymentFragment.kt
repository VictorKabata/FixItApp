package com.vickikbt.fixitapp.ui.fragments.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.twigafoods.daraja.Daraja
import com.twigafoods.daraja.DarajaListener
import com.twigafoods.daraja.model.AccessToken
import com.twigafoods.daraja.model.LNMExpress
import com.twigafoods.daraja.model.LNMResult
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.data.cache.AppDatabase
import com.vickikbt.fixitapp.databinding.FragmentPaymentBinding
import com.vickikbt.fixitapp.utils.*
import com.vickikbt.fixitapp.utils.Constants.CONSUMER_KEY
import com.vickikbt.fixitapp.utils.Constants.CONSUMER_SECRET
import com.vickikbt.fixitapp.utils.DataFormatter.Companion.getPassword
import com.vickikbt.fixitapp.utils.DataFormatter.Companion.getTimeStamp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PaymentFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentPaymentBinding

    private val args: PaymentFragmentArgs by navArgs()
    private lateinit var daraja: Daraja

    //@Inject
    //lateinit var appDatabase: AppDatabase

    init {
        getAccessToken()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false)

        initUI()

        binding.buttonPaymentMpesa.setOnClickListener {
            makePayment()
        }

        return binding.root
    }

    private fun getAccessToken() {
        daraja = Daraja.with(CONSUMER_KEY, CONSUMER_SECRET, object : DarajaListener<AccessToken> {
            override fun onResult(accessToken: AccessToken) {
                requireActivity().log("Access token: ${accessToken.access_token}")
            }

            override fun onError(error: String) {
                requireActivity().toast(error)
                requireActivity().log("Error getting access token: $error")
            }
        })
    }

    private fun makePayment() {
        val phoneEt = binding.editTextPaymentPhone.text.toString()
        val countryCode = binding.countryCodePicker.selectedCountryCode
        val phoneNumber = countryCode + phoneEt
        val amount = binding.editTextPaymentAmount.text.toString()

        validate()

        val lnmExpress = LNMExpress(
            Constants.BUSINESS_SHORT_CODE,
            Constants.PASSKEY,
            amount,
            "0714091304",
            Constants.PARTYB,
            phoneNumber,
            Constants.CALLBACKURL,
            Constants.ACCOUNT_REFERENCE,
            Constants.ACCOUNT_REFERENCE
        )

        daraja.requestMPESAExpress(lnmExpress,
            object : DarajaListener<LNMResult> {
                override fun onResult(lnmResult: LNMResult) {
                    //requireActivity().toast("STKPush Successful")
                }

                override fun onError(error: String) {
                    requireActivity().log("STKPush error: $error")
                    //requireActivity().toast("STKPush Failed")
                }
            }
        )
    }

    private fun validate(){
        when {
            binding.editTextPaymentPhone.text.isNullOrEmpty() -> {
                requireActivity().toast("Enter phone number.")
            }
            binding.editTextPaymentAmount.text.isNullOrEmpty() -> {
                requireActivity().toast("Enter amount.")
            }
            binding.editTextPaymentAmount.text.toString().toInt()<1 -> {
                requireActivity().toast("Cannot send money less than 1 Ksh.")
            }
        }
    }

    private fun initUI() {
        //binding.editTextPaymentPhone.setText(phoneNumberFormatter(args.phoneNumber))
        binding.editTextPaymentPhone.setText("722387452")
        binding.editTextPaymentAmount.setText("0")
        //binding.editTextPaymentAmount.setText(args.budget) TODO: Update

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