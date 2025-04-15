package com.brunodegan.pokedex.ui.models

data class PokemonListViewData(
    val id: Int,
    val name: String,
    val imgUrl: String,
    val types: List<String>,
    val abilities: List<String>
)