package com.example.gameguesser.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gameguesser.databinding.FragmentLanguageBinding

class LanguagesFragment : Fragment() {

    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.btnSaveLanguages.setOnClickListener {
            val selectedLanguages = mutableListOf<String>()

            if (binding.cbEnglish.isChecked) selectedLanguages.add("English")
            if (binding.cbAfikaaans.isChecked) selectedLanguages.add("Afrikaans")
            if (binding.cbSpanish.isChecked) selectedLanguages.add("Spanish")
            if (binding.cbFrench.isChecked) selectedLanguages.add("French")
            if (binding.cbZulu.isChecked) selectedLanguages.add("Zulu")

            Toast.makeText(
                requireContext(),
                "Selected languages: ${selectedLanguages.joinToString(", ")}",
                Toast.LENGTH_SHORT
            ).show()

            // TO DO: Save the selected languages to user preferences
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
