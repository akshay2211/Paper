package com.pyrocodes.adapters

import com.pyrocodes.fragments.CalendarFragment
import com.pyrocodes.fragments.CreateFragment

class SectionsPagerAdapter(fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return when (position) {
            0 -> CreateFragment.newInstance("", "" + position)
            1 -> CalendarFragment.newInstance("", "" + position)
            else -> CalendarFragment.newInstance("", "" + position)
        }
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 3
    }
}