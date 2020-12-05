package com.edu.movie.screen.moviedetails

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.edu.movie.R
import com.edu.movie.data.model.Cast
import com.edu.movie.data.model.MovieDetails
import com.edu.movie.data.model.MovieItem
import com.edu.movie.data.model.VideoYoutube
import com.edu.movie.data.source.repository.MovieRepository
import com.edu.movie.screen.cast.CastFragment
import com.edu.movie.screen.commonView.movieItem.adapter.MoviesHorizontalAdapter
import com.edu.movie.screen.company.CompanyFragment
import com.edu.movie.screen.genres.details.DetailsGenresFragment
import com.edu.movie.screen.moviedetails.adapter.CastsAdapter
import com.edu.movie.screen.moviedetails.adapter.GenresAdapter
import com.edu.movie.utils.Constant
import com.edu.movie.utils.LoadImageFromUrl
import com.edu.movie.utils.addFragment
import kotlinx.android.synthetic.main.fragment_movie_details.*

class MovieDetailsFragment : Fragment(), MovieDetailsContact.View {

    private val adapterGenres by lazy { GenresAdapter() }
    private val adapterCast by lazy { CastsAdapter() }
    private val adapterRecommendations by lazy { MoviesHorizontalAdapter() }
    private var idMovieDetails: Int? = null
    private val presenterMovieDetailsContact: MovieDetailsContact.Presenter by lazy {
        MovieDetailsPresenter(MovieRepository.instance)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idMovieDetails = it.getInt(ARG_ID_MOVIE_DETAIL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButtonClick()
        initRecyclerView()
        initPresenter()
    }

    override fun onDestroy() {
        super.onDestroy()
        onStop()
    }

    override fun loadContentMovieOnSuccess(movieDetails: MovieDetails) {
        initDataMovieDetails(movieDetails)
    }

    override fun loadCastsOnSuccess(casts: List<Cast>) {
        adapterCast.registerData(casts.toMutableList())
    }

    override fun loadVideoTrailerOnSuccess(video: VideoYoutube?) {
        imageButtonPlay.setOnClickListener {
            video?.keyYoutube?.let(this@MovieDetailsFragment::openYouTube)
                ?: Toast.makeText(
                    context,
                    getString(R.string.trailer_coming_soon),
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    override fun loadMoviesRecommendations(movies: List<MovieItem>) {
        adapterRecommendations.registerData(movies.toMutableList())
    }


    override fun onError(exception: Exception?) {
        Toast.makeText(context, exception?.message, Toast.LENGTH_SHORT).show()
    }

    private fun backButtonClick() {
        fragmentManager?.apply {
            btnBack.setOnClickListener {
                if (backStackEntryCount > 0) popBackStack()
            }
            btnHome.setOnClickListener {
                popBackStack(NUMBER_0, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
    }

    private fun initRecyclerView() {
        recyclerViewGenresMovieDetails.apply {
            setHasFixedSize(false)
            adapter = adapterGenres.apply {
                registerItemGenreClickListener { idGenre, nameGenre ->
                    addFragment(
                        DetailsGenresFragment.newInstance(idGenre, nameGenre),
                        R.id.container
                    )
                }
            }
        }
        recyclerviewCastsMovie.apply {
            setHasFixedSize(true)
            adapter = adapterCast.apply {
                registerOnItemCastClickListener {
                    addFragment(CastFragment.newInstance(it), R.id.container)
                }
            }
        }
        recyclerviewRecommendations.apply {
            setHasFixedSize(true)
            adapter = adapterRecommendations.apply {
                registerOnItemClickListener {
                    addFragment(newInstance(it), R.id.container)
                }
            }
        }
    }

    private fun initPresenter() {
        presenterMovieDetailsContact.apply {
            setView(this@MovieDetailsFragment)
            idMovieDetails?.let {
                getMovieDetails(it)
                getCastsInMovieDetails(it)
                getVideoTrailer(it)
                getListMovieRecommendations(it)
            }
        }
    }

    private fun initDataMovieDetails(movieDetails: MovieDetails) {
        movieDetails.apply {
            textViewMovieTitle.text = title
            if (rate != null) {
                textViewRate.text = getString(R.string.percent, (rate * 10).toInt().toString())
            }
            textViewDescription.text = description
            imageUrl?.let { url ->
                LoadImageFromUrl {
                    imageMoviesDetails?.setImageBitmap(it)
                }.execute(Constant.BASE_URL_IMAGE + url)
            }
            val company = companies?.firstOrNull { it.logoUrl != null }
            company?.run {
                textViewCompanyName.text = name
                LoadImageFromUrl {
                    imageViewCompany?.setImageBitmap(it)
                }.execute(Constant.BASE_URL_IMAGE + logoUrl)
                textViewCountryCompany.text = productionCountry
                id?.let {
                    imageViewCompany.setOnClickListener {
                        addFragment(CompanyFragment.newInstance(company), R.id.container)
                    }
                }
            }
            if (company == null) textViewCompanyName.text =
                getText(R.string.no_company_support_for_movie)
            genres?.let(adapterGenres::registerData)
        }
    }

    private fun openYouTube(idYoutube: String) {
        try {
            context?.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(URI_YOUTUBE_APP + idYoutube)
                )
            )
        } catch (e: ActivityNotFoundException) {
            context?.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(URI_YOUTUBE_WEBSITE + idYoutube)
                )
            )
        }
    }

    companion object {
        private const val ARG_ID_MOVIE_DETAIL = "ARG_ID_MOVIE_DETAIL"
        private const val URI_YOUTUBE_APP = "vnd.youtube:"
        private const val URI_YOUTUBE_WEBSITE = "http://www.youtube.com/watch?v="
        private const val NUMBER_0 = 0

        @JvmStatic
        fun newInstance(id: Int) =
            MovieDetailsFragment().apply {
                arguments = bundleOf(ARG_ID_MOVIE_DETAIL to id)
            }
    }
}
