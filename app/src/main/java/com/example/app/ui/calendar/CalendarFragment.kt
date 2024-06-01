package com.example.app.ui.calendar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.app.databinding.FragmentCalendarBinding
import com.example.app.R
import com.example.app.ui.today.ViewDay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var calendarViewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        calendarViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(CalendarViewModel::class.java)
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val sharedPref = requireActivity().getSharedPreferences("hash", Context.MODE_PRIVATE)
        val hash_va = sharedPref.getString("hash", "")
        val textView: TextView = binding.textViewMonth
        val buttonLeft: ImageButton = binding.imageButtonLeft
        val buttonRight: ImageButton = binding.imageButtonRight

        calendarViewModel.date.observe(viewLifecycleOwner) { date ->
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru"))
            val nowDate = date.format(formatter)
            val nowDateN = nowDate.split(" ").let {
                "${it[0]} ${it[1].uppercase()} ${it[2]}"
            }
            textView.text = nowDateN

            val fformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale("ru"))
            val dDate = date.format(fformatter)

            // Обновление данных с сервера
            val day = ViewDay(hash_va, nowDateN)
            println(dDate)
            val imageViewGen: ImageView = binding.imageViewGen
            val imageViewWeather: ImageView = binding.imageViewWeather
            val imageViewPlace: ImageView = binding.imageViewPlace
            val imageViewWith: ImageView = binding.imageViewWith

            imageViewGen.setImageResource(R.drawable.nothing)
            imageViewWeather.setImageResource(R.drawable.nothing)
            imageViewPlace.setImageResource(R.drawable.nothing)
            imageViewWith.setImageResource(R.drawable.nothing)
            day.getDate(hash_va, dDate) { response ->
                if (response[0] != "8") {
                    updateUI(response)
                    calendarViewModel.setDataFromServer(response)
                } else {
                    // Обработайте ошибку
                }
            }
        }

        buttonLeft.setOnClickListener {
            val newDate = calendarViewModel.date.value?.minusDays(1)
            newDate?.let { calendarViewModel.setDate(it) }
        }

        buttonRight.setOnClickListener {
            val newDate = calendarViewModel.date.value?.plusDays(1)
            newDate?.let { calendarViewModel.setDate(it) }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUI(data: Array<String>) {
        // Обновление UI на основе данных
        val textViewThinksN: TextView = binding.textViewThinksN

        val imageViewGen: ImageView = binding.imageViewGen
        val imageViewWeather: ImageView = binding.imageViewWeather
        val imageViewPlace: ImageView = binding.imageViewPlace
        val imageViewWith: ImageView = binding.imageViewWith

        // Обновление текстового поля "Thinks"
        textViewThinksN.text = data[4]

        // Обновление изображений на основе данных
        imageViewGen.setImageResource(getDrawableResIdByMood(data[0]))
        imageViewWeather.setImageResource(getDrawableResIdByWeather(data[1]))
        imageViewPlace.setImageResource(getDrawableResIdByPlace(data[2]))
        imageViewWith.setImageResource(getDrawableResIdByWith(data[3]))
    }

    private fun getDrawableResIdByMood(mood: String): Int {
        return when (mood) {
            "1" -> R.drawable.terrible
            "2" -> R.drawable.sad
            "3" -> R.drawable.soso
            "4" -> R.drawable.good
            "5" -> R.drawable.happy
            else -> R.drawable.nothing
        }
    }

    private fun getDrawableResIdByWeather(weather: String): Int {
        return when (weather) {
            "1" -> R.drawable.lighting
            "2" -> R.drawable.rain
            "3" -> R.drawable.snowy
            "4" -> R.drawable.cloudy
            "5" -> R.drawable.sunny
            else -> R.drawable.nothing
        }
    }

    private fun getDrawableResIdByPlace(place: String): Int {
        return when (place) {
            "1" -> R.drawable.plane
            "2" -> R.drawable.shop
            "3" -> R.drawable.work
            "4" -> R.drawable.nature
            "5" -> R.drawable.home
            else -> R.drawable.nothing
        }
    }

    private fun getDrawableResIdByWith(with: String): Int {
        return when (with) {
            "1" -> R.drawable.alone
            "2" -> R.drawable.wwork
            "3" -> R.drawable.friends
            "4" -> R.drawable.family
            "5" -> R.drawable.love
            else -> R.drawable.nothing
        }
    }
}
