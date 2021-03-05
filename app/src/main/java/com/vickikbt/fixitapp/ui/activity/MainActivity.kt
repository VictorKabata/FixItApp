package com.vickikbt.fixitapp.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.ActivityMainBinding
import com.vickikbt.fixitapp.ui.fragments.auth.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.glide.transformations.BlurTransformation

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_fragment,
                R.id.feeds_fragment,
                R.id.profile_fragment,
                R.id.messages_fragment,
                R.id.settings_fragment
            ), binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.splash_fragment || destination.id == R.id.login_fragment || destination.id == R.id.register_fragment || destination.id == R.id.post_detail_fragment) {
                toolbar.visibility = View.GONE
            } else toolbar.visibility = View.VISIBLE
        }

        initUI()
    }

    private fun initUI() {
        val headerUsername: TextView = binding.navView.getHeaderView(0).findViewById(R.id.userName_navHeader)
        val headerEmail: TextView = binding.navView.getHeaderView(0).findViewById(R.id.emailAddress_navHeader)
        val headerImageView: ImageView = binding.navView.getHeaderView(0).findViewById(R.id.imageView_navHeader)
        val headerImageViewBackground: ImageView = binding.navView.getHeaderView(0).findViewById(R.id.imageView_navHeaderBackground)

        viewModel.getCurrentUser.observe(this, { user ->
            if (user != null) {
                headerUsername.text = user.username
                headerEmail.text = user.email
                Glide.with(this)
                    .load(user.imageUrl)
                    .placeholder(R.drawable.ic_profile_pic)
                    .into(headerImageView)

                Glide.with(this).load(user.imageUrl)
                    .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
                    .into(headerImageViewBackground)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}