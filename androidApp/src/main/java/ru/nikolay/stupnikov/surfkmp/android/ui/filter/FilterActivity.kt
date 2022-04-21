package ru.nikolay.stupnikov.surfkmp.android.ui.filter

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import androidx.lifecycle.ViewModelProvider
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

@FlowPreview
class FilterActivity : MvvmActivity<ActivityFilterBinding, FilterViewModel>() {

    companion object {
        const val FILTER = "filter"
        const val FILTER_REQUEST_CODE = 1000
    }

    override val layoutId: Int
        get() = R.layout.activity_filter

    override val viewModelClass: Class<FilterViewModel>
        get() = FilterViewModel::class.java

    override val viewModelVariableId: Int
        get() = BR.viewModel

    override fun viewModelFactory(): ViewModelProvider.Factory {
        return createViewModelFactory { FilterViewModel(intent.getSerializableExtra(FILTER) as? Filter) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSeasons()
        initAgeRating()
        initSpinner()
        initToolbar()

        binding.btnSubmit.setOnClickListener {
            val intent = Intent()
            val filter = Filter(
                viewModel.selectSeasons,
                binding.etYear.text.toString(),
                if (binding.categorySpinner.selectedItemPosition == 0) null
                else (binding.categorySpinner.getItemAtPosition(binding.categorySpinner.selectedItemPosition) as CategoryApi).attributes?.slug,
                viewModel.selectAgeRating
            )
            intent.putExtra(FILTER, filter)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun initAgeRating() {
        for (ageRating in ageRatingList) {
            val checkBox = CheckBox(this)
            checkBox.text = ageRating
            if (viewModel.selectAgeRating.contains(ageRating)) {
                checkBox.isChecked = true
            }
            checkBox.setOnClickListener{
                if (checkBox.isChecked) {
                    viewModel.selectAgeRating.add(ageRating)
                } else {
                    viewModel.selectAgeRating.remove(ageRating)
                }
            }
            binding.layoutAgeRating.addView(checkBox)
        }
    }

    private fun initSeasons() {
        for (season in seasons) {
            val checkBox = CheckBox(this)
            checkBox.text = season
            if (viewModel.selectSeasons.contains(season)) {
                checkBox.isChecked = true
            }
            checkBox.setOnClickListener{
                if (checkBox.isChecked) {
                    viewModel.selectSeasons.add(season)
                } else {
                    viewModel.selectSeasons.remove(season)
                }
            }
            binding.layoutSeasons.addView(checkBox)
        }
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
