package com.example.app.ui.today

import java.time.LocalDate
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.app.databinding.FragmentTodayBinding
import androidx.appcompat.widget.AppCompatButton

class TodayFragment : Fragment() {
    private lateinit var editText: EditText
    private val sharedPrefsFile = "com.example.autosaveedittext.prefs"
    private val sharedPrefsKey = "textEditThinks"
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!
    private lateinit var todayViewModel: TodayViewModel
    private var buttonGen = intArrayOf(0, 0, 0, 0, 0)
    private var buttonWeather = intArrayOf(0, 0, 0, 0, 0)
    private var buttonPlace = intArrayOf(0, 0, 0, 0, 0)
    private var buttonWith = intArrayOf(0, 0, 0, 0, 0)
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val Date = LocalDate.now()
        val nowDate = Date.toString()
        todayViewModel = ViewModelProvider(this).get(TodayViewModel::class.java)
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = requireContext().getSharedPreferences(sharedPrefsFile, Context.MODE_PRIVATE)
        editText = binding.editTextThinks
        loadSavedText()
//        val now = SaveDay("", "", "", "", "", nowDate)
//        now.date(nowDate) {response ->
//            if(response == "1"){
//
//            }
//        }


        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                saveText(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed
            }
        })

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

        // Set initial transparency for all buttons
        setInitialButtonTransparency(buttonsGen)
        setInitialButtonTransparency(buttonsWeather)
        setInitialButtonTransparency(buttonsPlace)
        setInitialButtonTransparency(buttonsWith)

        // Observe the selected button index for Gen buttons from ViewModel
        todayViewModel.selectedGenButtonIndex.observe(viewLifecycleOwner, { index ->
            selectButton(buttonsGen, index)
        })

        // Observe the selected button index for Weather buttons from ViewModel
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
        val buttonSave: AppCompatButton = binding.buttonSave
//        buttonSave.setOnClickListener {
//            val inputText = editText.text.toString()
//            var gen: String = "0"
//            var weather: String = "0"
//            var place: String = "0"
//            var with: String = "0"
//            for (i in 1..3) {
//                if (buttonGen[i] == 1){
//                    gen = (i+1).toString()
//                }
//                if (buttonWeather[i] == 1){
//                    weather = (i+1).toString()
//                }
//                if (buttonPlace[i] == 1){
//                    place = (i+1).toString()
//                }
//                if (buttonWith[i] == 1){
//                    with = (i+1).toString()
//                }
//            }
//
//            val day = SaveDay(inputText, gen, weather, place, with, nowDate)
//            day.sendDay(inputText, gen, weather, place, with, nowDate) {response ->
//
//            }

        }
        return root
    }

    private fun setInitialButtonTransparency(buttons: List<ImageButton>) {
        for (button in buttons) {
            button.alpha = 0.5f // Set transparency to 50%
            button.isSelected = false
        }
    }

    private fun updateButton(selectedButtons: IntArray, selectedIndex: Int) {
        for (i in selectedButtons.indices) {
            selectedButtons[i] = if (i == selectedIndex) 1 else 0
        }
    }

    // Method to select button and update its transparency
    private fun selectButton(buttons: List<ImageButton>, selectedIndex: Int?) {
        for ((index, button) in buttons.withIndex()) {
            val isSelected = index == selectedIndex
            button.alpha = if (isSelected) 1.0f else 0.5f
            button.isSelected = isSelected
        }
    }

    private fun saveText(text: String) {
        with(sharedPreferences.edit()) {
            putString(sharedPrefsKey, text)
            apply()
        }
    }

    private fun loadSavedText() {
        val savedText = sharedPreferences.getString(sharedPrefsKey, "")
        editText.setText(savedText)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
