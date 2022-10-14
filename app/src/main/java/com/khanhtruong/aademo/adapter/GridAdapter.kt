package com.khanhtruong.aademo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khanhtruong.aademo.databinding.ImageCardBinding
import com.khanhtruong.aademo.util.consts

interface GridListener {
    /**
     *  [onItemClick] will be call whenever an item is clicked
     **/
    fun onItemClick(position: Int)

    /**
     * [onTransitionViewReady] will be call after image loaded correctly
     **/
    fun onTransitionViewReady(position: Int)
}

class GridAdapter(private val listener: GridListener): RecyclerView.Adapter<GridViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding = ImageCardBinding.inflate(LayoutInflater.from(parent.context))
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.bind(position, listener)
    }

    override fun getItemCount() = consts.IMAGE_DRAWABLES.count()
}