package com.example.app.ui.today

import java.time.LocalDate
import java.util.Locale
import java.time.format.DateTimeFormatter
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
import android.widget.Toast
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
    private lateinit var shared_Preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val Date = LocalDate.now()
        val nowDate = Date.toString()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val nowDateN = nowDate.format(formatter)
        val sharedPref = requireActivity().getSharedPreferences("hash", Context.MODE_PRIVATE)
        val hash_va = sharedPref.getString("hash", "")
        todayViewModel = ViewModelProvider(this).get(TodayViewModel::class.java)
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        val root: View = binding.root
        shared_Preferences = requireContext().getSharedPreferences(sharedPrefsFile, Context.MODE_PRIVATE)
        editText = binding.editTextThinks
        loadSavedText()



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

        if(hash_va != ""){
            val now = SaveDay(hash_va,"", 0, 0, 0, 0, nowDateN)
            now.date(hash_va, nowDateN) { response ->
                if(response[0] != "8") {
                    val genIndex = response[0].toIntOrNull() ?: 0
                    val weatherIndex = response[1].toIntOrNull() ?: 0
                    val placeIndex = response[2].toIntOrNull() ?: 0
                    val withIndex = response[3].toIntOrNull() ?: 0
                    val thinks = response[4]

                    todayViewModel.setSelectedGenButtonIndex(genIndex-1)
                    selectButton(buttonsGen, genIndex-1)
                    updateButton(buttonGen, genIndex-1)

                    todayViewModel.setSelectedWeatherButtonIndex(weatherIndex-1)
                    selectButton(buttonsWeather, weatherIndex-1)
                    updateButton(buttonWeather, weatherIndex-1)

                    todayViewModel.setSelectedPlaceButtonIndex(placeIndex-1)
                    selectButton(buttonsPlace, placeIndex-1)
                    updateButton(buttonPlace, placeIndex-1)

                    todayViewModel.setSelectedWithButtonIndex(withIndex-1)
                    selectButton(buttonsWith, withIndex-1)
                    updateButton(buttonWith, withIndex-1)

                    editText.setText(thinks)
                }
            }
        }

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
        buttonSave.setOnClickListener {
            val inputText = shared_Preferences.getString(sharedPrefsKey, "")
            var gen: String = "0"
            var weather: String = "0"
            var place: String = "0"
            var with: String = "0"
            for (i in 0 until buttonGen.size) {
                if (buttonGen[i] == 1){
                    gen = (i + 1).toString()
                }
            }
            for (i in 0 until buttonWeather.size) {
                if (buttonWeather[i] == 1){
                    weather = (i + 1).toString()
                }
            }
            for (i in 0 until buttonPlace.size) {
                if (buttonPlace[i] == 1){
                    place = (i + 1).toString()
                }
            }
            for (i in 0 until buttonWith.size) {
                if (buttonWith[i] == 1){
                    with = (i + 1).toString()
                }
            }

            println(hash_va)
            val day = SaveDay(hash_va, inputText, gen.toInt(), weather.toInt(), place.toInt(), with.toInt(), nowDateN)
            day.sendDay(hash_va, inputText, gen.toInt(), weather.toInt(), place.toInt(), with.toInt(), nowDateN) { response ->
                activity?.runOnUiThread{
                    if (response) {
                        Toast.makeText(requireContext(), "Данные дня сохранены", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(requireContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show()
                    }
                }
            }
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
        with(shared_Preferences.edit()) {
            putString(sharedPrefsKey, text)
            apply()  // или commit()
        }
    }

    private fun loadSavedText() {
        val savedText = shared_Preferences.getString(sharedPrefsKey, "")
        editText.setText(savedText)
    }

    private fun updateUI(buttonArray: IntArray, vararg buttons: ImageButton) {
        for ((index, button) in buttons.withIndex()) {
            val isSelected = buttonArray[index] == 1
            button.alpha = if (isSelected) 1.0f else 0.5f
            button.isSelected = isSelected
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
