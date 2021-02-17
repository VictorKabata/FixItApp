package com.vickikbt.fixitapp.ui.fragments.work

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentWorkBinding
import com.vickikbt.fixitapp.models.entity.User
import com.vickikbt.fixitapp.models.entity.Work
import com.vickikbt.fixitapp.ui.fragments.auth.UserViewModel
import com.vickikbt.fixitapp.ui.views.PaymentBottomSheet
import com.vickikbt.fixitapp.utils.Constants.COMPLETED
import com.vickikbt.fixitapp.utils.DataFormatter
import com.vickikbt.fixitapp.utils.StateListener
import com.vickikbt.fixitapp.utils.log
import com.vickikbt.fixitapp.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentWorkBinding
    private val workViewModel by viewModels<WorkViewModel>()
    private val userViewModel by viewModels<UserViewModel>()
    private val args: WorkFragmentArgs by navArgs()

    private var workX: Work? = null
    private var currentUserX: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_work, container, false)
        workViewModel.stateListener = this

        binding.buttonComplete.setOnClickListener {
            completeWork()
        }

        initUI()

        return binding.root
    }

    private fun initUI() {
        val postId = args.PostId

        userViewModel.getCurrentUser.observe(viewLifecycleOwner, { currentUser ->
            currentUserX = currentUser

            workViewModel.getWork(postId).observe(viewLifecycleOwner, { work ->
                workX = work

                if (currentUser.id == work.user.id) {
                    //Show the worker detail
                    binding.workUsername.text = work.worker.username
                    binding.workEmailAddress.text = work.worker.email
                    binding.workPhoneNumber.text = work.worker.phoneNumber
                    Glide.with(requireActivity()).load(work.worker.imageUrl)
                        .into(binding.workImageView)
                } else {
                    //Show the user/employer detail
                    binding.workUsername.text = work.user.username
                    binding.workEmailAddress.text = work.user.email
                    binding.workPhoneNumber.text = work.user.phoneNumber
                    Glide.with(requireActivity()).load(work.user.imageUrl)
                        .into(binding.workImageView)
                }

                binding.workStarted.text = DataFormatter.dateFormatter(work.createdAt)

                if (work.status == COMPLETED) {
                    binding.workFinished.text = DataFormatter.dateFormatter(work.updatedAt)
                    binding.buttonComplete.text =
                        requireActivity().resources.getString(R.string.completed)
                    binding.buttonComplete.setBackgroundColor(resources.getColor(R.color.button_disabled))
                    binding.buttonComplete.isEnabled = false
                }
            })
        })
    }

    private fun completeWork() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_options)

        val dialogMessage: TextView = dialog.findViewById(R.id.textView_dialog_message)
        val buttonYes: Button = dialog.findViewById(R.id.button_dialog_yes)
        val buttonNo: TextView = dialog.findViewById(R.id.textView_dialog_no)

        dialogMessage.text = resources.getString(R.string.complete_work_message)

        buttonNo.setOnClickListener { dialog.dismiss() }

        buttonYes.setOnClickListener {
            workViewModel.updateWork(workX!!).observe(viewLifecycleOwner, { work ->
                workViewModel.updateWork(work)
                //binding.workFinished.text = DataFormatter.updateDateFormatter(work.updatedAt) TODO: Fix this
                //requireActivity().log("Updated At: ${work.updatedAt}")
                //requireActivity().log("Formatted Updated At: ${DataFormatter.dateFormatter(work.updatedAt)}")
                //rateWorker(work)

                if (currentUserX!!.id == work.userId) {
                    //val action=WorkFragmentDirections.workToPayment(work.worker.phoneNumber)
                    //findNavController().navigate(action)
                    val paymentBottomSheet = PaymentBottomSheet()
                    //paymentBottomSheet.
                    paymentBottomSheet.show(childFragmentManager, "Payment BottomSheet")
                    dialog.dismiss()
                } else

                    dialog.dismiss()
            })
        }

        dialog.show()
    }

    /*@SuppressLint("SetTextI18n")
    private fun rateWorker(work: Work) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_rating)

        val dialogMessage: TextView = dialog.findViewById(R.id.textView_review_title)
        val ratingBar:RatingBar=dialog.findViewById(R.id.rating_bar_review)
        val editTextReview:EditText=dialog.findViewById(R.id.editText_review)
        val buttonYes: Button = dialog.findViewById(R.id.button_dialog_yes)
        val buttonNo: TextView = dialog.findViewById(R.id.textView_dialog_no)

        dialogMessage.text = "${resources.getString(R.string.rating_dialog_title)} ${work.worker.username}?"

        buttonNo.setOnClickListener { dialog.dismiss() }

        buttonYes.setOnClickListener {
            val rating=ratingBar.rating.toInt()
            val comment=editTextReview.text.toString()
            workViewModel.reviewUser(work,rating, comment)
            dialog.dismiss()
        }

        dialog.show()
    }*/

    override fun onLoading() {
        //requireActivity().log(message) TODO: Show progressbar
    }

    override fun onSuccess(message: String) {
        requireActivity().log(message)
    }

    override fun onFailure(message: String) {
        requireActivity().toast(message)
        requireActivity().log("Network Error: $message")
    }

}