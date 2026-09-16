package com.sdv.guessnumber.fragments.playScreen.contract

import com.sdv.guessnumber.util.EMPTY
import com.sdv.guessnumber.util.UiText

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
    val isTimerVisible: Boolean = false,
    val timeText: String = EMPTY,
)