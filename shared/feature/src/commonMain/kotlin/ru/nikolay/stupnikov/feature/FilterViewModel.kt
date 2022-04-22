package ru.nikolay.stupnikov.feature

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.livedata.readOnly
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import ru.nikolay.stupnikov.interactor.interactor.DefaultInteractor
import org.koin.core.component.inject
import ru.nikolay.stupnikov.domain.Filter
import ru.nikolay.stupnikov.feature.util.addAll
import ru.nikolay.stupnikov.feature.util.clear
import ru.nikolay.stupnikov.interactor.api.response.CategoryApi
import ru.nikolay.stupnikov.interactor.api.response.CategoryAttribute

@FlowPreview
class FilterViewModel: ViewModel(), KoinComponent {

    private val interactor: DefaultInteractor by inject()

    private val _seasonsLiveData: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
    val seasonsLiveData: LiveData<List<String>> = _seasonsLiveData.readOnly().map { it }

    private val _ageRatingLiveData: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
    val ageRatingLiveData: LiveData<MutableList<String>> = _ageRatingLiveData.readOnly().map { it }

    private val _yearLiveData: MutableLiveData<String> = MutableLiveData("")
    val yearLiveData: LiveData<String> = _yearLiveData.readOnly()

    private val _categoryListLiveData: MutableLiveData<MutableList<CategoryApi>> = MutableLiveData(mutableListOf())
    val categoryListLiveData: LiveData<List<CategoryApi>> = _categoryListLiveData.readOnly().map { it }

    private val _selectCategoryLiveData: MutableLiveData<String?> = MutableLiveData(null)
    val selectCategoryLiveData: LiveData<String?> = _selectCategoryLiveData.readOnly()

    fun saveSeasons(list: List<String>) {
        _seasonsLiveData.clear()
        _seasonsLiveData.addAll(list)
    }

    fun saveAgeRating(list: List<String>) {
        _ageRatingLiveData.clear()
        _ageRatingLiveData.addAll(list)
    }

    init {
        observeFilter()
        getCategoryList()
    }

    fun saveFilter(filter: Filter?) {
        viewModelScope.launch {
            interactor.updateFilter(filter)
        }
    }

    private fun observeFilter() {
        viewModelScope.launch {
            interactor.state.collectLatest { filter ->
                filter?.let {
                    _seasonsLiveData.value = filter.seasons.toMutableList()
                    _ageRatingLiveData.value = filter.ageRatingList.toMutableList()
                    _yearLiveData.value = filter.year
                    _selectCategoryLiveData.value = filter.category
                }
            }
        }
    }

    private fun getCategoryList() {
        viewModelScope.launch {
            interactor.getCategoryList().catch {
            }.collect{
                _categoryListLiveData.addAll(it.map { entity ->
                    CategoryApi(CategoryAttribute(entity.title, entity.slug), entity.id)
                }.sortedBy { api ->
                    api.attributes?.title
                })
            }
        }
    }
}