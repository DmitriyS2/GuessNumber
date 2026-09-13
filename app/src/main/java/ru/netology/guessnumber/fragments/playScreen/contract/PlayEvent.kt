package ru.netology.guessnumber.fragments.playScreen.contract

sealed interface PlayEvent {

    data class OnClickButtonEnterNumber(val text: String) : PlayEvent
    data object OnClickNewInterval : PlayEvent
    data object OnClickNewGame : PlayEvent
    data class CheckPointInterval(val minValue: String, val maxValue: String) : PlayEvent
}