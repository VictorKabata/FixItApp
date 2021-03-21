package com.vickikbt.fixitapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.ItemReviewBinding
import com.vickikbt.fixitapp.models.entity.Review

class ReviewsRecyclerviewAdapter constructor(
    private val reviewList: List<Review>
) : RecyclerView.Adapter<ReviewRecyclerviewViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewRecyclerviewViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemReviewBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_review, parent, false)

        return ReviewRecyclerviewViewHolder(binding)
    }

    override fun getItemCount() = reviewList.size

    override fun onBindViewHolder(holder: ReviewRecyclerviewViewHolder, position: Int) {
        val review = reviewList[position]

        holder.bind(review, holder.itemView.context)
    }
}

class ReviewRecyclerviewViewHolder(private val binding: ItemReviewBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(review: Review, context: Context) {
        Glide.with(context).load(review.user.imageUrl).into(binding.imageViewUser)
        binding.textViewUsername.text = review.user.username
        binding.ratingBarReview.rating = review.rating.toFloat()
        binding.textViewRating.text=review.rating.toString()
        binding.textViewReview.text = review.comment
    }

}