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

class GameAdapter( private var games: List<Game>,
                   private val onItemClick: (Game) -> Unit   // ✅ <--- expects one parameter now
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

        if (game.coverImagePath.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(game.coverImagePath)
                .into(holder.imageCover)
        }

        holder.itemView.setOnClickListener {
            onItemClick(game)   // ✅ Pass the clicked game
        }
    }

    override fun getItemCount(): Int = games.size

    fun updateGames(newGames: List<Game>) {
        games = newGames
        notifyDataSetChanged()
    }
}