package ru.nikolay.stupnikov.surfkmp.android.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.nikolay.stupnikov.surfkmp.android.BR
import dev.icerock.moko.mvvm.MvvmEventsActivity
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.dispatcher.eventsDispatcherOnMain
import kotlinx.coroutines.FlowPreview
import ru.nikolay.stupnikov.domain.Filter
import ru.nikolay.stupnikov.feature.MainViewModel
import ru.nikolay.stupnikov.surfkmp.android.R
import ru.nikolay.stupnikov.surfkmp.android.databinding.ActivityMainBinding
import ru.nikolay.stupnikov.surfkmp.android.ui.filter.FilterActivity
import ru.nikolay.stupnikov.surfkmp.android.ui.filter.FilterActivity.Companion.FILTER
import ru.nikolay.stupnikov.surfkmp.android.ui.filter.FilterActivity.Companion.FILTER_REQUEST_CODE
import ru.nikolay.stupnikov.surfkmp.android.ui.main.anime.AnimeRecyclerViewAdapter

@FlowPreview
class MainActivity :
    MvvmEventsActivity<ActivityMainBinding, MainViewModel, MainViewModel.EventsListener>(),
    MainViewModel.EventsListener, SwipeRefreshLayout.OnRefreshListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        binding.refreshLayout.setOnRefreshListener(this)

        initRecycler()
    }

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override val viewModelClass: Class<MainViewModel>
        get() = MainViewModel::class.java

    override val viewModelVariableId: Int
        get() = BR.viewModel

    override fun viewModelFactory(): ViewModelProvider.Factory {
        return createViewModelFactory { MainViewModel(eventsDispatcherOnMain()) }
    }

    override fun onRefresh() {
        viewModel.restart()
        binding.refreshLayout.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        binding.searchForm.addTextChangedListener {
            viewModel.search(it.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter -> {
                val intent = Intent(this, FilterActivity::class.java)
                if (viewModel.filter != null) {
                    intent.putExtra(FILTER, viewModel.filter)
                }
                startActivityForResult(intent, FILTER_REQUEST_CODE)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra(FILTER)) {
                viewModel.setFilter(data.getSerializableExtra(FILTER) as Filter)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initRecycler() {
        binding.rvAnimeList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAnimeList.adapter = AnimeRecyclerViewAdapter(mutableListOf())
        binding.rvAnimeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                viewModel.doOnScroll(
                    totalItemCount,
                    lastVisibleItemPosition
                )
            }
        })
    }
}
