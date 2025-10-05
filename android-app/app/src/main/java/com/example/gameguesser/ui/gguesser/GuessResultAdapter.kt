package com.example.gameguesser.ui.gguesser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gameguesser.R

class GuessResultAdapter : RecyclerView.Adapter<GuessResultAdapter.VH>() {

    private var items: List<GuessResult> = emptyList()

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val cover: ImageView = view.findViewById(R.id.guessCover)
        val title: TextView = view.findViewById(R.id.guessTitle)
        val comparisonContainer: LinearLayout = view.findViewById(R.id.comparisonContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_guess_result, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.title.text = item.guessedGame.name
        Glide.with(holder.itemView.context).load(item.guessedGame.coverImageUrl).into(holder.cover)

        // populate chips
        holder.comparisonContainer.removeAllViews()
        item.comparisons.forEach { c ->
            val chip = LayoutInflater.from(holder.itemView.context).inflate(R.layout.view_comparison_chip, holder.comparisonContainer, false)
            val tvField = chip.findViewById<TextView>(R.id.chipText)
            tvField.text = "${c.fieldName}: ${c.actual}"

            // color background depending on state
            val bg = when (c.state) {
                MatchState.GREEN -> R.color.match_green
                MatchState.YELLOW -> R.color.match_yellow
                MatchState.RED -> R.color.match_red
            }
            chip.backgroundTintList = ContextCompat.getColorStateList(holder.itemView.context, bg)
            holder.comparisonContainer.addView(chip)
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(list: List<GuessResult>) {
        items = list.reversed() // newest first
        notifyDataSetChanged()
    }
}
