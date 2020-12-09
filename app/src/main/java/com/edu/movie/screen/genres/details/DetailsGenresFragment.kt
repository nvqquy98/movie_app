package com.edu.movie.screen.genres.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edu.movie.R
import com.edu.movie.data.model.MovieItem
import com.edu.movie.data.source.repository.MovieRepository
import com.edu.movie.screen.commonView.movieItem.adapter.MoviesGridAdapter
import com.edu.movie.screen.genres.details.adapter.DetailGenrePresenter
import com.edu.movie.screen.genres.details.adapter.ViewContactDetailsGenres
import com.edu.movie.screen.moviedetails.MovieDetailsFragment
import com.edu.movie.utils.Constant
import com.edu.movie.utils.addFragment
import com.edu.movie.utils.showIconLoadMore
import kotlinx.android.synthetic.main.fragment_details_genres.*
import kotlinx.android.synthetic.main.recyclerview_gridlayout.*

class DetailsGenresFragment : Fragment(), ViewContactDetailsGenres.View {

    private var idGenre: Int? = null
    private var name: String? = null
    private var page = Constant.DEFAULT_PAGE
    private var isLoading = false
    private val adapterDetails by lazy { MoviesGridAdapter() }
    private val presenterDetails: ViewContactDetailsGenres.Presenter by lazy {
        DetailGenrePresenter(MovieRepository.instance).apply {
            setView(
                this@DetailsGenresFragment
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idGenre = it.getInt(ARG_ID_GENRE)
            name = it.getString(ARG_NAME_GENRE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textDetailsGenre.text = name
        initRecyclerView()
        onClickBackToGenres()
        initPresenter()
        onClickItemMovieDetailGenres()
        initSwipeRefresh()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details_genres, container, false)
    }

    private fun onClickBackToGenres() {
        imageButtonDetailsBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }
    }

    override fun getDetailsGenresOnSuccess(movies: List<MovieItem>) {
        if (page == 1) {
            adapterDetails.registerListMovies(movies.toMutableList())
            swipeRefreshData.isRefreshing = false
        } else {
            adapterDetails.removeMoviesLastItem()
            adapterDetails.addMovies(movies.toMutableList())
            if (!movies.isNullOrEmpty()) {
                isLoading = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onStop()
    }

    override fun onError(exception: Exception?) {
        Toast.makeText(context, exception?.message, Toast.LENGTH_LONG).show()
    }

    private fun initRecyclerView() {
        recyclerViewMoviesGrid.apply {
            showIconLoadMore(adapterDetails)
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

    private fun initPresenter() {
        idGenre?.let { presenterDetails.getMoviesDetailsGenres(it, Constant.DEFAULT_PAGE) }
    }

    private fun onClickItemMovieDetailGenres() {
        adapterDetails.registerOnItemClickListener {
            idGenre?.let {
                addFragment(
                    MovieDetailsFragment.newInstance(it), R.id.container
                )
            }
        }
    }

    private fun initSwipeRefresh() {
        swipeRefreshData.setOnRefreshListener {
            page = Constant.DEFAULT_PAGE
            idGenre?.let { it ->
                presenterDetails.getMoviesDetailsGenres(
                    it, Constant.DEFAULT_PAGE
                )
            }
        }
    }

    private fun loadMoreData() {
        recyclerViewMoviesGrid.post {
            idGenre?.let {
                adapterDetails.addMoviesNull()
                presenterDetails.getMoviesDetailsGenres(
                    it, ++page
                )
            }
        }
    }

    companion object {
        private const val ARG_ID_GENRE = "ARG_ID_GENRE"
        private const val ARG_NAME_GENRE = "ARG_NAME_GENRE"

        @JvmStatic
        fun newInstance(idGenre: Int?, name: String?) =
            DetailsGenresFragment().apply {
                arguments = bundleOf(ARG_ID_GENRE to idGenre, ARG_NAME_GENRE to name)
            }
    }
}
