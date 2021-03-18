package com.vickikbt.fixitapp.ui.fragments.work

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentWorkBinding
import com.vickikbt.fixitapp.models.entity.User
import com.vickikbt.fixitapp.models.entity.Work
import com.vickikbt.fixitapp.ui.fragments.auth.UserViewModel
import com.vickikbt.fixitapp.utils.Constants
import com.vickikbt.fixitapp.utils.DataFormatter.Companion.dateFormatter
import com.vickikbt.fixitapp.utils.DataFormatter.Companion.sanitizePhoneNumber
import com.vickikbt.fixitapp.utils.DataFormatter.Companion.workDateFormatter
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

        binding.buttonStartWork.setOnClickListener {
            startWorkDialog()
        }

        getCurrentUser()

        initUI()

        return binding.root
    }

    private fun getCurrentUser() {
        userViewModel.getCurrentUser.observe(viewLifecycleOwner, { user ->
            currentUserX = user
        })
    }

    private fun initUI() {
        initWork()

        if (workX == null) {
            userViewModel.fetchUser(args.UserId).observe(viewLifecycleOwner, { user ->
                Glide.with(requireActivity()).load(user.imageUrl).into(binding.workImageView)
                binding.workUsername.text = user.username
                binding.workEmailAddress.text = user.email
                binding.workPhoneNumber.text = "+${sanitizePhoneNumber(user.phoneNumber)}"
            })
            requireActivity().log("WorkX is null")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initWork() {
        workViewModel.getWork(args.PostId).observe(viewLifecycleOwner, { work ->
            if (work != null) {
                workX = work

                //Check if work is already started to hide the start work button
                if (!workX?.createdAt.isNullOrEmpty()) {
                    binding.buttonStartWork.visibility = GONE
                    binding.buttonComplete.visibility= VISIBLE
                    requireActivity().log("Work created at is no null or empty")
                } //else binding.buttonStartWork.visibility = VISIBLE

                if (currentUserX?.id == args.UserId) {
                    //Check if is current user, then display worker's photo
                    Glide.with(requireActivity()).load(work.worker.imageUrl).into(binding.workImageView)
                    binding.workUsername.text = work.worker.username
                    binding.workEmailAddress.text = work.worker.email
                    binding.workPhoneNumber.text = "+${sanitizePhoneNumber(work.worker.phoneNumber)}"
                } else {
                    //Check if is not current user then display employer's info
                    Glide.with(requireActivity()).load(work.user.imageUrl).into(binding.workImageView)
                    binding.workUsername.text = work.user.username
                    binding.workEmailAddress.text = work.user.email
                    binding.workPhoneNumber.text = "+${sanitizePhoneNumber(work.user.phoneNumber)}"
                }
                binding.workStarted.text = workDateFormatter(work.createdAt)

                if (work.status == Constants.STATUS_COMPLETED) {
                    binding.buttonComplete.visibility = VISIBLE
                    binding.buttonStartWork.visibility = GONE
                    binding.buttonComplete.isEnabled = false
                    binding.buttonComplete.setBackgroundColor(resources.getColor(R.color.button_disabled))
                    binding.buttonComplete.text = resources.getString(R.string.completed)
                    binding.workFinished.text = dateFormatter(work.updatedAt)
                } else {
                    binding.buttonComplete.visibility = GONE
                    //binding.buttonStartWork.visibility = VISIBLE
                }
            }

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
                    val action = WorkFragmentDirections.workToPayment(
                        work.worker.phoneNumber,
                        work.post.budget,
                        work.postId,
                        work.id,
                        work.workerId
                    )
                    findNavController().navigate(action)

                    dialog.dismiss()
                } else
                    requireActivity().toast("Await payment from employer")
                dialog.dismiss()
            })
        }

        dialog.show()
    }

    private fun startWorkDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_options)

        val dialogMessage: TextView = dialog.findViewById(R.id.textView_dialog_message)
        val buttonYes: Button = dialog.findViewById(R.id.button_dialog_yes)
        val buttonNo: TextView = dialog.findViewById(R.id.textView_dialog_no)

        dialogMessage.text = resources.getString(R.string.start_work_message)

        buttonNo.setOnClickListener { dialog.dismiss() }

        buttonYes.setOnClickListener {
            startWork()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun startWork() {
        workViewModel.createWork(args.PostId, args.UserId).observe(viewLifecycleOwner, { work ->
            binding.workStarted.text = workDateFormatter(work.createdAt)
        })
        binding.buttonStartWork.visibility = GONE
        binding.buttonComplete.visibility = VISIBLE
    }


    override fun onLoading() {
        //requireActivity().log(message) TODO: Show progressbar
        binding.shimmerWorkFragment.startShimmer()
    }

    override fun onSuccess(message: String) {
        binding.shimmerWorkFragment.hideShimmer()
        binding.shimmerWorkFragment.stopShimmer()
        binding.shimmerWorkFragment.visibility = GONE
        requireActivity().log(message)
    }

    override fun onFailure(message: String) {
        binding.shimmerWorkFragment.hideShimmer()
        binding.shimmerWorkFragment.stopShimmer()
        binding.shimmerWorkFragment.visibility = GONE

        requireActivity().toast(message)
        requireActivity().log("Network Error: $message")
    }

}