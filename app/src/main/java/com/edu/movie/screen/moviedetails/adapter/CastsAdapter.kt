package com.edu.movie.screen.moviedetails.adapter

import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edu.movie.R
import com.edu.movie.data.model.Cast
import com.edu.movie.utils.Constant
import com.edu.movie.utils.LoadImageFromUrl
import kotlinx.android.synthetic.main.item_cast.view.*

class CastsAdapter : RecyclerView.Adapter<CastViewHolder>() {

    private var onItemCastClickListener: ((Cast) -> Unit) = {}
    private val casts = mutableListOf<Cast>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CastViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cast, parent, false)
        )

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.registerOnItemCastClickListener(onItemCastClickListener)
        holder.bindData(casts[position])
    }

    override fun getItemCount() = casts.size

    fun registerOnItemCastClickListener(onItemCastClickListener: (Cast) -> Unit) {
        this.onItemCastClickListener = onItemCastClickListener
    }

    fun registerData(casts: MutableList<Cast>) {
        this.casts.clear()
        this.casts.addAll(casts)
        notifyDataSetChanged()
    }
}

class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var onItemCastClickListener: (Cast) -> Unit = {}

    fun bindData(cast: Cast) {
        itemView.apply {
            textViewCastName.text = cast.name

            cast.imageUrl?.let { url ->
                LoadImageFromUrl {
                    imageViewCast.setImageBitmap(it)
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constant.BASE_URL_IMAGE + url)
            }
        }
        itemView.setOnClickListener {
            onItemCastClickListener(cast)
        }
    }

    fun registerOnItemCastClickListener(onItemCastClickListener: (Cast) -> Unit) {
        this.onItemCastClickListener = onItemCastClickListener
    }
}
