package com.edu.movie.screen.favorite.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edu.movie.R
import com.edu.movie.data.model.Favorite
import com.edu.movie.utils.Constant
import kotlinx.android.synthetic.main.item_movie_favorite_vertical.view.*

class AdapterFavorite : RecyclerView.Adapter<ItemFavorite>() {
    private var onClickImageView: (Int) -> Unit = {}
    private var onClickDeleteItem: (Favorite) -> Unit = {}
    private val listFavorite = mutableListOf<Favorite>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemFavorite(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie_favorite_vertical, parent, false)
        )


    override fun onBindViewHolder(holder: ItemFavorite, position: Int) {
        holder.registerOnClickImageView(onClickImageView)
        holder.registerOnClickDeleteItem(onClickDeleteItem)
        holder.bindData(listFavorite[position])
    }

    override fun getItemCount() = listFavorite.size

    fun registerData(listFavorite: MutableList<Favorite>) {
        this.listFavorite.clear()
        this.listFavorite.addAll(listFavorite)
        notifyDataSetChanged()
    }

    fun registerOnClickImageView(click: (Int) -> Unit) {
        onClickImageView = click
    }

    fun registerOnClickDeleteItem(click: (Favorite) -> Unit) {
        onClickDeleteItem = click
    }
}

class ItemFavorite(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var onClickImageView: (Int) -> Unit = {}
    private var onClickDeleteItem: (Favorite) -> Unit = {}

    fun bindData(favorite: Favorite) {
        itemView.apply {
            textNameMovieFavorite.text = favorite.title
            imageButtonDeleteFavorite.setOnClickListener { onClickDeleteItem(favorite) }
            imageMoviesItemFavorite.setImageDrawable(Drawable.createFromPath(favorite.imagePath))
            textRateFavorite.text = itemView.context.getString(
                R.string.percent,
                (favorite.rate?.plus(Constant.NUMBER_10))?.toInt().toString()
            )
            textDetailDescription.text = favorite.description
            imageMoviesItemFavorite.setOnClickListener { onClickImageView(favorite.id) }
        }
    }

    fun registerOnClickImageView(click: (Int) -> Unit) {
        onClickImageView = click
    }

    fun registerOnClickDeleteItem(click: (Favorite) -> Unit) {
        onClickDeleteItem = click
    }
}