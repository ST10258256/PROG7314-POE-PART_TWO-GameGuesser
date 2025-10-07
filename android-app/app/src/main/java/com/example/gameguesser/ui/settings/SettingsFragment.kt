package com.example.gameguesser.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gameguesser.R
import com.example.gameguesser.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val vm: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnProfile.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragment)

        }


//        binding.btnAccount.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainer, AccountFragment())
//                .addToBackStack(null)
//                .commit()
//        }
//
//        binding.btnHelp.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainer, HelpFragment())
//                .addToBackStack(null)
//                .commit()
//        }
//
//        binding.btnTandC.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainer, TermsFragment())
//                .addToBackStack(null)
//                .commit()
//        }
//
//        binding.btnLanguages.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainer, LanguagesFragment())
//                .addToBackStack(null)
//                .commit()
//        }

        binding.btnLogout.setOnClickListener {
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
            // TO DO: Clear user session / SharedPreferences
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
