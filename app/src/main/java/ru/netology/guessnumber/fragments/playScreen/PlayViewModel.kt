package ru.netology.guessnumber.fragments.playScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.netology.guessnumber.R
import ru.netology.guessnumber.fragments.playScreen.contract.PlayEffect
import ru.netology.guessnumber.fragments.playScreen.contract.PlayEvent
import ru.netology.guessnumber.fragments.playScreen.contract.PlayState
import ru.netology.guessnumber.util.UiText

class PlayViewModel : ViewModel() {

    private val _state = MutableStateFlow(PlayState())
    val state: StateFlow<PlayState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<PlayEffect>()
    val effect: SharedFlow<PlayEffect> = _effect.asSharedFlow()

    init {
        startGame()
    }

    fun onEvent(event: PlayEvent) {
        when (event) {
            is PlayEvent.OnClickButtonEnterNumber -> clickNumber(event.text)
            is PlayEvent.CheckPointInterval -> newInterval(event.minValue, event.maxValue)
            PlayEvent.OnClickNewInterval -> showScreenNewInterval()
            PlayEvent.OnClickNewGame -> startGame()
        }
    }

    private fun showScreenNewInterval() {
        viewModelScope.launch {
            _effect.emit(PlayEffect.ScreenNewInterval)
        }
    }

    private fun startGame(minInt: Int = 0, maxInt: Int = 100) {
        _state.update {
            it.copy(
                count = 0,
                minimNumber = minInt,
                maximNumber = maxInt,
                min = minInt,
                max = maxInt,
                number = (minInt..maxInt).random(),
                textAnswer = UiText.Res(R.string.i_guess_number, listOf(minInt, maxInt)),
                interval = "$minInt  <  ?  <  $maxInt"
            )
        }
        viewModelScope.launch {
            _effect.emit(PlayEffect.StartNewGame)
        }
    }

    private fun clickNumber(editNumber: String) {
        val lengthMax = state.value.maximNumber.toString().length + 1
        if (!editNumber.isEmpty() && editNumber.length <= lengthMax) {
            val numberInput = editNumber.toInt()
            guessNumber(numberInput)
        } else {
            _state.update {
                it.copy(
                    count = it.count + 1,
                    textAnswer = UiText.Res(R.string.wrong_number)
                )
            }
        }
    }

    private fun guessNumber(numberInput: Int) {
        var textAnswer: UiText? = null
        var min: Int = state.value.min
        var max: Int = state.value.max
        if (numberInput == state.value.number) {
            viewModelScope.launch {
                _effect.emit(PlayEffect.NavigateToEnd(state.value.count))
            }
            return
        }
        when (numberInput) {
            in state.value.min + 1 until state.value.number -> {
                textAnswer = UiText.Res(R.string.number_of_comp_more, listOf(numberInput))
                min = numberInput
            }

            in state.value.minimNumber..min -> textAnswer =
                UiText.Res(R.string.number_of_comp_bigger_min, listOf(min))

            in state.value.number + 1 until max -> {
                textAnswer = UiText.Res(R.string.number_of_comp_smaller, listOf(numberInput))
                max = numberInput
            }

            in max..state.value.maximNumber -> textAnswer =
                UiText.Res(R.string.number_of_comp_smaller_max, listOf(max))

            in state.value.maximNumber + 1..Int.MAX_VALUE -> textAnswer =
                UiText.Res(R.string.its_too_much, listOf(numberInput))

            in 0..state.value.minimNumber -> textAnswer =
                UiText.Res(R.string.its_too_few, listOf(numberInput))
        }
        _state.update {
            it.copy(
                count = it.count + 1,
                textAnswer = textAnswer,
                min = min,
                max = max,
                interval = "$min <  ?  < $max"
            )
        }
    }

    private fun newInterval(minValue: String, maxValue: String) {
        if (isIntervalTrue(minValue, maxValue)) {
            val minValueInt = minValue.toInt()
            val maxValueInt = maxValue.toInt()

            if (minValueInt >= maxValueInt) {
                viewModelScope.launch {
                    _effect.emit(PlayEffect.ShowToast(UiText.Res(R.string.minmum_maximum)))
                }
            } else {
                _state.update { it.copy(minimNumber = minValueInt, maximNumber = maxValueInt) }
                startGame(minValueInt, maxValueInt)
            }
        }
    }

    private fun isIntervalTrue(valueMin: String, valueMax: String): Boolean {
        var flag = true
        var textErrorMin: UiText? = null
        var textErrorMax: UiText? = null
        if (valueMin.isEmpty()) {
            textErrorMin = UiText.Res(R.string.field_isnt_empty)
            flag = false
        } else if (valueMin.toInt() !in 0..10000) {
            textErrorMin = UiText.Res(R.string.value_0_10000)
            flag = false
        }
        if (valueMax.isEmpty()) {
            textErrorMax = UiText.Res(R.string.field_isnt_empty)
            flag = false
        } else if (valueMax.toInt() !in 0..10000) {
            textErrorMax = UiText.Res(R.string.value_0_10000)
            flag = false
        }
        _state.update { it.copy(minimNumberError = textErrorMin, maximNumberError = textErrorMax) }

        return flag
    }
}