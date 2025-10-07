package com.example.gameguesser.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gameguesser.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Example: Load current user info (replace with actual user data)
        binding.etUsername.setText("John Doe")
        binding.etPhone.setText("0821234567")
        binding.tvEmail.text = "johndoe@gmail.com"

        // Disable editing for email
        binding.tvEmail.isEnabled = false

        // Handle Save button click
        binding.btnSaveChanges.setOnClickListener {
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        val username = binding.etUsername.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()

        if (username.isEmpty() || phone.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Changes")
            .setMessage("Are you sure you want to save these changes?")
            .setPositiveButton("Yes") { _, _ ->
                // TODO: Save updated info to your data source
                Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
