package com.example.app.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TodayViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    private val _selectedGenButtonIndex = MutableLiveData<Int?>()
    val selectedGenButtonIndex: LiveData<Int?> get() = _selectedGenButtonIndex

    private val _selectedWeatherButtonIndex = MutableLiveData<Int?>()

    val selectedWeatherButtonIndex: LiveData<Int?> get() = _selectedWeatherButtonIndex

    private val _selectedPlaceButtonIndex = MutableLiveData<Int?>()

    val selectedPlaceButtonIndex: LiveData<Int?> get() = _selectedPlaceButtonIndex

    private val _selectedWithButtonIndex = MutableLiveData<Int?>()

    val selectedWithButtonIndex: LiveData<Int?> get() = _selectedWithButtonIndex
    fun setSelectedGenButtonIndex(index: Int) {
        _selectedGenButtonIndex.value = index
    }

    fun setSelectedWeatherButtonIndex(index: Int) {
        _selectedWeatherButtonIndex.value = index
    }
    fun setSelectedPlaceButtonIndex(index: Int) {
        _selectedPlaceButtonIndex.value = index
    }
    fun setSelectedWithButtonIndex(index: Int) {
        _selectedWithButtonIndex.value = index
    }
}