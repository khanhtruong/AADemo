package com.khanhtruong.aademo.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.fragment.app.Fragment
import com.khanhtruong.aademo.databinding.FragmentSpringBinding

class SpringFragment : Fragment() {
    private lateinit var binding: FragmentSpringBinding

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var springAnimationX: SpringAnimation
    private lateinit var springAnimationY: SpringAnimation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpringBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupView()
        setupAnimation()
        setupGestureDetector()
        setupEventListener()
    }

    private fun setupView() {
        // Initial text, progress, min, and max value for seek bar
        binding.sbStiffness.min = 50
        binding.sbStiffness.max = 10_000
        binding.sbDampingRatio.min = 0
        binding.sbDampingRatio.max = 100
        binding.txtDampingRatio.text = SpringForce.DAMPING_RATIO_LOW_BOUNCY.toString()
        binding.sbDampingRatio.progress = (SpringForce.DAMPING_RATIO_LOW_BOUNCY * 100).toInt()
        binding.txtStiffness.text = SpringForce.STIFFNESS_MEDIUM.toString()
        binding.sbStiffness.progress = SpringForce.STIFFNESS_MEDIUM.toInt()
    }

    private fun setupAnimation() {
        springAnimationX = binding.imgIcon.let { imageView ->
            SpringAnimation(imageView, SpringAnimation.SCALE_X).apply {
                val springForce = SpringForce(1f)
                springForce.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
                springForce.stiffness = SpringForce.STIFFNESS_MEDIUM
                spring = springForce
            }
        }
        springAnimationY = binding.imgIcon.let { imageView ->
            SpringAnimation(imageView, SpringAnimation.SCALE_Y).apply {
                val springForce = SpringForce(1f)
                springForce.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
                springForce.stiffness = SpringForce.STIFFNESS_MEDIUM
                spring = springForce
            }
        }
    }

    private fun setupEventListener() {
        binding.imgIcon.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                // Perform click will prevent side-effect for this view
                // If not doing this, later on other listeners may not be triggered
                // because it already being handled here
                v.performClick()
                springAnimationX.start()
                springAnimationY.start()
            } else {
                springAnimationX.cancel()
                springAnimationY.cancel()

                val x = event.x
                val y = event.y
                event.setLocation(event.rawX, event.rawY)
                scaleGestureDetector.onTouchEvent(event)
                event.setLocation(x, y)
            }
            true
        }

        binding.sbDampingRatio.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                springAnimationX.spring.dampingRatio = (p1.toFloat() / 100)
                springAnimationY.spring.dampingRatio = (p1.toFloat() / 100)
                binding.txtDampingRatio.text = (p1.toFloat() / 100).toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit
            override fun onStopTrackingTouch(p0: SeekBar?) = Unit
        })

        binding.sbStiffness.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                springAnimationX.spring.stiffness = p1.toFloat()
                springAnimationY.spring.stiffness = p1.toFloat()
                binding.txtStiffness.text = p1.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit
            override fun onStopTrackingTouch(p0: SeekBar?) = Unit
        })
    }

    private fun setupGestureDetector() {
        // Gesture detector will allow us to listen to users interact
        // At this time is the scaling event
        //
        // Important notes:
        // If we not going call `onTouchEvent(event)` function, the view we aim to scale/drag won't move
        // So remember to call `scaleGestureDetector.onTouchEvent(event)` in `onTouchListener` for the specific view
        var scaleFactor = 1f
        scaleGestureDetector = ScaleGestureDetector(requireActivity(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    scaleFactor *= detector.scaleFactor
                    binding.imgIcon.scaleX *= scaleFactor
                    binding.imgIcon.scaleY *= scaleFactor
                    return true
                }
            })
    }
}