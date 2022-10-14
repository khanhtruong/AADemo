package com.khanhtruong.aademo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.khanhtruong.aademo.R
import com.khanhtruong.aademo.databinding.FragmentSelectionBinding
import com.khanhtruong.aademo.databinding.FragmentSpringBinding

class SelectionFragment : Fragment() {

    private lateinit var binding: FragmentSelectionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectionBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupViewListener()
    }

    private fun setupViewListener() {
        binding.btnSpringAnimation.setOnClickListener {
            this.findNavController().navigate(R.id.action_selectionFragment_to_springAnimation)
        }
        binding.btnGridAndPager.setOnClickListener {
            this.findNavController().navigate(R.id.action_selectionFragment_to_gridFragment)
        }
    }
}