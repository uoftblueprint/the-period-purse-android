package com.tpp.app.ui.component

import android.content.Context
import android.widget.Toast

fun handleError(context: Context, error: String, callback: () -> Unit) {
    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    callback()
}
