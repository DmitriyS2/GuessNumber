package ru.netology.guessnumber.fragments.playScreen.contract

import ru.netology.guessnumber.util.UiText

data class PlayState(
    val count: Int = 0,
    val number: Int = 0,
    val minimNumber: Int = 0,
    val minimNumberError: UiText? = null,
    val maximNumber: Int = 100,
    val maximNumberError: UiText? = null,
    val min: Int = 0,
    val max: Int = maximNumber,
    val textAnswer: UiText? = null,
    val interval: String = "",
)