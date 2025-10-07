package com.example.gameguesser.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gameguesser.databinding.FragmentDeleteAccountBinding

class DeleteAccountFragment : Fragment() {

    private var _binding: FragmentDeleteAccountBinding? = null
    private val binding get() = _binding!!

    private var firstConfirmation = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeleteAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDeleteAccount.setOnClickListener {
            if (!firstConfirmation) {
                showFirstConfirmation()
            } else {
                deleteUserProfile()
            }
        }
    }

    private fun showFirstConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete your account?")
            .setPositiveButton("Yes") { _, _ ->
                firstConfirmation = true
                Toast.makeText(requireContext(), "Press 'Delete my account' again to confirm", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteUserProfile() {
        // TO DO: Add the deletion logic here
        Toast.makeText(requireContext(), "Profile deleted successfully", Toast.LENGTH_SHORT).show()

        // Optionally navigate back to login or splash screen
        // findNavController().navigate(R.id.loginActivity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
