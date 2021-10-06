package com.church.sdahymnal.ui.bible

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class BibleViewPagerAdapter(val fragmentManager: FragmentManager, lifecycle : Lifecycle ,val totalCount : Int, val bibleBookId : Int) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return totalCount
    }

    override fun createFragment(position: Int): Fragment {
            return BibleReaderFragment.newInstance(position, bibleBookId)
    }

}