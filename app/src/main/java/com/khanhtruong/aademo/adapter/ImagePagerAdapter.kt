package com.khanhtruong.aademo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.khanhtruong.aademo.util.consts.Companion.IMAGE_DRAWABLES
import com.khanhtruong.aademo.view.subview.ImageFragment
import com.khanhtruong.aademo.view.subview.ImageInteraction

class ImagePagerAdapter(
    fragment: Fragment,
    private val imageInteraction: ImageInteraction
) : FragmentStatePagerAdapter(
    fragment.parentFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getCount(): Int {
        return IMAGE_DRAWABLES.count()
    }

    override fun getItem(position: Int): Fragment {
        return ImageFragment.newInstance(IMAGE_DRAWABLES[position], imageInteraction)
    }
}