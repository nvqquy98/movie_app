package com.edu.movie.screen.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.edu.movie.R
import com.edu.movie.data.model.Favorite
import com.edu.movie.data.source.repository.MovieRepository

class FavoriteFragment : Fragment(), FavoriteContact.View {

    private val presenterFavorite by lazy { FavoritePresenter(MovieRepository.newInstance(context)) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPresenter()
    }


    override fun onDestroy() {
        super.onDestroy()
        presenterFavorite.onStop()
    }

    private fun initPresenter() {
        presenterFavorite.apply {
            setView(this@FavoriteFragment)
            getListFavorite()
        }
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    override fun loadListFavoriteSuccess(listFavorite: List<Favorite>?) {
        Toast.makeText(context, listFavorite.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun deleteSuccess(checkSuccess: Boolean?) {
        Toast.makeText(context, checkSuccess.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onError(exception: Exception?) {
        Toast.makeText(context, exception?.message, Toast.LENGTH_SHORT).show()
    }
}
