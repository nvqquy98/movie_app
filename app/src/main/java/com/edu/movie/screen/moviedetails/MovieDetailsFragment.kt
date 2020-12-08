package com.edu.movie.screen.moviedetails

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
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
import com.edu.movie.data.model.*
import com.edu.movie.data.source.local.sqlite.ConstantSQL
import com.edu.movie.data.source.repository.MovieRepository
import com.edu.movie.screen.commonView.movieItem.adapter.MoviesHorizontalAdapter
import com.edu.movie.screen.genres.details.DetailsGenresFragment
import com.edu.movie.screen.moviedetails.adapter.CastsAdapter
import com.edu.movie.screen.moviedetails.adapter.GenresAdapter
import com.edu.movie.utils.Constant
import com.edu.movie.utils.LoadImageFromUrl
import com.edu.movie.utils.addFragment
import kotlinx.android.synthetic.main.fragment_movie_details.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MovieDetailsFragment : Fragment(), MovieDetailsContact.View {

    private val adapterGenres by lazy { GenresAdapter() }
    private val adapterCast by lazy { CastsAdapter() }
    private val adapterRecommendations by lazy { MoviesHorizontalAdapter() }
    private val presenterMovieDetails: MovieDetailsContact.Presenter by lazy {
        MovieDetailsPresenter(MovieRepository.newInstance(context))
    }
    private var idMovieDetails: Int? = null
    private var isFavorite = false
    private var favorite: Favorite? = null
    private var imageFavorite: Bitmap? = null

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
        initClickFavorite()
    }

    override fun loadContentMovieOnSuccess(movieDetails: MovieDetails) {
        initDataMovieDetails(movieDetails)
        movieDetails?.imagePosterUrl?.let {
            LoadImageFromUrl {
                this.imageFavorite = it
            }.execute(Constant.BASE_URL_IMAGE + movieDetails.imagePosterUrl)
        }
        idMovieDetails?.let { id ->
            favorite = movieDetails.run {
                Favorite(id, title, rate, description, null)
            }
        }
        favorite?.id?.let { id -> presenterMovieDetails.getFavorite(id) }
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

    override fun getFavorite(favorite: Favorite?) {
        isFavorite = favorite != null
        iconFavorite.setImageResource(
            if (isFavorite) R.drawable.ic_favorite_active else R.drawable.ic_heart
        )
        favorite?.let { this.favorite = it }
    }

    override fun addFavoriteSuccess(isSuccess: Boolean) {
        if (isSuccess) isFavorite = true
        iconFavorite.setImageResource(
            if (isFavorite) R.drawable.ic_favorite_active else R.drawable.ic_heart
        )
    }

    override fun unFavoriteSuccess(isSuccess: Boolean) {
        if (isSuccess) isFavorite = false
        iconFavorite.setImageResource(
            if (isFavorite) R.drawable.ic_favorite_active else R.drawable.ic_heart
        )
    }

    private fun backButtonClick() {
        fragmentManager?.apply {
            btnBack.setOnClickListener {
                if (backStackEntryCount > Constant.NUMBER_0) popBackStack()
            }
            btnHome.setOnClickListener {
                popBackStack(Constant.NUMBER_0, FragmentManager.POP_BACK_STACK_INCLUSIVE)
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
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
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
        presenterMovieDetails.apply {
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
                textViewRate.text =
                    getString(R.string.percent, (rate * Constant.NUMBER_10).toInt().toString())
            }
            textViewDescription.text = description
            imageUrl?.let { url ->
                LoadImageFromUrl {
                    imageMoviesDetails.setImageBitmap(it)
                }.execute(Constant.BASE_URL_IMAGE + url)
            }
            val company = companies?.firstOrNull { it.logoUrl != null }
            company?.run {
                textViewCompanyName.text = name
                LoadImageFromUrl {
                    imageViewCompany.setImageBitmap(it)
                }.execute(Constant.BASE_URL_IMAGE + logoUrl)
                textViewCountryCompany.text = productionCountry
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

    private fun initClickFavorite() {
        iconFavorite.setOnClickListener {
            if (isFavorite && favorite?.imagePath?.let { pathImage -> deleteImage(pathImage) } == true)
                favorite?.id?.let { id -> presenterMovieDetails.deleteFavorite(id) }
            else {
                if (imageFavorite?.let { image -> saveImage(image) } == true)
                    favorite?.imagePath?.let { presenterMovieDetails.addFavorite(favorite!!) }
            }
        }
    }

    private fun saveImage(bitmap: Bitmap): Boolean {
        var isSuccess = false
        context?.applicationContext.run {
            favorite?.id?.let {
                val contextWrapper = ContextWrapper(this)
                val directory =
                    contextWrapper.getDir(ConstantSQL.FOLDER_IMAGE, Context.MODE_PRIVATE)
                val file =
                    File(directory, ConstantSQL.BASE_IMAGE_NAME + it + ConstantSQL.FORMAT_PNG)
                if (!file.exists()) {
                    try {
                        val fos = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                        fos.flush()
                        fos.close()
                        favorite?.imagePath = file.path
                        isSuccess = true
                    } catch (e: IOException) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return isSuccess
    }

    private fun deleteImage(stringPath: String): Boolean =
        File(stringPath).delete()

    companion object {
        private const val ARG_ID_MOVIE_DETAIL = "ARG_ID_MOVIE_DETAIL"
        private const val URI_YOUTUBE_APP = "vnd.youtube:"
        private const val URI_YOUTUBE_WEBSITE = "http://www.youtube.com/watch?v="

        @JvmStatic
        fun newInstance(id: Int) =
            MovieDetailsFragment().apply {
                arguments = bundleOf(ARG_ID_MOVIE_DETAIL to id)
            }
    }
}
