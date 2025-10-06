package com.example.gameguesser.ui.home

import android.content.Context
import android.content.Intent
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
import com.example.gameguesser.LoginActivity
import com.example.gameguesser.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


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

        binding.btnLogout.setOnClickListener {
            // signs them out of ggole and the app
            val googleSignInClient = GoogleSignIn.getClient(requireContext(),
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build())
            googleSignInClient.signOut().addOnCompleteListener {
                // clears shared pref
                val prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                prefs.edit().clear().apply()

                // goes back to login activity
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        //displays the name in the user label, felt cute might delete later
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        account?.let {
            val displayName = it.displayName ?: "Player"
            binding.usernameText.text = displayName
        }


        // Text binding
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            binding.textHome.text = it
//        }

        // Button click to open KeyGameFragment
//        binding.playButton.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .replace(
//                    (requireView().parent as ViewGroup).id, // Replace current container
//                    KeyGameFragment()
//                )
//                .addToBackStack(null) // So user can press back
//                .commit()
//        }

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