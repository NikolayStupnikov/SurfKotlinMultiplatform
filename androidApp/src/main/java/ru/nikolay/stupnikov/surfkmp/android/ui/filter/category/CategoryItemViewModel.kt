package ru.nikolay.stupnikov.surfkmp.android.ui.filter.category

import androidx.databinding.ObservableField

class CategoryItemViewModel(
    private val name: String,
    isChecked: Boolean,
    private val listener: CategoryItemViewModelListener
) {

    var nameView: ObservableField<String> = ObservableField(name)
    var isCheckedView: ObservableField<Boolean> = ObservableField(isChecked)

    fun onItemClick(isChecked: Boolean) {
        listener.onItemClick(name, isChecked)
    }

    interface CategoryItemViewModelListener {
        fun onItemClick(name: String, isChecked: Boolean)
    }
}