package ru.nikolay.stupnikov.surfkmp.android.ui.main.anime

import androidx.databinding.ObservableField
import ru.nikolay.stupnikov.interactor.api.response.AnimeApi
import ru.nikolay.stupnikov.interactor.api.response.Titles

class AnimeItemViewModel(
    private val anime: AnimeApi,
    private val listener: AnimeItemViewModelListener
) {

    var imageUrl: ObservableField<String> = ObservableField(anime.attributes?.posterImage?.original)
    var name: ObservableField<Titles> = ObservableField(anime.attributes?.titles)

    fun onItemClick() {
        listener.onItemClick(
            anime.id,
            anime.attributes?.titles
        )
    }

    interface AnimeItemViewModelListener {
        fun onItemClick(id: Int, titles: Titles?)
    }
}