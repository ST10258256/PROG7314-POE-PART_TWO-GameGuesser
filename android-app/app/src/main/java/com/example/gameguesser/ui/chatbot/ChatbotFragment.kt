package com.example.gameguesser.ui.chatbot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gameguesser.databinding.FragmentChatbotBinding

class ChatbotFragment : Fragment() {

    private var _binding: FragmentChatbotBinding? = null
    private val binding get() = _binding!!
    private val vm: ChatbotViewModel by viewModels()
    private lateinit var adapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatbotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = ChatAdapter()
        binding.messagesRecycler.layoutManager = LinearLayoutManager(requireContext()).apply { stackFromEnd = true }
        binding.messagesRecycler.adapter = adapter


        vm.messages.observe(viewLifecycleOwner) { list ->
            adapter.setMessages(list)
            if (adapter.itemCount > 0) {
                binding.messagesRecycler.scrollToPosition(adapter.itemCount - 1)
            }
        }

        binding.sendButton.setOnClickListener { sendCurrentText() }


        binding.messageEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendCurrentText()
                true
            } else false
        }
    }

    private fun sendCurrentText() {
        val txt = binding.messageEditText.text.toString().trim()
        if (txt.isNotEmpty()) {
            vm.sendMessage(txt)
            binding.messageEditText.text?.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
