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
import com.vickikbt.fixitapp.ui.fragments.auth.UserViewModel
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

    private var workId: Int? = null
    //private var currentUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            workViewModel.getWork(postId).observe(viewLifecycleOwner, { work ->
                workId = work.id

                if (currentUser.id == work.user.id) {
                    //Show the worker detail
                    binding.workUsername.text = work.worker.username
                    binding.workEmailAddress.text = work.worker.email
                    binding.workPhoneNumber.text = work.worker.phoneNumber
                    Glide.with(requireActivity()).load(work.worker.imageUrl).into(binding.workImageView)
                } else {
                    //Show the user/employer detail
                    binding.workUsername.text = work.user.username
                    binding.workEmailAddress.text = work.user.email
                    binding.workPhoneNumber.text = work.user.phoneNumber
                    Glide.with(requireActivity()).load(work.user.imageUrl).into(binding.workImageView)
                }

                binding.workStarted.text = DataFormatter.dateFormatter(work.createdAt)

                if (work.status == COMPLETED) {
                    binding.workFinished.text = DataFormatter.dateFormatter(work.updatedAt)
                    binding.buttonComplete.text=requireActivity().resources.getString(R.string.completed)
                    binding.buttonComplete.setBackgroundColor(resources.getColor(R.color.button_disabled))
                    binding.buttonComplete.isEnabled=false
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

        dialogMessage.text=requireActivity().resources.getString(R.string.logout_message)

        buttonNo.setOnClickListener { dialog.dismiss() }

        workViewModel.updateWork(workId!!).observe(viewLifecycleOwner, { work ->

            buttonYes.setOnClickListener {
                workViewModel.updateWork(work.id)
                dialog.dismiss()
            }
        })
    }

    override fun onLoading() {
        //requireActivity().log(message) TODO: Show progressbar
    }

    override fun onSuccess(message: String) {
        requireActivity().log(message)
    }

    override fun onFailure(message: String) {
        requireActivity().toast(message)
        requireActivity().log(message)
    }

}