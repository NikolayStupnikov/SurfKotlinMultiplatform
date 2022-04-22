package ru.nikolay.stupnikov.surfkmp.android.ui.filter.category

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nikolay.stupnikov.surfkmp.android.databinding.ViewHolderCategoryBinding
import ru.nikolay.stupnikov.surfkmp.android.ui.base.BaseViewHolder

class CategoryRecyclerViewAdapter(
    private val allList: List<String>
) : RecyclerView.Adapter<BaseViewHolder>() {

    private val elements: MutableMap<String, Boolean>

    init {
        elements = HashMap()
        for (value in allList) {
            elements[value] = false
        }
    }

    fun getSelectList(): List<String> {
        val list = mutableListOf<String>()
        for ((name, isChecked) in elements) {
            if (isChecked) list.add(name)
        }
        return list
    }


    override fun getItemCount(): Int {
        return allList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(list: List<String>) {
        for (value in list) {
            elements[value] = true
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val categoryViewBinding: ViewHolderCategoryBinding = ViewHolderCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
       return CategoryViewHolder(categoryViewBinding)
    }

    inner class CategoryViewHolder(
        private val binding: ViewHolderCategoryBinding
    ) : BaseViewHolder(binding.root), CategoryItemViewModel.CategoryItemViewModelListener {

        override fun onBind(position: Int) {
            val name = allList[position]
            val categoryItemViewModel = CategoryItemViewModel(
                name = name,
                isChecked = elements[name]!!,
                this
            )
            binding.viewModel = categoryItemViewModel
            binding.executePendingBindings()
        }

        override fun onItemClick(name: String, isChecked: Boolean) {
            elements[name] = isChecked
        }
    }
}