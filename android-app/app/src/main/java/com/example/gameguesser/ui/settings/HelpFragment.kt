package com.example.gameguesser.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gameguesser.databinding.FragmentHelpBinding

class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFaqToggle(binding.btnFaq1, binding.tvFaq1)
        setupFaqToggle(binding.btnFaq2, binding.tvFaq2)
        setupFaqToggle(binding.btnFaq3, binding.tvFaq3)
        setupFaqToggle(binding.btnFaq4, binding.tvFaq4)
        setupFaqToggle(binding.btnFaq5, binding.tvFaq5)
        setupFaqToggle(binding.btnFaq6, binding.tvFaq6)
        setupFaqToggle(binding.btnFaq7, binding.tvFaq7)
    }

    private fun setupFaqToggle(button: View, content: View) {
        button.setOnClickListener {
            content.visibility = if (content.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
