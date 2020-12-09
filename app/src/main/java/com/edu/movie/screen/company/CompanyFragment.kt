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
import com.edu.movie.utils.showIconLoadMore
import kotlinx.android.synthetic.main.fragment_company.*
import kotlinx.android.synthetic.main.recyclerview_gridlayout.*

class CompanyFragment : Fragment(), CompanyContact.View {

    private var company: Company? = null
    private val adapterMovies by lazy { MoviesGridAdapter() }
    private val presenter: CompanyContact.Presenter by lazy { CompanyPresenter(MovieRepository.instance) }
    private var isLoading = false
    private var page = Constant.DEFAULT_PAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            company = it.getParcelable(ARG_COMPANY)
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
    }

    override fun loadMoviesOnSuccess(movies: List<MovieItem>) {
        if (page == Constant.DEFAULT_PAGE) {
            if (movies.isNotEmpty())
                adapterMovies.registerListMovies(movies.toMutableList())
            swipeRefreshData.isRefreshing = false
        } else {
            adapterMovies.removeMoviesLastItem()
            if (movies.isNotEmpty())
                isLoading = false
            adapterMovies.addMovies(movies.toMutableList())
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
                    imageViewCompany?.setImageBitmap(it)
                }.execute(Constant.BASE_URL_IMAGE + url)
            }
            textViewCompanyName.text = name
        }
    }

    private fun initRecyclerView() {
        recyclerViewMoviesGrid.apply {
            showIconLoadMore(adapterMovies.apply {
                registerOnItemClickListener {
                    addFragment(MovieDetailsFragment.newInstance(it), R.id.container)
                }
            })
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
        company?.id?.let {
            recyclerViewMoviesGrid.post {
                adapterMovies.addMoviesNull()
                presenter.getMovies(it, ++page)
            }
        }
        if (company?.id == null) {
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

        @JvmStatic
        fun newInstance(company: Company) =
            CompanyFragment().apply {
                arguments = bundleOf(ARG_COMPANY to company)
            }
    }
}
