package com.vickikbt.fixitapp.ui.fragments.post_detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentPostDetailBinding
import com.vickikbt.fixitapp.ui.fragments.auth.UserViewModel
import com.vickikbt.fixitapp.utils.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PostDetailFragment : Fragment(), StateListener, OnMapReadyCallback {

    private lateinit var binding: FragmentPostDetailBinding
    private val postViewModel by viewModels<PostDetailViewModel>()
    private val userViewModel by viewModels<UserViewModel>()
    private val args: PostDetailFragmentArgs by navArgs()

    private lateinit var currentUserName: String
    private lateinit var postUserName: String
    private lateinit var postUserPhoneNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_detail, container, false)
        postViewModel.stateListener = this

        binding.postDetailToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.postDetailToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        binding.buttonBook.setOnClickListener {
            bookWork() //TODO: apply work api
        }

        binding.buttonContact.setOnClickListener {
            sendSms()
        }

        //initAnimations()

        initUI()

        return binding.root
    }

    private fun initUI() {
        val postId = args.PostId

        postViewModel.getPost(postId).observe(viewLifecycleOwner, { post ->
            val user = post.user

            postUserName = user.username
            postUserPhoneNumber = user.phoneNumber

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

            binding.userImageView.setOnClickListener {
                val action = PostDetailFragmentDirections.postDetailToUserProfile(user.id)
                findNavController().navigate(action)
            }


            //setUpMap(post)

        })

        userViewModel.getCurrentUser.observe(viewLifecycleOwner, { user ->
            currentUserName = user.username
        })

    }


    private fun bookWork() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_book_work)


        val profileImage: ImageView = dialog.findViewById(R.id.imageView_book_work)
        val userName: TextView = dialog.findViewById(R.id.textView_userName_book_work)
        val phoneNumber: TextView = dialog.findViewById(R.id.textView_phoneNumber_book_work)
        val budgetEt: EditText = dialog.findViewById(R.id.editText_budget_book_work)
        val commentEt: EditText = dialog.findViewById(R.id.editText_comment_book_work)
        val cancel: TextView = dialog.findViewById(R.id.textView_dialog_cancel_book_work)

        val bookWork: Button = dialog.findViewById(R.id.button_dialog_book_work)

        userViewModel.getCurrentUser.observe(viewLifecycleOwner, { user ->
            Glide.with(requireActivity()).load(user.imageUrl).into(profileImage)
            userName.text = user.username
            phoneNumber.text = user.phoneNumber
        })
        budgetEt.setText("0")

        bookWork.setOnClickListener {
            val budget = budgetEt.text.toString()
            val comment = commentEt.text.toString()

            when {
                budget.isEmpty() -> requireActivity().toast("Enter bid amount")
                comment.isEmpty() -> requireActivity().toast("Enter comment")
                else -> {
                    postViewModel.bookWork(args.PostId, budget, comment)
                    dialog.dismiss()
                }
            }
        }

        cancel.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun sendSms() {
        val uri = Uri.parse("smsto:$postUserPhoneNumber")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra("sms_body", "Hello $postUserName, i'm $currentUserName. ")
        startActivity(intent)
    }

    private fun initAnimations() {
        val animation =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.isMyLocationEnabled = false //Error due to permission check
        //googleMap?.uiSettings?.isScrollGesturesEnabled = false

        postViewModel.getPost(args.PostId).observe(viewLifecycleOwner, { post ->
            val location = LatLng(post.latitude, post.longitude)
            requireActivity().log("Location is: $location")

            googleMap?.addMarker(
                MarkerOptions().position(location).title(post.address)
                    .snippet("${post.region}, ${post.country}")
            )

            val cameraPosition = CameraPosition.Builder()
                .target(location)
                .zoom(16f)
                .build()

            googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        })
    }

    override fun onLoading() {
        binding.progressBarPostDetail.show()
    }

    override fun onSuccess(message: String) {
        binding.progressBarPostDetail.hide()
        //requireActivity().toast(message)
        requireActivity().log(message)
    }

    override fun onFailure(message: String) {
        binding.progressBarPostDetail.hide()
        //requireActivity().toast(message)
        requireActivity().log(message)
    }

}