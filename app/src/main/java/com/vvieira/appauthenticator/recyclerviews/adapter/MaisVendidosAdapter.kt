package com.vvieira.appauthenticator.recyclerviews.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vvieira.appauthenticator.databinding.MaisVendidosLayoutBinding
import com.vvieira.appauthenticator.domain.model.MaisVendidos
import java.util.Locale

class MaisVendidosAdapter(
    private val context: Context,
    private val maisVendidosList: List<MaisVendidos>):
    RecyclerView.Adapter<MaisVendidosAdapter.ViewHolder>() {

    class ViewHolder(
        private val maisVendidosItemLayoutBinding: MaisVendidosLayoutBinding)
        : RecyclerView.ViewHolder(maisVendidosItemLayoutBinding.root) {

        fun bind(maisVendidos: MaisVendidos) {
            maisVendidosItemLayoutBinding.categoria.text = maisVendidos.categoria
            maisVendidosItemLayoutBinding.nomeProduto.text = maisVendidos.nomeProduto
            maisVendidosItemLayoutBinding.precoProduto.text = String.format(Locale.getDefault(), "%.2f", maisVendidos.preco)
            maisVendidosItemLayoutBinding.imagemMaisVendidos.setImageResource(maisVendidos.imagemDrawableId)
            //Nao sei como vou passar esse id e se faz sentido.
            //maisVendidosItemLayoutBinding.idProduto.text = maisVendidos.idProduto.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MaisVendidosLayoutBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false //se mudar isso vai quebrar, deixa ele se virar.
            )
        )
    }

    override fun getItemCount(): Int = maisVendidosList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val maisVendidos = maisVendidosList[position]
        holder.bind(maisVendidos)
    }
}