package com.example.gceolmcqs.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.gceolmcqs.fragments.Paper2ExamTypeFragment

class Paper2TabPagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>): FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {

        return fragments[position]
    }
}