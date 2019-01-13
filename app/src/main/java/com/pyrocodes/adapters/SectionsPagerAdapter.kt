package com.pyrocodes.adapters

import com.pyrocodes.fragments.CreateFragment

class SectionsPagerAdapter(fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return CreateFragment.newInstance("", "" + position)
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 3
    }
}