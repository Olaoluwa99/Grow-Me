package com.excercise.growme.ui

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showLongSnackbar(view: View, message: String){
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
}

fun showShortSnackbar(view: View, message: String){
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
}