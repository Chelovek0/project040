package com.example.app.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.app.databinding.FragmentTodayBinding
import android.widget.ImageButton

class TodayFragment : Fragment() {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!
    private lateinit var todayViewModel: TodayViewModel
    private var buttonGen = intArrayOf(0, 0, 0, 0, 0)
    private var buttonWeather = intArrayOf(0, 0, 0, 0, 0)
    private var buttonPlace = intArrayOf(0, 0, 0, 0, 0)
    private var buttonWith = intArrayOf(0, 0, 0, 0, 0)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        todayViewModel = ViewModelProvider(this).get(TodayViewModel::class.java)

        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val buttonsGen = listOf(
            binding.imageButtonGenTer,
            binding.imageButtonGenSad,
            binding.imageButtonGenSo,
            binding.imageButtonGenGood,
            binding.imageButtonGenHappy
        )
        val buttonsWeather = listOf(
            binding.imageButtonWeatherLight,
            binding.imageButtonWeatherRain,
            binding.imageButtonWeatherSnow,
            binding.imageButtonWeatherCloud,
            binding.imageButtonWeatherSun
        )
        val buttonsPlace = listOf(
            binding.imageButtonPlacePlane,
            binding.imageButtonPlaceShop,
            binding.imageButtonPlaceWork,
            binding.imageButtonPlaceNature,
            binding.imageButtonPlaceHome
        )
        val buttonsWith = listOf(
            binding.imageButtonWithAlone,
            binding.imageButtonWithWork,
            binding.imageButtonWithFriend,
            binding.imageButtonWithFamily,
            binding.imageButtonWithLove
        )

        // Установим начальную прозрачность для всех кнопок
        setInitialButtonTransparency(buttonsGen)
        setInitialButtonTransparency(buttonsWeather)
        setInitialButtonTransparency(buttonsPlace)
        setInitialButtonTransparency(buttonsWith)

        // Наблюдаем за индексом выбранной кнопки для Gen-кнопок из ViewModel
        todayViewModel.selectedGenButtonIndex.observe(viewLifecycleOwner, { index ->
            selectButton(buttonsGen, index)
        })

        // Наблюдаем за индексом выбранной кнопки для Weather-кнопок из ViewModel
        todayViewModel.selectedWeatherButtonIndex.observe(viewLifecycleOwner, { index ->
            selectButton(buttonsWeather, index)
        })

        todayViewModel.selectedPlaceButtonIndex.observe(viewLifecycleOwner, { index ->
            selectButton(buttonsPlace, index)
        })

        todayViewModel.selectedWithButtonIndex.observe(viewLifecycleOwner, { index ->
            selectButton(buttonsWith, index)
        })

        for ((index, button) in buttonsGen.withIndex()) {
            button.setOnClickListener {
                todayViewModel.setSelectedGenButtonIndex(index)
                selectButton(buttonsGen, index)
                updateButton(buttonGen, index)
            }
        }
        for ((index, button) in buttonsWeather.withIndex()) {
            button.setOnClickListener {
                todayViewModel.setSelectedWeatherButtonIndex(index)
                selectButton(buttonsWeather, index)
                updateButton(buttonWeather, index)
            }
        }
        for ((index, button) in buttonsPlace.withIndex()) {
            button.setOnClickListener {
                todayViewModel.setSelectedPlaceButtonIndex(index)
                selectButton(buttonsPlace, index)
                updateButton(buttonPlace, index)
            }
        }
        for ((index, button) in buttonsWith.withIndex()) {
            button.setOnClickListener {
                todayViewModel.setSelectedWithButtonIndex(index)
                selectButton(buttonsWith, index)
                updateButton(buttonWith, index)
            }
        }

        return root
    }

    private fun setInitialButtonTransparency(buttons: List<ImageButton>) {
        for (button in buttons) {
            button.alpha = 0.5f // Устанавливаем прозрачность на 50%
            button.isSelected = false
        }
    }

    private fun updateButton(selectedButtons: IntArray, selectedIndex: Int) {
        for (i in selectedButtons.indices) {
            selectedButtons[i] = if (i == selectedIndex) 1 else 0
        }
    }

    // Метод для выбора кнопки и обновления её прозрачности
    private fun selectButton(buttons: List<ImageButton>, selectedIndex: Int?) {
        for ((index, button) in buttons.withIndex()) {
            val isSelected = index == selectedIndex
            button.alpha = if (isSelected) 1.0f else 0.5f
            button.isSelected = isSelected
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

