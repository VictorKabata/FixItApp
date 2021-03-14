package com.vickikbt.fixitapp.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.ItemPostBookingBinding
import com.vickikbt.fixitapp.models.entity.Booking
import com.vickikbt.fixitapp.ui.fragments.bookings.BookingViewModel
import com.vickikbt.fixitapp.ui.fragments.bookings.BookingsFragmentDirections
import com.vickikbt.fixitapp.utils.Constants.REJECT
import com.vickikbt.fixitapp.utils.StateListener
import com.vickikbt.fixitapp.utils.log
import com.vickikbt.fixitapp.utils.toast

class PostBookingRecyclerviewAdapter constructor(
    private val context: Context,
    private val bookingList: List<Booking>,
    private val budget: Int,
    private val bookingViewModel: BookingViewModel
) : RecyclerView.Adapter<PostBookingsRecyclerviewViewHolder>(), StateListener {

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
            val action = BookingsFragmentDirections.postBookingsToUserProfile(booking.userId)
            it.findNavController().navigate(action)
        }

        holder.acceptButton.setOnClickListener {
            acceptBooking(booking.id, booking.postId, booking.userId, it)
        }

        holder.rejectButton.setOnClickListener {
            rejectBooking(booking.id, booking.userId)

            holder.linearLayoutContainer.visibility = View.GONE
            holder.rejectConfirmedButton.visibility = View.VISIBLE
        }

    }

    private fun acceptBooking(bookingId: Int, postId: Int, userId: Int, view: View) {
        try {
            bookingViewModel.acceptBooking(bookingId, postId, userId)
            val action = BookingsFragmentDirections.postBookingsToWork(postId,userId)
            view.findNavController().navigate(action)
        } catch (e: Exception) {
            context.toast("${e.message}")
        }
    }

    private fun rejectBooking(bookingId: Int, userId: Int) =
        bookingViewModel.rejectBooking(bookingId, userId)

    override fun onLoading() {}

    override fun onSuccess(message: String) {
        context.toast(message)
    }

    override fun onFailure(message: String) {
        context.toast(message)
    }


}

class PostBookingsRecyclerviewViewHolder(private val binding: ItemPostBookingBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val profilePic: ImageView = binding.imageViewPostBookingProfile
    val acceptButton: Button = binding.buttonAccept
    val rejectButton: Button = binding.buttonReject

    val linearLayoutContainer: LinearLayout = binding.linearLayoutContainer
    val rejectConfirmedButton: Button = binding.buttonRejectionConfirmed

    @SuppressLint("SetTextI18n")
    fun bind(booking: Booking, context: Context, budget: Int) {
        val bid = booking.bid.toInt()

        Glide.with(context).load(booking.user.imageUrl).into(binding.imageViewPostBookingProfile)
        binding.textViewPostBookingUsername.text = booking.user.username
        binding.textViewPostBookingEmail.text = booking.user.email
        //binding.postBookingUserPhone.text = booking.user.phoneNumber
        binding.textViewPostBookingLocation.text = "${booking.user.region}, ${booking.user.address}"
        //binding.postBookingDate.text = DataFormatter.dateFormatter(booking.createdAt)
        binding.textViewPostBookingComment.text = booking.comment
        context.log("${booking.status}")


        when {
            bid < budget -> binding.textViewPostBookingsBid.setTextColor(
                context.resources.getColor(
                    R.color.green
                )
            )
            bid > budget -> binding.textViewPostBookingsBid.setTextColor(
                context.resources.getColor(
                    R.color.red
                )
            )
            else -> binding.textViewPostBookingsBid.setTextColor(context.resources.getColor(R.color.yellow))
        }

        binding.textViewPostBookingsBid.text = "Ksh. $bid"

        if (booking.status == REJECT) {
            binding.linearLayoutContainer.visibility = View.GONE
            binding.buttonRejectionConfirmed.visibility = View.VISIBLE
        }
    }

}