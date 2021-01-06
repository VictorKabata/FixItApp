package com.vickikbt.fixitapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentLoginBinding
import com.vickikbt.fixitapp.ui.viewmodels.UserViewModel
import com.vickikbt.fixitapp.utils.StateListener
import com.vickikbt.fixitapp.utils.hide
import com.vickikbt.fixitapp.utils.show
import com.vickikbt.fixitapp.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.viewModel = viewModel
        viewModel.stateListener = this

        binding.linearLayoutRegister.setOnClickListener {
            findNavController().navigate(R.id.login_to_register)
        }

        return binding.root
    }


    override fun onLoading() {
        binding.progressBarLogin.show()
    }

    override fun onSuccess(message: String) {
        binding.progressBarLogin.hide()
        requireActivity().toast(message)
        findNavController().navigate(R.id.login_to_home)
    }

    override fun onFailure(message: String) {
        binding.progressBarLogin.hide()
        requireActivity().toast(message)
    }

}