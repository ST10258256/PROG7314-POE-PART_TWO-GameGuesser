package com.example.gameguesser.ui.chatbot

import com.example.gameguesser.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<ChatMessage>()

    private companion object {
        const val TYPE_USER = 0
        const val TYPE_BOT = 1
    }

    override fun getItemViewType(position: Int): Int =
        when (items[position].sender) {
            ChatMessage.Sender.USER -> TYPE_USER
            ChatMessage.Sender.BOT  -> TYPE_BOT
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_USER) {
            val v = inflater.inflate(R.layout.item_message_user, parent, false)
            UserVH(v)
        } else {
            val v = inflater.inflate(R.layout.item_message_bot, parent, false)
            BotVH(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is UserVH) holder.bind(item) else if (holder is BotVH) holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    fun setMessages(list: List<ChatMessage>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun addMessage(m: ChatMessage) {
        items.add(m)
        notifyItemInserted(items.size - 1)
    }

    class UserVH(view: View) : RecyclerView.ViewHolder(view) {
        private val text = view.findViewById<TextView>(R.id.messageText)
        fun bind(m: ChatMessage) { text.text = m.text }
    }

    class BotVH(view: View) : RecyclerView.ViewHolder(view) {
        private val text = view.findViewById<TextView>(R.id.messageText)
        fun bind(m: ChatMessage) { text.text = m.text }
    }
}
