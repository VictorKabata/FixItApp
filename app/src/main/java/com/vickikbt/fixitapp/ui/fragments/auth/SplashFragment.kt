package com.vickikbt.fixitapp.ui.fragments.auth

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.data.preferences.ThemePreference
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val viewModel by viewModels<UserViewModel>()
    private lateinit var themePreferenceRepo: ThemePreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_splash, container, false)

        Handler().postDelayed({
            viewModel.getCurrentUser.observe(viewLifecycleOwner, Observer { user ->
                if (user == null) {
                    findNavController().navigate(R.id.splash_to_login)
                } else {
                    findNavController().navigate(R.id.splash_to_home)
                }
            })
        }, 1200)

        //initTheme()

        return root
    }

    private fun initTheme() {
        val pref = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        themePreferenceRepo = ThemePreference(pref)

        themePreferenceRepo.nightModeLive.observe(viewLifecycleOwner, {
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
    }

}