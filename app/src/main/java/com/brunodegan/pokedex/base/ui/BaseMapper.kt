package com.brunodegan.pokedex.base.ui

interface BaseMapper<in IN, out OUT> {
    fun map(input: IN): OUT
}