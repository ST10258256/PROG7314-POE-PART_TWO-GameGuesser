package com.example.gameguesser.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gameguesser.R
import com.example.gameguesser.data.Game

class GameAdapter(
    private var games: List<Game>,
    private val onItemClick: (Game) -> Unit
) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageCover: ImageView = itemView.findViewById(R.id.imageCover)
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        val textGenre: TextView = itemView.findViewById(R.id.textGenre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]

        holder.textTitle.text = game.name
        holder.textGenre.text = game.genre

        // Load image from API
        if (!game.coverImageUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(game.coverImageUrl)
                .into(holder.imageCover)
        } else {
            holder.imageCover.setImageResource(R.drawable.ic_launcher_foreground) // fallback
        }

        holder.itemView.setOnClickListener {
            val mongoId = game.mongoId // or game.id if you want the MongoDB ID
            Log.d("GameClick", "Clicked game: ${game.name}, mongoId: $mongoId")

            onItemClick(game)
        }

    }

    override fun getItemCount(): Int = games.size

    fun updateGames(newGames: List<Game>) {
        games = newGames
        notifyDataSetChanged()
    }
}

