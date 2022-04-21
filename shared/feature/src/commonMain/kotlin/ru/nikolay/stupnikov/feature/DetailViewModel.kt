package ru.nikolay.stupnikov.feature

import com.soywiz.klogger.Logger
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcherOwner
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.readOnly
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nikolay.stupnikov.interactor.interactor.DefaultInteractor
import ru.nikolay.stupnikov.interactor.api.response.CategoryApi

class DetailViewModel(
    override val eventsDispatcher: EventsDispatcher<EventsListener>,
    id: Int
): ViewModel(), KoinComponent, EventsDispatcherOwner<DetailViewModel.EventsListener> {

    private val interactor: DefaultInteractor by inject()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading.readOnly()

    private val _description: MutableLiveData<String> = MutableLiveData("")
    val description: LiveData<String> = _description.readOnly()

    private val _rating: MutableLiveData<String> = MutableLiveData("")
    val rating: LiveData<String> = _rating.readOnly()

    private val _startDate: MutableLiveData<String> = MutableLiveData("")
    val startDate: LiveData<String> = _startDate.readOnly()

    private val _endDate: MutableLiveData<String> = MutableLiveData("")
    val endDate: LiveData<String> = _endDate.readOnly()

    private val _ageRating: MutableLiveData<String> = MutableLiveData("")
    val ageRating: LiveData<String> = _ageRating.readOnly()

    private val _episodeCount: MutableLiveData<String> = MutableLiveData("")
    val episodeCount: LiveData<String> = _episodeCount.readOnly()

    private val _episodeLength: MutableLiveData<String> = MutableLiveData("")
    val episodeLength: LiveData<String> = _episodeLength.readOnly()

    private val _categories: MutableLiveData<String> = MutableLiveData("")
    val categories: LiveData<String> = _categories.readOnly()

    private val _imageUrl: MutableLiveData<String> = MutableLiveData("")
    val imageUrl: LiveData<String> = _imageUrl.readOnly()

    init {
        getDetailsAnime(id)
    }

    private fun getDetailsAnime(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            interactor.getDetailsAnime(id).catch {
                Logger.ConsoleLogOutput.output(Logger("Hello"), Logger.Level.ERROR, it.message )
                eventsDispatcher.dispatchEvent { onBackPressed() }
            }.collect { pair ->
                if (pair.first.result != null && pair.first.result!!.attributes != null) {
                    _description.value = pair.first.result!!.attributes!!.description ?: ""
                    _rating.value = pair.first.result!!.attributes!!.ratingRank?.toString() ?: "-"
                    _startDate.value = pair.first.result!!.attributes!!.startDate ?: "-"
                    _endDate.value = pair.first.result!!.attributes!!.endDate ?: "-"
                    _ageRating.value = "${pair.first.result!!.attributes!!.ageRating}, ${pair.first.result!!.attributes!!.ageRatingGuide ?: ""}"
                    _episodeCount.value = pair.first.result!!.attributes!!.episodeCount?.toString() ?: "-"
                    _episodeLength.value = pair.first.result!!.attributes!!.episodeLength?.toString() ?: "-"
                    _categories.value = getStringCategories(pair.second.result)
                    _imageUrl.value = pair.first.result!!.attributes!!.posterImage?.original ?: ""
                } else {
                    eventsDispatcher.dispatchEvent { onBackPressed() }
                }
            }
            _isLoading.value = false
        }
    }

    private fun getStringCategories(list: List<CategoryApi>?): String {
        if (list.isNullOrEmpty()) return "-"
        val builder = StringBuilder()
        for (category in list) {
            if (category.attributes != null && !category.attributes!!.title.isNullOrEmpty()) {
                builder.append("${category.attributes!!.title}, ")
            }
        }
        if (builder.isNotEmpty()) {
            return builder.toString().substring(0, builder.toString().length - 2)
        }
        return "-"
    }

    interface EventsListener {
        fun onBackPressed()
    }
}