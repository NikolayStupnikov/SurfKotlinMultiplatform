package ru.nikolay.stupnikov.surfkmp.android.ui.main.anime

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nikolay.stupnikov.interactor.api.response.AnimeApi
import ru.nikolay.stupnikov.interactor.api.response.Titles
import ru.nikolay.stupnikov.surfkmp.android.databinding.ViewHolderAnimeBinding
import ru.nikolay.stupnikov.surfkmp.android.ui.base.BaseViewHolder
import ru.nikolay.stupnikov.surfkmp.android.ui.detail.DetailActivity
import ru.nikolay.stupnikov.surfkmp.android.ui.detail.DetailActivity.Companion.ID_ANIME
import ru.nikolay.stupnikov.surfkmp.android.ui.detail.DetailActivity.Companion.TITLES

class AnimeRecyclerViewAdapter(private val animeList: MutableList<AnimeApi>) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun getItemCount(): Int {
        return animeList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val animeViewBinding: ViewHolderAnimeBinding = ViewHolderAnimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
       return AnimeViewHolder(animeViewBinding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(list: List<AnimeApi>) {
        animeList.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearItems() {
        animeList.clear()
        notifyDataSetChanged()
    }

    inner class AnimeViewHolder(private val binding: ViewHolderAnimeBinding) : BaseViewHolder(binding.root),
        AnimeItemViewModel.AnimeItemViewModelListener {

        override fun onBind(position: Int) {
            val anime: AnimeApi = animeList[position]
            val animeItemViewModel = AnimeItemViewModel(anime, this)
            binding.viewModel = animeItemViewModel
            binding.executePendingBindings()
        }

        override fun onItemClick(id: Int, titles: Titles?) {
            val intent = Intent(itemView.context, DetailActivity::class.java)
            intent.putExtra(ID_ANIME, id)
            intent.putExtra(TITLES, titles)
            itemView.context.startActivity(intent)
        }
    }
}