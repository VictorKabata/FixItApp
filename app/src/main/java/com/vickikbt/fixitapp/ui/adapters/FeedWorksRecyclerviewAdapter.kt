package com.vickikbt.fixitapp.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.ItemHomeBinding
import com.vickikbt.fixitapp.models.entity.Work

class FeedWorksRecyclerviewAdapter constructor(
    private val context: Context,
    private val worksList: List<Work>
) : RecyclerView.Adapter<FeedWorkRecyclerviewViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeedWorkRecyclerviewViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemHomeBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_home, parent, false)

        return FeedWorkRecyclerviewViewHolder(binding)
    }

    override fun getItemCount() = worksList.size

    override fun onBindViewHolder(holder: FeedWorkRecyclerviewViewHolder, position: Int) {
        val post = worksList[position]

        holder.bind(post, context)

    }

}

class FeedWorkRecyclerviewViewHolder(private val binding: ItemHomeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(work: Work, context: Context) {
        Glide.with(context).load(work.user.imageUrl).into(binding.postUserImageView)
        binding.postUserUsername.text = work.user.username
        binding.postDate.text = work.createdAt //TODO: Add date formatter later
        Glide.with(context).load(work.post.imageUrl).into(binding.postImageView)
        binding.postLocationTextView.text = "${work.post.region}, ${work.post.country}"
        binding.postCategoryTextView.text = work.post.category
        binding.postDescriptionTextView.text = work.post.description
    }

}