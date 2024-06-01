package com.example.app.ui.calendar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.time.LocalDate

class CalendarViewModel(application: Application) : AndroidViewModel(application) {
    val date = MutableLiveData<LocalDate>()

    init {
        date.value = LocalDate.now() // Установите начальное значение даты
    }

    fun setDate(newDate: LocalDate) {
        date.value = newDate
    }

    fun setDataFromServer(response: Array<String>) {
        // Обработка данных с сервера
    }
}
