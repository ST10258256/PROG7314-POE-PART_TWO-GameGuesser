package com.example.gameguesser.ui.gguesser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gameguesser.R
import com.example.gameguesser.data.Game

class SearchGameAdapter(private val onItemClick: (Game) -> Unit) : RecyclerView.Adapter<SearchGameAdapter.VH>() {

    private var items: List<Game> = emptyList()

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val cover: ImageView = view.findViewById(R.id.imageCover)
        val title: TextView = view.findViewById(R.id.textTitle)
        val genre: TextView = view.findViewById(R.id.textGenre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val g = items[position]
        holder.title.text = g.name
        holder.genre.text = g.genre
        if (!g.coverImageUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context).load(g.coverImageUrl).into(holder.cover)
        } else holder.cover.setImageResource(R.drawable.ic_launcher_foreground)

        holder.itemView.setOnClickListener {
            onItemClick(g)
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(list: List<Game>) {
        items = list
        notifyDataSetChanged()
    }
}
