package com.example.gameguesser.ui.gguesser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gameguesser.R
import com.example.gameguesser.data.Game
import com.example.gameguesser.databinding.FragmentGguesserBinding
import kotlinx.coroutines.launch

class GGuesserFragment : Fragment() {

    private var _binding: FragmentGguesserBinding? = null
    private val binding get() = _binding!!
    private val vm: GGuesserViewModel by viewModels()

    private lateinit var searchAdapter: SearchGameAdapter
    private lateinit var guessAdapter: GuessResultAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGguesserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchAdapter = SearchGameAdapter { game -> onGameSelected(game) }
        guessAdapter = GuessResultAdapter()

        binding.recyclerSearch.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }

        binding.recyclerGuesses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = guessAdapter
        }

        binding.buttonFetchRandom.setOnClickListener {
            vm.fetchRandomTarget()
        }

        binding.searchInput.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val q = binding.searchInput.text.toString().trim()
                if (q.isNotEmpty()) vm.searchGames(q)
                true
            } else false
        }

        binding.buttonSearch.setOnClickListener {
            val q = binding.searchInput.text.toString().trim()
            if (q.isNotEmpty()) vm.searchGames(q)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        vm.target.observe(viewLifecycleOwner) { target ->
            if (target == null) {
                binding.textTargetName.text = "No target"
                binding.imageTargetCover.setImageResource(R.drawable.ic_launcher_foreground)
            } else {
                binding.textTargetName.text = "Target: ${target.name}"
                Glide.with(requireContext()).load(target.coverImageUrl).centerCrop().into(binding.imageTargetCover)
            }
        }

        vm.searchResults.observe(viewLifecycleOwner) { list ->
            searchAdapter.update(list)
            binding.textSearchEmpty.isVisible = list.isEmpty()
        }

        vm.loading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }

        vm.error.observe(viewLifecycleOwner) {
            binding.textError.text = it ?: ""
            binding.textError.isVisible = !it.isNullOrEmpty()
        }

        vm.guessHistory.observe(viewLifecycleOwner) { history ->
            guessAdapter.update(history)
            binding.textNoGuesses.isVisible = history.isEmpty()
        }
    }

    private fun onGameSelected(game: Game) {
        // When user selects a game from search, submit it as a guess
        lifecycleScope.launch {
            vm.submitGuess(game)
            // optionally clear search
            binding.searchInput.text?.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
