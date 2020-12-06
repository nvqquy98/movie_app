package com.edu.movie.screen.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.edu.movie.R
import com.edu.movie.screen.commonView.movieItem.adapter.MoviesGridAdapter
import com.edu.movie.utils.showIconLoadMore
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.recyclerview_gridlayout.*

class SearchFragment : Fragment() {

    private val adapterMovies by lazy { MoviesGridAdapter() }

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
        initRecyclerview()
    }

    private fun initRecyclerview() {
        recyclerViewMoviesGrid.apply {
            showIconLoadMore(adapterMovies)
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
