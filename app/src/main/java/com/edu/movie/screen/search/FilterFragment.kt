package com.edu.movie.screen.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.edu.movie.R
import com.edu.movie.data.model.Genres
import com.edu.movie.screen.search.adapter.FilterAdapter
import com.edu.movie.utils.Constant
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterFragment : BottomSheetDialogFragment(), ViewContact.FilterView {

    private var view: ViewContact.SearchView? = null
    private val adapterTopRate by lazy { FilterAdapter<Int>() }
    private val adapterGenres by lazy { FilterAdapter<Genres>() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater
            .inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        setOnclickFilter()
    }

    override fun onDestroy() {
        view = null
        super.onDestroy()
    }

    override fun setView(view: ViewContact.SearchView) {
        this.view = view
    }

    override fun loadGenresOnSuccess(genres: List<Genres>) {
        val listGenres = mutableListOf<Genres>()
        listGenres.add(Genres(Constant.NUMBER_0, getString(R.string.all)))
        listGenres.addAll(genres)
        adapterGenres.registerData(listGenres)
    }

    override fun onError(exception: Exception?) {
        Toast.makeText(context, exception?.message, Toast.LENGTH_SHORT).show()
    }

    private fun initSpinner() {
        spinnerGenres.adapter = adapterGenres
        spinnerTopRate.adapter = adapterTopRate.apply {
            registerData(listRateValue)
        }
    }

    private fun setOnclickFilter() {
        btnApply.setOnClickListener {
            val rateValue = spinnerTopRate.selectedItem as Int
            val genre = spinnerGenres.run {
                selectedItem?.let { it as Genres }
            }
            view?.onListenerAcceptFilter(
                (rateValue / Constant.NUMBER_10).toDouble(),
                genre
            )
        }
    }

    companion object {
        private val listRateValue: MutableList<Int> =
            (0..10).map { it * Constant.NUMBER_10 }.toMutableList()

        @JvmStatic
        fun newInstance() = FilterFragment()
    }
}
