package com.edu.movie.screen.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edu.movie.R
import com.edu.movie.data.model.Genres
import com.edu.movie.data.model.MovieItem
import com.edu.movie.screen.commonView.movieItem.adapter.MoviesGridAdapter
import com.edu.movie.utils.Constant
import com.edu.movie.utils.showIconLoadMore
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.recyclerview_gridlayout.*

class SearchFragment : Fragment(), ViewContact.SearchView {

    private val adapterMovies by lazy { MoviesGridAdapter() }
    private var isLoading = false
    private var filterFragment: FilterFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPage()
        initSearchBar()
        initFilterSearch()
        initRecyclerview()
    }

    override fun onListenerAcceptFilter(rateValue: Double, genre: Genres?) {
        filterFragment?.dismiss()
    }

    override fun loadMoviesOnSuccess(movies: List<MovieItem>) {
    }

    override fun onError(exception: Exception?) {
        Toast.makeText(context, exception?.message, Toast.LENGTH_SHORT).show()
    }

    private fun initRecyclerview() {
        recyclerViewMoviesGrid.apply {
            showIconLoadMore(adapterMovies)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val gridLayoutManager = (layoutManager as GridLayoutManager)
                    val totalItemCount = gridLayoutManager.itemCount
                    val lastVisibleItem = gridLayoutManager.findLastCompletelyVisibleItemPosition()
                    if (!isLoading && totalItemCount <= lastVisibleItem + Constant.VISIBLE_THRESHOLD) {
                        loadMoreData()
                        isLoading = true
                    }
                }
            })
        }
    }

    private fun loadMoreData() {
        recyclerViewMoviesGrid.post {
        }
    }

    private fun initFilterSearch() {
        btnFilterSearch.setOnClickListener {
            fragmentManager?.let {
                filterFragment = FilterFragment.newInstance().apply {
                    show(it, null)
                    setView(this@SearchFragment)
                }
            }
        }
    }

    private fun backPage() {
        btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }
    }

    private fun initSearchBar() {
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                btnFilterSearch.visibility = View.VISIBLE
                searchBar.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        searchBar.requestFocus()
    }

    companion object {

        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}
