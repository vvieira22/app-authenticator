package com.vvieira.appauthenticator.recyclerviews.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vvieira.appauthenticator.R

class BannersCarouselAdapter(private val images: List<Int>) : RecyclerView.Adapter<BannersCarouselAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: ImageView) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.banners_carousel, parent, false) as ImageView
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }
}

