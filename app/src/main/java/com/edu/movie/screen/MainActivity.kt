package com.edu.movie.screen

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.edu.movie.R
import com.edu.movie.data.model.MovieItem
import com.edu.movie.utils.TypeModel
import com.edu.movie.data.source.MovieRepository
import com.edu.movie.data.source.remote.OnFetchDataListener
import com.edu.movie.utils.Constant
import com.edu.movie.utils.TrendingMoviesParams

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getData()
    }

    private fun getData() {
        val movieRepository = MovieRepository.instance
        val trendingParams = TrendingMoviesParams.UP_COMING
        val typeModel = when (trendingParams) {
            is TrendingMoviesParams -> TypeModel.MOVIE_ITEM_TRENDING
            else -> TypeModel.MOVIE_ITEM_TRENDING
        }

        val stringURL = Constant.BASE_URL + trendingParams.path + Constant.BASE_API_KEY
        movieRepository.getListMovieTrending(object : OnFetchDataListener<MutableList<MovieItem>> {
            override fun onSuccess(list: MutableList<MovieItem>) {
                Toast.makeText(baseContext, list.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun loadError(e: Exception?) {
                e?.printStackTrace()
            }

        }, stringURL, typeModel)
    }
}
