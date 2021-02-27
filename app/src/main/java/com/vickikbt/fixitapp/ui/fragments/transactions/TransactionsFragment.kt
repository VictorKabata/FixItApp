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
import com.vickikbt.fixitapp.ui.adapters.TransactionsRecyclerviewAdapter
import com.vickikbt.fixitapp.utils.*
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
            if (transactions.isNullOrEmpty()) binding.textViewTransactions.visibility = VISIBLE

            binding.recyclerviewTransactions.adapter = TransactionsRecyclerviewAdapter(transactions)
        })

    }

    override fun onLoading() {
        binding.progressBarTransaction.show()
    }

    override fun onSuccess(message: String) {
        binding.progressBarTransaction.hide()
        requireActivity().log(message)
    }

    override fun onFailure(message: String) {
        binding.progressBarTransaction.hide()
        requireActivity().log(message)
    }
}