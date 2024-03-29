package com.vickikbt.fixitapp.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.ItemHomeBinding
import com.vickikbt.fixitapp.models.entity.Post
import com.vickikbt.fixitapp.ui.fragments.home.HomeFragmentDirections
import com.vickikbt.fixitapp.utils.Constants
import com.vickikbt.fixitapp.utils.DataFormatter.Companion.dateFormatter
import com.vickikbt.fixitapp.utils.log
import com.vickikbt.fixitapp.utils.toast

class HomeRecyclerviewAdapter constructor(
    private val context: Context,
    private val postsList: MutableList<Post>,
    private val currentUserId: Int
) : RecyclerView.Adapter<HomeRecyclerviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerviewViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemHomeBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_home, parent, false)

        return HomeRecyclerviewViewHolder(binding)
    }

    override fun getItemCount() = postsList.size

    override fun onBindViewHolder(holder: HomeRecyclerviewViewHolder, position: Int) {
        val post = postsList[position]

        holder.bind(post, context)

        holder.itemView.animation = AnimationUtils.loadAnimation(
            holder.itemView.context,
            R.anim.recyclerview_load_animation
        )

        holder.profilePic.setOnClickListener {
            if (currentUserId!=post.userId){
                val action = HomeFragmentDirections.homeToUserProfile(post.userId)
                it.findNavController().navigate(action)
            }
        }

        holder.userName.setOnClickListener {
            val action = HomeFragmentDirections.homeToUserProfile(post.userId)
            it.findNavController().navigate(action)
        }

        holder.itemView.setOnClickListener { postDetail(it, post, currentUserId) }
    }

    private fun postDetail(view: View, post: Post, currentUserId: Int) {
        if (post.status.isEmpty() && post.user.id != currentUserId) {
            val action = HomeFragmentDirections.homeToPostDetail(post.id)
            view.findNavController().navigate(action)
            //performTransition(holder, view, post)
        } else if (post.status.isEmpty() && post.user.id == currentUserId) {
            val action = HomeFragmentDirections.homeToPostBookings(post.id, post.budget.toInt())
            view.findNavController().navigate(action)
        } else if (post.status == Constants.STATUS_IN_PROGRESS && post.workerId == currentUserId) {
            val action = HomeFragmentDirections.homeToWork(post.id,post.userId)
            view.findNavController().navigate(action)
        } else if (post.status == Constants.STATUS_IN_PROGRESS && post.userId == currentUserId) {
            context.log("${post.userId}")
            val action = HomeFragmentDirections.homeToWork(post.id,post.workerId)
            view.findNavController().navigate(action)
        } else if (post.status == Constants.STATUS_IN_PROGRESS && post.workerId != currentUserId || post.user.id != currentUserId) {
            context.toast(context.getString(R.string.no_work))
        } //TODO: Add check for if is paid field==true
    }

    private fun performTransition(holder: HomeRecyclerviewViewHolder, view: View, post: Post) {
        val extras = FragmentNavigatorExtras(
            holder.postImage to "post_image",
            holder.category to "post_category",
            holder.description to "post_description",
            holder.profilePic to "post_profile",
            holder.userName to "post_username"
        )

        val action = HomeFragmentDirections.homeToPostDetail(post.id)
        view.findNavController().navigate(action, extras)
    }

}

class HomeRecyclerviewViewHolder(private val binding: ItemHomeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val profilePic = binding.postUserImageView
    val userName = binding.postUserUsername
    val postImage = binding.postImageView
    val category = binding.postCategoryTextView
    val description = binding.postDescriptionTextView

    @SuppressLint("SetTextI18n")
    fun bind(post: Post, context: Context) {
        Glide.with(context).load(post.user.imageUrl)
            .placeholder(R.drawable.imageview_placeholder)
            .error(R.drawable.imageview_placeholder)
            .into(binding.postUserImageView)

        binding.postUserUsername.text = post.user.username
        binding.postDate.text = dateFormatter(post.createdAt)

        Glide.with(context).load(post.imageUrl)
            .placeholder(R.drawable.imageview_placeholder)
            .error(R.drawable.imageview_placeholder)
            .into(binding.postImageView)

        binding.postLocationTextView.text = "${post.region}, ${post.country}"
        binding.postCategoryTextView.text = post.category
        binding.postDescriptionTextView.text = post.description
    }

}