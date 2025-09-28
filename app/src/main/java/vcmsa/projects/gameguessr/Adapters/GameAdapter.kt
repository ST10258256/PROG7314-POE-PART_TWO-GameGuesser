package vcmsa.projects.gameguessr.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vcmsa.projects.gameguessr.Class.Game
import vcmsa.projects.gameguessr.R

class GameAdapter(private var games: List<Game>) :
    RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameImage: ImageView = itemView.findViewById(R.id.imageViewGameCover)
        val gameName: TextView = itemView.findViewById(R.id.textViewGameName)
        val gameGenre: TextView = itemView.findViewById(R.id.textViewGameGenre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.gameName.text = game.name
        holder.gameGenre.text = game.genre

        Glide.with(holder.itemView.context)
            .load(game.coverImagePath)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(holder.gameImage)
    }

    override fun getItemCount(): Int = games.size

    fun updateGames(newGames: List<Game>) {
        games = newGames
        notifyDataSetChanged()
    }
}