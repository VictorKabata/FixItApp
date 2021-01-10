package com.vickikbt.fixitapp.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.ItemPostBookingBinding
import com.vickikbt.fixitapp.models.entity.Booking
import com.vickikbt.fixitapp.ui.fragments.PostBookingsFragmentDirections

class PostBookingRecyclerviewAdapter constructor(
    private val context: Context,
    private val bookingList: MutableList<Booking>,
    private val budget: Int
) : RecyclerView.Adapter<PostBookingsRecyclerviewViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostBookingsRecyclerviewViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemPostBookingBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_post_booking, parent, false)

        return PostBookingsRecyclerviewViewHolder(binding)
    }

    override fun getItemCount() = bookingList.size

    override fun onBindViewHolder(holder: PostBookingsRecyclerviewViewHolder, position: Int) {
        val booking = bookingList[position]

        holder.bind(booking, context, budget)

        holder.profilePic.setOnClickListener {
            val action=PostBookingsFragmentDirections.postBookingToUserProfile(booking.userId)
            it.findNavController().navigate(action)
        }

    }


}

class PostBookingsRecyclerviewViewHolder(private val binding: ItemPostBookingBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val profilePic:ImageView =binding.imageViewPostBookingProfile

    @SuppressLint("SetTextI18n")
    fun bind(booking: Booking, context: Context, budget: Int) {
        val bid = 499

        Glide.with(context).load(booking.user.imageUrl).into(binding.imageViewPostBookingProfile)
        binding.textViewPostBookingUsername.text = booking.user.username
        binding.textViewPostBookingEmail.text = booking.user.email
        //binding.postBookingUserPhone.text = booking.user.phoneNumber
        binding.textViewPostBookingLocation.text = "${booking.user.region}, ${booking.user.address}"
        //binding.postBookingDate.text = DataFormatter.dateFormatter(booking.createdAt)
        binding.textViewPostBookingComment.text = "This is just a placeholder text"//booking.comment


        if (bid < budget) {
            binding.textViewPostBookingsBid.setTextColor(context.resources.getColor(R.color.red))
        } else {
            binding.textViewPostBookingsBid.setTextColor(context.resources.getColor(R.color.green))
        }
        binding.textViewPostBookingsBid.text = "Ksh. $bid"//booking.bid
    }

}