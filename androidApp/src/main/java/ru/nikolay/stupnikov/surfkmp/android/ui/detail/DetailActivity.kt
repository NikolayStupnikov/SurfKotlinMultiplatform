package ru.nikolay.stupnikov.surfkmp.android.ui.detail

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import ru.nikolay.stupnikov.surfkmp.android.BR
import dev.icerock.moko.mvvm.MvvmEventsActivity
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.dispatcher.eventsDispatcherOnMain
import ru.nikolay.stupnikov.feature.DetailViewModel
import ru.nikolay.stupnikov.interactor.api.response.Titles

import ru.nikolay.stupnikov.surfkmp.android.R
import ru.nikolay.stupnikov.surfkmp.android.databinding.ActivityDetailBinding

class DetailActivity :
    MvvmEventsActivity<ActivityDetailBinding, DetailViewModel, DetailViewModel.EventsListener>(),
    DetailViewModel.EventsListener {

    companion object {
        const val ID_ANIME = "id_anime"
        const val TITLES = "titles"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
    }

    override val layoutId: Int
        get() = R.layout.activity_detail

    override val viewModelClass: Class<DetailViewModel>
        get() = DetailViewModel::class.java

    override val viewModelVariableId: Int
        get() = BR.viewModel

    override fun viewModelFactory(): ViewModelProvider.Factory {
        return createViewModelFactory {
            DetailViewModel(eventsDispatcherOnMain(), intent.getIntExtra(ID_ANIME, 0))
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val titles: Titles? = intent.getSerializableExtra(TITLES) as Titles?
        if (titles != null) {
            if (!titles.en.isNullOrEmpty()) {
                supportActionBar!!.title = titles.en
            } else if (!titles.enJp.isNullOrEmpty()) {
                supportActionBar!!.title = titles.enJp
            } else if (!titles.jp.isNullOrEmpty()) {
                supportActionBar!!.title = titles.jp
            }
        }
    }
}
