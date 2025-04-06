package com.vvieira.appauthenticator.recyclerviews.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vvieira.appauthenticator.databinding.CategoriasItemLayoutBinding
import com.vvieira.appauthenticator.domain.model.Categoria

class CategoriasAdapter(
    private val context: Context,
    private val categoriasList: List<Categoria>):
    RecyclerView.Adapter<CategoriasAdapter.ViewHolder>() {

    class ViewHolder(
        private val categoriasItemLayoutBinding: CategoriasItemLayoutBinding)
        : RecyclerView.ViewHolder(categoriasItemLayoutBinding.root) {

        fun bind(categ: Categoria) {
            categoriasItemLayoutBinding.textoCategoria.text = categ.nome
            categoriasItemLayoutBinding.imagemCategoria.setImageResource(categ.imagemDrawableId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoriasItemLayoutBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false //se mudar isso vai quebrar, deixa ele se virar.
            )
        )
    }

    override fun getItemCount(): Int = categoriasList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categ = categoriasList[position]
        holder.bind(categ)
    }
}