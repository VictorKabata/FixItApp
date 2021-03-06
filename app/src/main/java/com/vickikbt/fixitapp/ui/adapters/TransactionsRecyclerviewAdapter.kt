package com.vickikbt.fixitapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.ItemTransactionBinding
import com.vickikbt.fixitapp.models.network.TransactionResponse
import com.vickikbt.fixitapp.ui.fragments.transactions.TransactionsFragmentDirections
import com.vickikbt.fixitapp.utils.DataFormatter.Companion.dateFormatter

class TransactionsRecyclerviewAdapter constructor(private val transactionList: List<TransactionResponse>) :
    RecyclerView.Adapter<TransactionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemTransactionBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_transaction, parent, false)

        return TransactionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        val transaction = transactionList[position]

        holder.bind(transaction)

        holder.userName.setOnClickListener {
            val action = TransactionsFragmentDirections.transactionToUserProfile(transaction.worker.id)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount() = transactionList.size
}

class TransactionsViewHolder(private val binding: ItemTransactionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val userName = binding.textViewName

    fun bind(transaction: TransactionResponse) {
        binding.textViewName.text = transaction.worker.username
        binding.textViewAmount.text = "Ksh. ${transaction.amount}"
        binding.textViewDate.text = dateFormatter(transaction.createdAt)

    }

}


