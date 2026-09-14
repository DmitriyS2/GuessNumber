package com.sdv.guessnumber.util

import androidx.fragment.app.Fragment

fun Fragment.resolve(text: UiText?): String = when (text) {
    is UiText.Res -> getString(text.id, *text.args.toTypedArray())
    is UiText.Raw -> text.value
    null -> ""
}