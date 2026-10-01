package com.example.soccerappproject.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.soccerappproject.databinding.StandingListItemBinding
import com.example.soccerappproject.model.StandingItem

class StandingAdapter(
    private val list: MutableList<StandingItem> = mutableListOf(),
) : RecyclerView.Adapter<StandingAdapter.StandingViewHolder>() {

    fun setStandingList(newList: List<StandingItem>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class StandingViewHolder(private val binding: StandingListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        // Stats are looked up by the name the API gives them, not by their position in the
        // list, so a reordered or extended stats list can't show the wrong number or crash.
        private fun StandingItem.stat(name: String): String {
            return stats.firstOrNull { it.name == name }?.value?.toInt()?.toString() ?: "-"
        }

        fun onBind(item: StandingItem) {
            binding.apply {
                tvWins.text = item.stat("wins")
                tvLoss.text = item.stat("losses")
                tvDraws.text = item.stat("ties")
                tvGamesPlayed.text = item.stat("gamesPlayed")
                tvPoints.text = item.stat("points")
                tvTeamName.text = item.team.shortDisplayName

                Glide.with(ivTeam)
                    .load(item.team.logos.firstOrNull()?.href)
                    .into(ivTeam)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StandingViewHolder {
        return StandingViewHolder(
            StandingListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StandingViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
