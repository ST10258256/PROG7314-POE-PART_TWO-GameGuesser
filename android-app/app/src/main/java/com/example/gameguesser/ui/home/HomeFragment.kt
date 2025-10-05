package com.example.gameguesser.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gameguesser.databinding.FragmentHomeBinding
import com.example.gameguesser.ui.keyWordGame.KeyGameFragment
import androidx.navigation.fragment.findNavController
import com.example.gameguesser.R
import androidx.navigation.fragment.findNavController


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.playGameGuesserButton.setOnClickListener {
            // navigate using the fragment id we added to the nav graph
            findNavController().navigate(R.id.gguesserFragment)
        }

        binding.playKeyWordsButton.setOnClickListener {
            findNavController().navigate(R.id.keyGameFragment)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}