package com.vickikbt.fixitapp.ui.fragments.post_detail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentPostDetailBinding
import com.vickikbt.fixitapp.ui.fragments.auth.UserViewModel
import com.vickikbt.fixitapp.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentPostDetailBinding
    private val postViewModel by viewModels<PostDetailViewModel>()
    private val userViewModel by viewModels<UserViewModel>()
    private val args: PostDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_detail, container, false)
        postViewModel.stateListener = this

        binding.buttonContact.setOnClickListener {
            //val action = PostDetailFragmentDirections.actionPostDetailFragmentToChatFragment(user.id)
            //findNavController().navigate(action)
        }

        binding.postDetailToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.postDetailToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonBook.setOnClickListener {
            bookWork() //TODO: apply work api
        }

        initUI()

        return binding.root
    }

    private fun initUI() {
        val postId = args.PostId

        postViewModel.getPost(postId).observe(viewLifecycleOwner, { post ->
            val user = post.user

            Glide.with(requireActivity()).load(post.imageUrl).into(binding.imageViewPostDetail)
            Glide.with(requireActivity()).load(user.imageUrl).into(binding.userImageView)

            binding.userUsername.text = user.username
            binding.userEmail.text = user.email
            binding.textViewPostCategory.text = post.category
            binding.textViewPostDescription.text = post.description
            binding.textViewPostBudget.text = post.budget

            val currentUserId = userViewModel.getCurrentUser.value!!.id
            if (currentUserId != user.id) {
                binding.userImageView.setOnClickListener {
                    val action = PostDetailFragmentDirections.postDetailToUserProfile(user.id)
                    findNavController().navigate(action)
                }
            } else {
                binding.buttonBook.visibility = View.GONE
                binding.buttonContact.visibility = View.GONE
            }

        })

    }

    private fun bookWork() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_book_work)

        val user = userViewModel.getCurrentUser.value!!

        val profileImage: ImageView = dialog.findViewById(R.id.imageView_book_work)
        val userName: TextView = dialog.findViewById(R.id.textView_userName_book_work)
        val phoneNumber: TextView = dialog.findViewById(R.id.textView_phoneNumber_book_work)
        val budgetEt: EditText = dialog.findViewById(R.id.editText_budget_book_work)
        val commentEt: EditText = dialog.findViewById(R.id.editText_comment_book_work)
        val cancel: TextView = dialog.findViewById(R.id.textView_dialog_cancel_book_work)
        val bookWork: Button = dialog.findViewById(R.id.button_dialog_book_work)

        Glide.with(requireActivity()).load(user.imageUrl).into(profileImage)
        userName.text = user.username
        phoneNumber.text = user.phoneNumber
        budgetEt.setText("0")

        bookWork.setOnClickListener {
            val budget = budgetEt.text.toString().toInt()
            val comment = commentEt.text.toString()

            //workViewModel.bookWork(postId, budget, comment)
        }

        cancel.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    override fun onLoading() {
        binding.progressBarPostDetail.show()
    }

    override fun onSuccess(message: String) {
        binding.progressBarPostDetail.hide()
        requireActivity().log(message)
    }

    override fun onFailure(message: String) {
        binding.progressBarPostDetail.hide()
        requireActivity().toast(message)
        requireActivity().log(message)
    }
}