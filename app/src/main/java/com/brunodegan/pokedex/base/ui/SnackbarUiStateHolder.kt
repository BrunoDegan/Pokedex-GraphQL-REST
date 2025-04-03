package com.brunodegan.pokedex.base.ui

interface SnackbarUiStateHolder {
    data class SnackbarUi(val msg: String) : SnackbarUiStateHolder
}