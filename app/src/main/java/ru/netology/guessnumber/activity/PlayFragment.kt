package ru.netology.guessnumber.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import ru.netology.guessnumber.R
import ru.netology.guessnumber.activity.EndFragment.Companion.textArg
import ru.netology.guessnumber.databinding.FragmentPlayBinding

class PlayFragment : Fragment() {

    private var fragmentBinding: FragmentPlayBinding? = null
    private var count = 0
    private var number = 0
    private var minimNumber = 0
    private var maximNumber = 100
    private var min = minimNumber
    private var max = maximNumber

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPlayBinding.inflate(inflater, container, false)
        fragmentBinding = binding
        startGame()

        binding.apply {
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_guess_options)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.newGameGuess -> {
                                startGame()
                                true
                            }

                            R.id.newIntervalGuess -> {
                                newInterval()
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

                            else -> false
                        }
                    }
                }.show()
            }
        }

        binding.buttonForNumber.setOnClickListener {
            val text = binding.editNumber.text.toString()
            val lengthMax = maximNumber.toString().length + 1
            if (!binding.editNumber.text.isNullOrEmpty() && text.length <= lengthMax) {
                val numberInput = binding.editNumber.text.toString().toInt()
                binding.editNumber.setText("")
                guessNumber(numberInput)
            } else {
                binding.editNumber.setText("")
                binding.textAnswer.text = getString(R.string.wrong_number)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }

    private fun guessNumber(numberInput: Int) {
        count++
        fragmentBinding?.textCount?.text = getString(R.string.attempt_x, count)
        var textAnswer = ""
        when (numberInput) {
            number -> {
                findNavController()
                    .navigate(R.id.action_playFragment_to_endFragment,
                        Bundle().apply {
                            textArg = count.toString()
                        })

            }

            in min + 1 until number -> {
                textAnswer = getString(R.string.number_of_comp_more, numberInput)
                min = numberInput
            }

            in minimNumber..min -> textAnswer = getString(R.string.number_of_comp_bigger_min, min)

            in number + 1 until max -> {
                textAnswer = getString(R.string.number_of_comp_smaller, numberInput)
                max = numberInput
            }

            in max..maximNumber -> textAnswer = getString(R.string.number_of_comp_smaller_max, max)

            in maximNumber + 1..maximNumber * 100 -> textAnswer =
                getString(R.string.its_too_much, numberInput)

            in 0..minimNumber -> textAnswer = getString(R.string.its_too_few, numberInput)
        }
        fragmentBinding?.textAnswer?.text = textAnswer
        fragmentBinding?.interval?.text = "$min <  ?  < $max"
    }

    private fun startGame(minInt: Int = 0, maxInt: Int = 100) {
        count = 0
        minimNumber = minInt
        maximNumber = maxInt
        min = minimNumber
        max = maximNumber
        number = (minimNumber..maximNumber).random()

        fragmentBinding?.apply {
            textAnswer.gravity = 0
            textCount.text = getString(R.string.attempt_x, count)
            textAnswer.text = getString(R.string.i_guess_number, minimNumber, maximNumber)
            interval.text = "$min  <  ?  <  $max"
        }

    }

    private fun newInterval() {
        fragmentBinding?.apply {
            groupInterval.visibility = View.VISIBLE
            groupPlay.visibility = View.GONE
            buttonInterval.setOnClickListener {
                if (isIntervalTrue()) {
                    val a = edMin.text.toString().toInt()
                    val b = edMax.text.toString().toInt()

                    if (edMin.text.toString().toInt() >= edMax.text.toString().toInt()) {
                        Toast.makeText(
                            activity,
                            getString(R.string.minmum_maximum),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        edMin.setText("")
                        edMax.setText("")
                        groupInterval.visibility = View.GONE
                        groupPlay.visibility = View.VISIBLE
                        startGame(a, b)
                    }
                }
            }
        }
    }

    private fun isIntervalTrue(): Boolean {
        var flag = true
        fragmentBinding?.apply {
            if (edMin.text.isNullOrEmpty()) {
                edMin.error = getString(R.string.field_isnt_empty)
                flag = false
            } else if (edMin.text.toString().toInt() < 0 || edMin.text.toString()
                    .toInt() > 10000
            ) {
                edMin.error = getString(R.string.value_0_10000)
                flag = false
            }

            if (edMax.text.isNullOrEmpty()) {
                edMax.error = getString(R.string.field_isnt_empty)
                flag = false
            } else if (edMax.text.toString().toInt() < 0 || edMax.text.toString()
                    .toInt() > 10000
            ) {
                edMax.error = getString(R.string.value_0_10000)
                flag = false
            }
        }

        return flag
    }

}

