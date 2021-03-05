package com.vickikbt.fixitapp.ui.fragments.payment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.twigafoods.daraja.Daraja
import com.twigafoods.daraja.DarajaListener
import com.twigafoods.daraja.model.AccessToken
import com.twigafoods.daraja.model.LNMExpress
import com.twigafoods.daraja.model.LNMResult
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentPaymentBinding
import com.vickikbt.fixitapp.models.entity.Work
import com.vickikbt.fixitapp.ui.fragments.auth.UserViewModel
import com.vickikbt.fixitapp.ui.fragments.transactions.TransactionViewModel
import com.vickikbt.fixitapp.ui.fragments.work.WorkViewModel
import com.vickikbt.fixitapp.utils.*
import com.vickikbt.fixitapp.utils.Constants.CONSUMER_KEY
import com.vickikbt.fixitapp.utils.Constants.CONSUMER_SECRET
import com.vickikbt.fixitapp.utils.Constants.TYPE_CASH
import com.vickikbt.fixitapp.utils.DataFormatter.Companion.phoneNumberFormatter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PaymentFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentPaymentBinding
    private val viewModel by viewModels<TransactionViewModel>()
    private val userViewModel by viewModels<UserViewModel>()
    private val workViewModel by viewModels<WorkViewModel>()

    private val args: PaymentFragmentArgs by navArgs()
    private lateinit var daraja: Daraja
    private lateinit var userPhoneNumber: String
    private lateinit var work: Work

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

        binding.buttonPaymentCash.setOnClickListener {
            createTransaction(TYPE_CASH)
        }

        getUserPhoneNumber()

        return binding.root
    }

    private fun getUserPhoneNumber() {
        userViewModel.getCurrentUser.observe(viewLifecycleOwner, { user ->
            userPhoneNumber = user.phoneNumber
        })
    }

    private fun initUI() {
        binding.editTextPaymentPhone.setText(phoneNumberFormatter(args.phoneNumber))
        binding.editTextPaymentAmount.setText(args.budget)

        workViewModel.getWork(args.postId).observe(viewLifecycleOwner, {
            work = it
        })
    }

    private fun validate() {
        when {
            binding.editTextPaymentPhone.text.isNullOrEmpty() -> {
                requireActivity().toast("Enter phone number.")
            }
            binding.editTextPaymentAmount.text.isNullOrEmpty() -> {
                requireActivity().toast("Enter amount.")
            }
            binding.editTextPaymentAmount.text.toString().toInt() < 1 -> {
                requireActivity().toast("Cannot send money less than 1 Ksh.")
            }
        }
    }

    private fun getAccessToken() {
        daraja = Daraja.with(CONSUMER_KEY, CONSUMER_SECRET, object : DarajaListener<AccessToken> {
            override fun onResult(accessToken: AccessToken) {
                if (isAdded) requireActivity().log("Access token: ${accessToken.access_token}")
            }

            override fun onError(error: String) {
                if (isAdded) {
                    requireActivity().toast(error)
                    requireActivity().log("Error getting access token: $error")
                }
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
            userPhoneNumber,
            Constants.PARTYB,
            phoneNumber,
            Constants.CALLBACKURL,
            Constants.ACCOUNT_REFERENCE,
            Constants.ACCOUNT_REFERENCE
        )

        daraja.requestMPESAExpress(lnmExpress,
            object : DarajaListener<LNMResult> {
                override fun onResult(lnmResult: LNMResult) {
                    requireActivity().log("STKPush Successful")
                }

                override fun onError(error: String) {
                    requireActivity().log("STKPush error: $error")
                    requireActivity().toast("Failed")
                }
            }
        )
    }

    private fun createTransaction(type: String) {
        val amount = binding.editTextPaymentAmount.text.toString()
        val postId = args.postId
        val workId = args.workId
        val workerId = args.workerId

        viewModel.createTransaction(postId, amount, type, workId, workerId)

        Handler().postDelayed({ rateWorker() }, 1000)
    }

    @SuppressLint("SetTextI18n")
    private fun rateWorker() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_rating)

        val dialogMessage: TextView = dialog.findViewById(R.id.textView_review_title)
        val ratingBar: RatingBar = dialog.findViewById(R.id.rating_bar_review)
        val editTextReview: EditText = dialog.findViewById(R.id.editText_review)
        val buttonYes: Button = dialog.findViewById(R.id.button_dialog_yes)
        val buttonNo: TextView = dialog.findViewById(R.id.textView_dialog_no)

        dialogMessage.text =
            "${resources.getString(R.string.rating_dialog_title)} ${work.worker.username}?"

        buttonNo.setOnClickListener { dialog.dismiss() }

        buttonYes.setOnClickListener {
            val rating = ratingBar.rating.toInt()
            val comment = editTextReview.text.toString()
            workViewModel.reviewUser(work, rating, comment)

            //Handler().postDelayed({findNavController().navigate(R.id.transactions_to_home)},1000)

            dialog.dismiss()
        }

        dialog.show()
    }


    override fun onLoading() {
        binding.progressBarPayment.show()
    }

    override fun onSuccess(message: String) {
        binding.progressBarPayment.hide()
        //requireActivity().toast(message)
        requireActivity().log(message)

        if (message == "Rated") findNavController().navigate(R.id.transactions_to_home)
    }

    override fun onFailure(message: String) {
        binding.progressBarPayment.hide()
        requireActivity().toast(message)
        requireActivity().log(message)
    }

}