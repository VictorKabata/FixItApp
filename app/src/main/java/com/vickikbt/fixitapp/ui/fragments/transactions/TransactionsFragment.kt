package com.vickikbt.fixitapp.ui.fragments.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentTransactionsBinding
import com.vickikbt.fixitapp.utils.StateListener
import com.vickikbt.fixitapp.utils.log
import com.vickikbt.fixitapp.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionsFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentTransactionsBinding
    private val viewModel by viewModels<TransactionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_transactions, container, false)
        viewModel.stateListener = this

        initUI()

        return binding.root
    }

    private fun initUI() {
        viewModel.getUserTransactions().observe(viewLifecycleOwner, { transactions ->
            if (transactions.isNullOrEmpty()){
                binding.textViewTransactions.text="No Transactions"
                //binding.textViewTransactions.visibility=VISIBLE
            }

            binding.textViewTransactions.text = transactions.toString()
        })

    }

    override fun onLoading() {
        requireActivity().toast("Loading")
    }

    override fun onSuccess(message: String) {
        requireActivity().toast("Loading")
        requireActivity().log(message)
    }

    override fun onFailure(message: String) {
        requireActivity().toast(message)
        requireActivity().log(message)
    }
}