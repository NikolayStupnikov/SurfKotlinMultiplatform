package ru.nikolay.stupnikov.feature

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.livedata.readOnly
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import ru.nikolay.stupnikov.interactor.interactor.DefaultInteractor
import org.koin.core.component.inject
import ru.nikolay.stupnikov.domain.Filter
import ru.nikolay.stupnikov.feature.util.addAll
import ru.nikolay.stupnikov.interactor.api.response.CategoryApi
import ru.nikolay.stupnikov.interactor.api.response.CategoryAttribute

@FlowPreview
class FilterViewModel(
    val filter: Filter?
): ViewModel(), KoinComponent {

    private val interactor: DefaultInteractor by inject()

    val selectSeasons: ArrayList<String> = ArrayList()
    val selectAgeRating: ArrayList<String> = ArrayList()
    var year = ""

    private val _categoryListLiveData: MutableLiveData<MutableList<CategoryApi>> = MutableLiveData(mutableListOf())
    val categoryListLiveData: LiveData<List<CategoryApi>> = _categoryListLiveData.readOnly().map { it }

    private val _selectCategoryLiveData: MutableLiveData<String?> = MutableLiveData(null)
    var selectCategoryLiveData: LiveData<String?> = _selectCategoryLiveData.readOnly()

    init {
        filter?.let {
            selectSeasons.addAll(filter.seasons)
            selectAgeRating.addAll(filter.ageRatingList)
            year = filter.year
            _selectCategoryLiveData.value = filter.category
        }

        getCategoryList()
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