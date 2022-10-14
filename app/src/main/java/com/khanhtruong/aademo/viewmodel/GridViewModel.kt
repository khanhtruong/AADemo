package com.khanhtruong.aademo.viewmodel

import androidx.lifecycle.ViewModel

class GridViewModel: ViewModel() {
    private var selectedPosition: Int = 0

    fun newSelectedPosition(position: Int) {
        this.selectedPosition = position
    }
    fun selectedPosition(): Int {
        return selectedPosition
    }

}