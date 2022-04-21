package ru.nikolay.stupnikov.feature

import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcherOwner
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.livedata.readOnly
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import ru.nikolay.stupnikov.domain.Filter
import ru.nikolay.stupnikov.interactor.api.response.AnimeApi
import ru.nikolay.stupnikov.feature.util.addAll
import ru.nikolay.stupnikov.feature.util.clear
import ru.nikolay.stupnikov.interactor.interactor.DefaultInteractor
import org.koin.core.component.inject
import ru.nikolay.stupnikov.interactor.Setting.PAGE_LIMIT

class MainViewModel(
    override val eventsDispatcher: EventsDispatcher<EventsListener>
): ViewModel(), KoinComponent, EventsDispatcherOwner<MainViewModel.EventsListener> {

    private val interactor: DefaultInteractor by inject()

    private val _animeListLiveData: MutableLiveData<MutableList<AnimeApi>> = MutableLiveData(mutableListOf())
    val animeListLiveData: LiveData<List<AnimeApi>> = _animeListLiveData.readOnly().map { it }

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading.readOnly()

    private var job: Job? = null

    private var offset = 0
    private var maxCount = 0
    private var search: String = ""
    var filter: Filter? = null
        private set

    init {
        getAnimeList()
    }

    private fun getAnimeList() {
        job?.let {
            if (it.isActive) it.cancel()
        }
        job = viewModelScope.launch {
            _isLoading.value = true
            kotlin.runCatching {
                interactor.requestAnimeList(offset, search, filter)
            }.onSuccess {
                if (!it.result.isNullOrEmpty()) {
                    _animeListLiveData.addAll(it.result!!)
                    offset += PAGE_LIMIT
                }
                if (it.meta != null) {
                    maxCount = it.meta!!.count
                }
            }.onFailure {
                if (it !is CancellationException) {
                    eventsDispatcher.dispatchEvent { showError(showErrorMessage(it)) }
                }
            }
            _isLoading.value = false
        }
    }

    fun restart() {
        offset = 0
        _animeListLiveData.clear()
        getAnimeList()
    }

    fun search(search: String) {
        this.search = search
        restart()
    }

    fun setFilter(filter: Filter) {
        this.filter = filter
        restart()
    }

    fun doOnScroll(totalItemCount: Int, lastVisibleItemPosition: Int) {
        if (totalItemCount == lastVisibleItemPosition + 1 && offset < maxCount) {
            getAnimeList()
        }
    }

    interface EventsListener {
        fun showError(message: String)
    }

    private fun showErrorMessage(it: Throwable): String {
        return if (!it.message.isNullOrEmpty()) it.message!!
        else "Server error"
    }
}