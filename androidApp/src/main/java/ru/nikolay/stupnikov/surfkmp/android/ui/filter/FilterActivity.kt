package ru.nikolay.stupnikov.surfkmp.android.ui.filter

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.icerock.moko.mvvm.MvvmActivity
import dev.icerock.moko.mvvm.createViewModelFactory
import kotlinx.coroutines.FlowPreview
import ru.nikolay.stupnikov.domain.Filter
import ru.nikolay.stupnikov.feature.FilterViewModel
import ru.nikolay.stupnikov.interactor.Setting.ageRatingList
import ru.nikolay.stupnikov.interactor.Setting.seasons
import ru.nikolay.stupnikov.interactor.api.response.CategoryApi
import ru.nikolay.stupnikov.interactor.api.response.CategoryAttribute
import ru.nikolay.stupnikov.surfkmp.android.BR
import ru.nikolay.stupnikov.surfkmp.android.R
import ru.nikolay.stupnikov.surfkmp.android.databinding.ActivityFilterBinding
import ru.nikolay.stupnikov.surfkmp.android.ui.filter.category.CategoryListAdapter
import ru.nikolay.stupnikov.surfkmp.android.ui.filter.category.CategoryRecyclerViewAdapter

@FlowPreview
class FilterActivity : MvvmActivity<ActivityFilterBinding, FilterViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_filter

    override val viewModelClass: Class<FilterViewModel>
        get() = FilterViewModel::class.java

    override val viewModelVariableId: Int
        get() = BR.viewModel

    override fun viewModelFactory(): ViewModelProvider.Factory {
        return createViewModelFactory { FilterViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSeasons()
        initSpinner()
        initAgeRating()
        initToolbar()

        binding.btnSubmit.setOnClickListener {
            val filter = Filter(
                (binding.rvSeasons.adapter as CategoryRecyclerViewAdapter).getSelectList(),
                binding.etYear.text.toString(),
                if (binding.categorySpinner.selectedItemPosition == 0) null
                else (binding.categorySpinner.getItemAtPosition(binding.categorySpinner.selectedItemPosition) as CategoryApi).attributes?.slug,
                (binding.rvAgeRating.adapter as CategoryRecyclerViewAdapter).getSelectList()
            )
            viewModel.saveFilter(filter)
            finish()
        }
    }

    override fun onDestroy() {
        viewModel.saveSeasons((binding.rvSeasons.adapter as CategoryRecyclerViewAdapter).getSelectList())
        viewModel.saveAgeRating((binding.rvAgeRating.adapter as CategoryRecyclerViewAdapter).getSelectList())
        super.onDestroy()
    }

    private fun initAgeRating() {
        binding.rvAgeRating.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAgeRating.adapter = CategoryRecyclerViewAdapter(
            ageRatingList
        )
    }

    private fun initSeasons() {
        binding.rvSeasons.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSeasons.adapter = CategoryRecyclerViewAdapter(
            seasons
        )
    }

    private fun initSpinner() {
        val adapter = CategoryListAdapter(this, arrayListOf(
            CategoryApi(
                CategoryAttribute(
                    getString(R.string.choose_category),
                    getString(R.string.choose_category)),
                0
            )
        ))
        binding.categorySpinner.adapter = adapter
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}
