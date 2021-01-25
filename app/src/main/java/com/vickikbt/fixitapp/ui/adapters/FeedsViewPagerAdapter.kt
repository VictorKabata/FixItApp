package com.vickikbt.fixitapp.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.vickikbt.fixitapp.ui.fragments.FeedsPostFragment
import com.vickikbt.fixitapp.ui.fragments.FeedsWorkFragment

class FeedsViewPagerAdapter constructor(fragmentManager: FragmentManager) : FragmentPagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getCount() = 2

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FeedsPostFragment()
            else -> FeedsWorkFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Posts"
            else -> "Work"
        }
    }

}