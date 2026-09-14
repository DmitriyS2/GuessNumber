package com.sdv.guessnumber.fragments.playScreen.contract

import com.sdv.guessnumber.util.UiText

sealed interface PlayEffect {

    data object StartNewGame : PlayEffect
    data object ScreenNewInterval : PlayEffect
    data class NavigateToEnd(val count: Int) : PlayEffect
    data class ShowToast(val text: UiText) : PlayEffect
}