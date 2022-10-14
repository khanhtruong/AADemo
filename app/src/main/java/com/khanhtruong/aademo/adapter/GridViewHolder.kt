package com.khanhtruong.aademo.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.khanhtruong.aademo.databinding.ImageCardBinding
import com.khanhtruong.aademo.util.consts

class GridViewHolder(private val binding: ImageCardBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(position: Int, listener: GridListener) {
        val imageRes = consts.IMAGE_DRAWABLES[position]

        binding.imgCard.setImageResource(imageRes)
        ViewCompat.setTransitionName(binding.imgCard, imageRes.toString())
        binding.root.setOnClickListener {
            listener.onItemClick(position)
        }
        Glide.with(binding.root)
            .load(imageRes)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    listener.onTransitionViewReady(position)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    listener.onTransitionViewReady(position)
                    return false
                }

            })
            .into(binding.imgCard)
    }
}