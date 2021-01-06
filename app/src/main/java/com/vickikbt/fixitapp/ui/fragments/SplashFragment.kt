package com.vickikbt.fixitapp.ui.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.ui.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val viewModel by viewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_splash, container, false)

        Handler().postDelayed({
            viewModel.getLoggedInUser.observe(viewLifecycleOwner, Observer { user->
                if (user==null){
                    findNavController().navigate(R.id.splash_to_login)
                }else{
                    findNavController().navigate(R.id.splash_to_home)
                }
            })
        },1200)

        //initTheme()

        return root
    }

    /*private fun initTheme() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        themePreferenceRepo = ThemePreference(pref)

        themePreferenceRepo.nightModeLive.observe(this, Observer {
            when (it) {
                "Light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                "Dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                "System" ->
                    if (Build.VERSION.SDK_INT >= 29)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    else
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            }
        })
    }*/

}