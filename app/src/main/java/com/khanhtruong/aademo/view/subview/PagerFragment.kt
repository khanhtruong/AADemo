package com.khanhtruong.aademo.view.subview

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.khanhtruong.aademo.R
import com.khanhtruong.aademo.adapter.ImagePagerAdapter
import com.khanhtruong.aademo.databinding.FragmentPagerBinding
import com.khanhtruong.aademo.viewmodel.GridViewModel

class PagerFragment : Fragment(), ImageInteraction {

    private lateinit var binding: FragmentPagerBinding
    private val viewModel by activityViewModels<GridViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPagerBinding.inflate(inflater)

        setupImage()
        prepareSharedElementTransition()
        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
        if (savedInstanceState == null) {
            postponeEnterTransition()
        }

        return binding.pgImage
    }

    private fun setupImage() {
        binding.pgImage.adapter = ImagePagerAdapter(this, this)
        binding.pgImage.currentItem = viewModel.selectedPosition()
        binding.pgImage.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                viewModel.newSelectedPosition(position)
            }
        })
    }

    /**
     * Prepares the shared element transition from and back to the grid fragment.
     */
    private fun prepareSharedElementTransition() {
        val transition = TransitionInflater.from(
            requireContext()
        ).inflateTransition(
            R.transition.img_shared_element_transition
        )
        sharedElementEnterTransition = transition

        // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
        setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: List<String>,
                    sharedElements: MutableMap<String, View>
                ) {
                    // Locate the image view at the primary fragment (the ImageFragment that is currently
                    // visible). To locate the fragment, call instantiateItem with the selection position.
                    // At this stage, the method will simply return the fragment at the position and will
                    // not create a new one.
                    val currentFragment = binding.pgImage.adapter
                        ?.instantiateItem(
                            binding.pgImage,
                            viewModel.selectedPosition()
                        ) as Fragment?
                    currentFragment?.view?.findViewById<View>(R.id.imgPagerCard)?.also {
                        sharedElements[names[0]] = it
                    }
                }
            })
    }

    /**
     * [ImageInteraction] from ImageFragment support notify [PagerFragment]
     * whenever the image is ready to begin transition animation
     **/
    override fun onImageLoaded() {
        this.startPostponedEnterTransition()
    }
}