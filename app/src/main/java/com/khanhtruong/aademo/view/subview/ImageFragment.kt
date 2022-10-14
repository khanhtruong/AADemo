package com.khanhtruong.aademo.view.subview

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.khanhtruong.aademo.databinding.FragmentImageBinding

interface ImageInteraction {
    fun onImageLoaded()
}

class ImageFragment(private val listener: ImageInteraction) : Fragment() {

    companion object {
        private val KEY_IMAGE_RES = "com.khanhtruong.aademo.imageRes"

        fun newInstance(
            @DrawableRes drawableRes: Int,
            imageInteraction: ImageInteraction
        ): ImageFragment {
            val fragment = ImageFragment(imageInteraction)
            val argument = Bundle()
            argument.putInt(KEY_IMAGE_RES, drawableRes)
            fragment.arguments = argument
            return fragment
        }
    }

    private lateinit var binding: FragmentImageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageBinding.inflate(inflater)
        loadImage()
        return binding.imgPagerCard
    }

    private fun loadImage() {
        @DrawableRes val imageRes = arguments?.getInt(KEY_IMAGE_RES) ?: 0

        binding.imgPagerCard.transitionName = imageRes.toString()

        Glide.with(requireContext())
            .load(imageRes)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    listener.onImageLoaded()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    listener.onImageLoaded()
                    return false
                }
            }).into(binding.imgPagerCard)
    }
}