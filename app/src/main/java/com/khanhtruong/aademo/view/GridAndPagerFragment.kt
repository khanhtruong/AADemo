package com.khanhtruong.aademo.view

import android.os.Bundle
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.khanhtruong.aademo.R
import com.khanhtruong.aademo.adapter.GridAdapter
import com.khanhtruong.aademo.adapter.GridListener
import com.khanhtruong.aademo.databinding.FragmentGridBinding
import com.khanhtruong.aademo.util.consts.Companion.CURRENT_POSITION_TAG
import com.khanhtruong.aademo.viewmodel.GridViewModel

class GridAndPagerFragment : Fragment(), GridListener {

    private lateinit var binding: FragmentGridBinding
    private val viewModel by activityViewModels<GridViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGridBinding.inflate(inflater)
        binding.rvGrid.adapter = GridAdapter(this)

        prepareTransitions()
        postponeEnterTransition()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scrollToPosition()
    }

    private fun scrollToPosition() {
        binding.rvGrid.addOnLayoutChangeListener(object : OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                binding.rvGrid.removeOnLayoutChangeListener(this)
                val layoutManager = binding.rvGrid.layoutManager ?: return
                val selectedPos = viewModel.selectedPosition()
                val viewAtPosition = layoutManager.findViewByPosition(selectedPos)
                // Scroll to position if the view for the current position is null (not currently part of
                // layout manager children), or it's not completely visible.
                if (viewAtPosition == null || layoutManager.isViewPartiallyVisible(
                        viewAtPosition,
                        false,
                        true
                    )
                ) {
                    binding.rvGrid.post {
                        layoutManager.scrollToPosition(selectedPos)
                    }
                }
            }
        })
    }

    private fun prepareTransitions() {
        exitTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.grid_exit_transition)

        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
                // Locate the ViewHolder for the clicked position.
                val selectedViewHolder: RecyclerView.ViewHolder =
                    binding.rvGrid.findViewHolderForAdapterPosition(viewModel.selectedPosition())
                        ?: return
                // Map the first shared element name to the child ImageView.
                sharedElements[names[0]] = selectedViewHolder.itemView.findViewById(R.id.imgCard)
            }
        })
    }

    override fun onItemClick(position: Int) {
        viewModel.newSelectedPosition(position)
        binding.rvGrid.findViewHolderForAdapterPosition(position)?.also {
            (exitTransition as TransitionSet).excludeTarget(view, true)
            val transitionView = it.itemView.findViewById<View>(R.id.imgCard)
            val extras = FragmentNavigatorExtras(transitionView to transitionView.transitionName)
            val bundle = Bundle()
            bundle.putInt(CURRENT_POSITION_TAG, position)
            this.findNavController().navigate(
                R.id.action_gridAndPagerFragment_to_pagerFragment,
                bundle,
                null,
                extras
            )
        }
    }

    override fun onTransitionViewReady(position: Int) {
        /**
         * After item shared between transition is loaded, calling [startPostponedEnterTransition]
         * to start the animation transition
         **/
        if (position == viewModel.selectedPosition()) {
            this.startPostponedEnterTransition()
        }
    }
}