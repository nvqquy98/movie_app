package com.edu.movie.screen.company

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
import com.edu.movie.data.model.Company
import com.edu.movie.data.model.MovieItem
import com.edu.movie.data.source.repository.MovieRepository
import com.edu.movie.screen.commonView.movieItem.adapter.MoviesGridAdapter
import com.edu.movie.screen.moviedetails.MovieDetailsFragment
import com.edu.movie.utils.Constant
import com.edu.movie.utils.LoadImageFromUrl
import com.edu.movie.utils.addFragment
import kotlinx.android.synthetic.main.fragment_company.*
import kotlinx.android.synthetic.main.recyclerview_gridlayout.*

class CompanyFragment : Fragment(), CompanyContact.View {

    private var company: Company? = null
    private val adapterMovies by lazy { MoviesGridAdapter() }
    private val presenter by lazy { CompanyPresenter(MovieRepository.instance) }
    private var isLoading = false
    private var page = Constant.DEFAULT_PAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            company = it.getSerializable(ARG_COMPANY) as Company
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_company, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickButton()
        initCompany()
        initRecyclerView()
        initPresenter()
        initReloadPage()
    }

    override fun loadMoviesOnSuccess(movies: List<MovieItem>) {
        if (page == Constant.DEFAULT_PAGE) {
            if (movies.isNotEmpty())
                adapterMovies.registerListMovies(movies.toMutableList())
            swipeRefreshData.isRefreshing = false
        } else {
            adapterMovies.removeMoviesLastItem()
            if (movies.isNotEmpty())
                adapterMovies.addMovies(movies.toMutableList())
            isLoading = false
        }
    }

    override fun onError(exception: Exception?) {
        Toast.makeText(context, exception?.message, Toast.LENGTH_SHORT).show()
    }

    private fun setOnClickButton() {
        btnBack.setOnClickListener {
            fragmentManager?.popBackStack()

        }
    }

    private fun initCompany() {
        company?.apply {
            logoUrl?.let { url ->
                LoadImageFromUrl {
                    imageViewCompany.setImageBitmap(it)
                }.execute(Constant.BASE_URL_IMAGE + url)
            }
            textViewCompanyName.text = name
        }
    }

    private fun initRecyclerView() {
        recyclerViewMoviesGrid.apply {
            setHasFixedSize(true)
            adapter = adapterMovies.apply {
                registerOnItemClickListener {
                    addFragment(MovieDetailsFragment.newInstance(it), R.id.container)
                }
            }
            val gridLayoutManager = (layoutManager as GridLayoutManager)
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapterMovies.getItemViewType(position)) {
                        MoviesGridAdapter.TYPE_PROGRESSBAR -> gridLayoutManager.spanCount
                        MoviesGridAdapter.TYPE_MOVIE_ITEM -> MoviesGridAdapter.TYPE_MOVIE_ITEM
                        else -> DO_SOME_THING
                    }
                }
            }
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = gridLayoutManager.itemCount
                    val lastVisibleItem = gridLayoutManager.findLastCompletelyVisibleItemPosition()
                    if (!isLoading && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD) {
                        loadMoreData()
                        isLoading = true
                    }
                }
            })
        }
    }

    private fun loadMoreData() {
        if (company?.id != null)
            recyclerViewMoviesGrid.post {
                adapterMovies.addMoviesNull()
                presenter.getMovies(company?.id!!, ++page)
            }
        else {
            isLoading = false
            adapterMovies.removeMoviesLastItem()
        }
    }

    private fun initPresenter() {
        presenter.apply {
            setView(this@CompanyFragment)
            company?.id?.let { getMovies(it, Constant.DEFAULT_PAGE) }
        }
    }

    private fun initReloadPage() {
        swipeRefreshData.setOnRefreshListener {
            page = Constant.DEFAULT_PAGE
            company?.id?.let { presenter.getMovies(it, page) }
        }
    }

    companion object {
        private const val ARG_COMPANY = "ARG_COMPANY"
        private const val DO_SOME_THING = -1
        private const val VISIBLE_THRESHOLD = 5

        @JvmStatic
        fun newInstance(company: Company) =
            CompanyFragment().apply {
                arguments = bundleOf(ARG_COMPANY to company)
            }
    }
}