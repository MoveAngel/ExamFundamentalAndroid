package com.exam.fundanavapii.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.exam.fundanavapii.R
import com.exam.fundanavapii.ui.viewmodel.ThemeViewModel

class SettingsFragment : Fragment() {

    private lateinit var themeViewModel: ThemeViewModel
    private lateinit var switchDarkMode: SwitchCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_settings, container, false)
        themeViewModel = ViewModelProvider(requireActivity())[ThemeViewModel::class.java]
        switchDarkMode = view.findViewById(R.id.switchDarkMode)

        themeViewModel.isDarkMode.observe(viewLifecycleOwner) { isDarkMode ->
            switchDarkMode.isChecked = isDarkMode
        }

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            themeViewModel.setDarkMode(isChecked)
        }

        return view
    }
}
