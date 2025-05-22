package com.vvieira.appauthenticator.domain.model

data class MaisVendidos(
    val imagemDrawableId: Int,
    val preco: Double,
    val categoria: String,
    val nomeProduto: String,
    val idProduto: Int
)
