package com.sdv.guessnumber.fragments.playScreen

import android.R.id.title
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import com.sdv.guessnumber.R
import com.sdv.guessnumber.databinding.FragmentPlayBinding
import com.sdv.guessnumber.fragments.endScreen.EndFragment.Companion.textArg
import com.sdv.guessnumber.fragments.menu.MenuFragment
import com.sdv.guessnumber.fragments.playScreen.contract.PlayEffect
import com.sdv.guessnumber.fragments.playScreen.contract.PlayEvent
import com.sdv.guessnumber.util.EMPTY
import com.sdv.guessnumber.util.resolve
import com.sdv.guessnumber.util.scale

class PlayFragment : Fragment() {

    private val viewModel: PlayViewModel by viewModels()

    private var _binding: FragmentPlayBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectState()
        collectEffect()

        binding.apply {
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_guess_options)
                    val isTimerVisible = viewModel.state.value.isTimerVisible
                    menu.findItem(R.id.timerToggleGuess).apply {
                        title = if (isTimerVisible) "Выкл таймер"
                        else "Вкл таймер"
                    }
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.newGameGuess -> {
                                viewModel.onEvent(PlayEvent.OnClickNewGame)
                                true
                            }

                            R.id.newIntervalGuess -> {
                                viewModel.onEvent(PlayEvent.OnClickNewInterval)
                                true
                            }

                            R.id.rulesGuess -> {
                                val menuDialog = MenuFragment(
                                    getString(R.string.guess_number),
                                    getString(R.string.rules_guess)
                                )
                                val manager = childFragmentManager
                                menuDialog.show(manager, "MENU_GUESS")
                                true
                            }

                            R.id.timerToggleGuess -> {
                                viewModel.onEvent(PlayEvent.OnClickTimerEnabled)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }

        binding.buttonForNumber.setOnClickListener {
            val text = binding.editNumber.text.toString()
            binding.editNumber.setText(EMPTY)
            viewModel.onEvent(PlayEvent.OnClickButtonEnterNumber(text))
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun collectState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.textAnswer?.let { binding.textAnswer.text = resolve(it) }
                    binding.apply {
                        textCount.text = getString(R.string.attempt_x, state.count)
                        timer.isVisible = state.isTimerVisible
                        timer.text = state.timeText
                        if (minValue.text != state.min.toString()) {
                            minValue.text = state.min.toString()
                            minValue.scale()
                        }
                        if (maxValue.text != state.max.toString()) {
                            maxValue.text = state.max.toString()
                            maxValue.scale()
                        }
                        tilMin.error = resolve(state.minimNumberError)
                        tilMax.error = resolve(state.maximNumberError)
                    }
                }
            }
        }
    }

    private fun collectEffect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is PlayEffect.NavigateToEnd -> {
                            findNavController()
                                .navigate(
                                    R.id.action_playFragment_to_endFragment,
                                    Bundle().apply {
                                        textArg = effect.count.toString()
                                    })
                        }

                        is PlayEffect.ShowToast -> showToast(
                            requireActivity(),
                            resolve(effect.text)
                        )

                        PlayEffect.StartNewGame -> startGame()
                        PlayEffect.ScreenNewInterval -> newInterval()
                    }
                }
            }
        }
    }

    private fun startGame() {
        binding.groupInterval.visibility = View.GONE
        binding.groupPlay.visibility = View.VISIBLE
    }

    private fun newInterval() {
        binding.apply {
            groupInterval.visibility = View.VISIBLE
            groupPlay.visibility = View.GONE
            buttonInterval.setOnClickListener {
                val a = edMin.text.toString()
                val b = edMax.text.toString()

                viewModel.onEvent(PlayEvent.CheckPointInterval(a, b))

                edMin.setText(EMPTY)
                edMax.setText(EMPTY)
            }
        }
    }

    private fun showToast(context: Activity, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}