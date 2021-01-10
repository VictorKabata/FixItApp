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
import com.vickikbt.fixitapp.databinding.ItemHomeBinding
import com.vickikbt.fixitapp.models.entity.Post
import com.vickikbt.fixitapp.ui.fragments.HomeFragmentDirections
import com.vickikbt.fixitapp.utils.toast

class HomeRecyclerviewAdapter constructor(
    private val context: Context,
    private val postsList: MutableList<Post>,
    private val currentUserId: Int
) : RecyclerView.Adapter<HomeRecyclerviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerviewViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemHomeBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_home, parent, false)

        return HomeRecyclerviewViewHolder(binding)
    }

    override fun getItemCount() = postsList.size

    override fun onBindViewHolder(holder: HomeRecyclerviewViewHolder, position: Int) {
        val post = postsList[position]

        holder.bind(post, context)

        holder.itemView.setOnClickListener { postDetail(it, post, currentUserId) }
    }

    private fun postDetail(view: View, post: Post, currentUserId: Int) {
        if (post.status.isEmpty() && post.user.id != currentUserId) {
            val action = HomeFragmentDirections.homeToPostDetail(post.id)
            view.findNavController().navigate(action)
        } else if (post.status.isEmpty() && post.user.id == currentUserId) {
            val action = HomeFragmentDirections.homeToPostBookings(post.id,post.budget.toInt())
            view.findNavController().navigate(action)
        } else if (post.status == "In-Progress" && post.workerId == currentUserId) {
            val action = HomeFragmentDirections.homeToWork(post.id)
            //val action = HomeFragmentDirections.homeToPostBookings(post.id,post.budget.toInt())
            view.findNavController().navigate(action)
        } else if (post.status == "In-Progress" && post.user.id == currentUserId) {
            val action = HomeFragmentDirections.homeToWork(post.id)
            //val action = HomeFragmentDirections.homeToPostBookings(post.id,post.budget.toInt())
            view.findNavController().navigate(action)
        } else if (post.status == "In-Progress" && post.workerId != currentUserId || post.user.id != currentUserId) {
            //context.toast("No more application for this work")
            context.toast("Work applications closed")
        }
    }

}

class HomeRecyclerviewViewHolder(private val binding: ItemHomeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(post: Post, context: Context) {
        Glide.with(context).load(post.user.imageUrl).into(binding.postUserImageView)
        binding.postUserUsername.text = post.user.username
        binding.postDate.text = post.createdAt //TODO: Add date formatter later
        Glide.with(context).load(post.imageUrl).into(binding.postImageView)
        binding.postLocationTextView.text =
            "${post.region}, ${post.country}"//"${post.address}, ${post.country}."
        binding.postCategoryTextView.text = post.category
        binding.postDescriptionTextView.text = post.description
    }

}