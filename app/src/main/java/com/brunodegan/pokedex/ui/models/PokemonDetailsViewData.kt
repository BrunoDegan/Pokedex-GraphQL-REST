package com.brunodegan.pokedex.ui.models

data class PokemonDetailsViewData(
    val id: String,
    val name: String,
    val height: String,
    val weight: String,
    val imgUrl: String,
    val types: List<String>,
)