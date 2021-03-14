package com.vickikbt.fixitapp.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.ItemFeedPostBinding
import com.vickikbt.fixitapp.models.entity.Post
import com.vickikbt.fixitapp.ui.fragments.feeds.FeedsFragmentDirections
import com.vickikbt.fixitapp.utils.DataFormatter.Companion.dateFormatter

class FeedPostRecyclerviewAdapter constructor(
    private val context: Context,
    private val postsList: List<Post>,
) : RecyclerView.Adapter<FeedPostRecyclerviewViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeedPostRecyclerviewViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemFeedPostBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_feed_post, parent, false)

        return FeedPostRecyclerviewViewHolder(binding)
    }

    override fun getItemCount() = postsList.size

    override fun onBindViewHolder(holder: FeedPostRecyclerviewViewHolder, position: Int) {
        val post = postsList[position]

        holder.bind(post, context)

        holder.itemView.setOnClickListener { postDetail(it, post) }
    }

    private fun postDetail(view: View, post: Post) {
        if (post.status.isEmpty()) {
            val action = FeedsFragmentDirections.feedsToBooking(post.id, post.budget.toInt())
            view.findNavController().navigate(action)
        } else if (post.status == "In-Progress") {
            //val action = FeedsPostFragmentDirections.feedPostsToWork(post.id)
            //view.findNavController().navigate(action)
        }
    }

}

class FeedPostRecyclerviewViewHolder(private val binding: ItemFeedPostBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(post: Post, context: Context) {
        Glide.with(context).load(post.imageUrl)
            .placeholder(R.drawable.imageview_placeholder)
            .error(R.drawable.imageview_placeholder)
            .into(binding.postImageView)
        binding.postDate.text = dateFormatter(post.createdAt)
        binding.postLocationTextView.text = "${post.region}, ${post.country}"
        binding.postCategoryTextView.text = post.category
        binding.postDescriptionTextView.text = post.description
    }

}