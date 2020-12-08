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
import com.edu.movie.screen.favorite.adapter.AdapterFavorite
import com.edu.movie.screen.moviedetails.MovieDetailsFragment
import com.edu.movie.utils.addFragment
import kotlinx.android.synthetic.main.fragment_favorite.*
import java.io.File

class FavoriteFragment : Fragment(), FavoriteContact.View {

    private val presenterFavorite: FavoriteContact.Presenter by lazy {
        FavoritePresenter(
            MovieRepository.newInstance(context)
        )
    }
    private val adapterFavorite by lazy { AdapterFavorite() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initPresenter()
    }


    override fun onDestroy() {
        super.onDestroy()
        presenterFavorite.onStop()
    }

    override fun loadListFavoriteSuccess(listFavorite: List<Favorite>) {
        adapterFavorite.registerData(listFavorite.toMutableList())
    }

    override fun deleteSuccess(checkSuccess: Boolean) {
        if (checkSuccess) presenterFavorite.getListFavorite()
    }

    override fun onError(exception: Exception?) {
        Toast.makeText(context, exception?.message, Toast.LENGTH_SHORT).show()
    }

    private fun initRecyclerView() {
        recyclerViewFavorite.apply {
            setHasFixedSize(true)
            adapter = adapterFavorite.apply {
                registerOnClickDeleteItem {
                    it.imagePath?.let { imagePath ->
                        if (File(imagePath).delete())
                            presenterFavorite.deleteFavorite(it.id)
                    }
                    if (it.imagePath == null)
                        presenterFavorite.deleteFavorite(it.id)
                }
                registerOnClickImageView {
                    addFragment(MovieDetailsFragment.newInstance(it), R.id.container)
                }
            }
        }
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
}
