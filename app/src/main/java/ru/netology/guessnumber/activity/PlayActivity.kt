package ru.netology.guessnumber.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.netology.guessnumber.R
import ru.netology.guessnumber.databinding.ActivityPlayBinding
import kotlin.properties.Delegates

class PlayActivity : AppCompatActivity() {

    lateinit var binding: ActivityPlayBinding
    var count by Delegates.notNull<Int>()
    var number by Delegates.notNull<Int>()
    var answer: Boolean = false
    var minimNumber = 0
    var maximNumber = 100
    var min = minimNumber
    var max = maximNumber

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            name = intent.getStringExtra("name")
        }

        startGame()

        binding.apply {
//            textCount.text = "Попытка №$count"
//            textAnswer.text = "$name, я загадал число от 0 до 100"
//            interval.text = "$min  <  ?  <  $max"

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_options)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.newGame -> {
                                startGame()
                                true
                            }

                            R.id.newInterval -> {
                                newInterval()
                                true
                            }

                            R.id.rules -> {
                                val menuDialog = MenuFragment()
                                val manager = supportFragmentManager
                                menuDialog.show(manager, "MY_MENU")

                                true
                            }
                            R.id.exitGAme -> {
                                exitGame()
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
                var numberInput = binding.editNumber.text.toString().toInt()
                binding.editNumber.setText("")
                guessNumber(numberInput)
            } else {
                binding.editNumber.setText("")
                binding.textAnswer.text = "Неправильно набрано число!\nПопробуй заново"
            }
        }
    }

    private fun guessNumber(numberInput: Int) {
        count++
        binding.textCount.text = "Попытка №$count"
        var textAnswer = ""
        when (numberInput) {
            number -> {
                answer = true
                isExit()
            }

            in min + 1 until number -> {
                textAnswer = "Загаданное число больше $numberInput"
                min = numberInput
            }

            in minimNumber..min -> textAnswer =
                "Эмм...Ну я как бы уже сказал, что мое число больше $min"

            in number + 1 until max -> {
                textAnswer = "Загаданное число меньше $numberInput"
                max = numberInput
            }

            in max..maximNumber -> textAnswer = "Я уже вроде говорил, что мое число меньше $max"

            in maximNumber + 1..maximNumber * 100 -> textAnswer =
                "Это слишком много и уж точно мое число меньше $numberInput"

            in 0..minimNumber -> textAnswer =
                "Слишком мало и уж точно мое число больше $numberInput"
        }
        binding.textAnswer.text = textAnswer
        binding.interval.text = "$min  <  X  <  $max"
    }

    private fun isExit() {
        if (answer) {
            val i = Intent()
            i.putExtra("result", "Поздравляю! Ты угадал мое число за $count раз")
            setResult(RESULT_OK, i)
            finish()
        }
    }

    private fun startGame(minInt: Int = 0, maxInt: Int = 100) {
        answer = false
        count = 0
        minimNumber = minInt
        maximNumber = maxInt
        min = minimNumber
        max = maximNumber
        number = (minimNumber..maximNumber).random()

        binding.apply {
            textCount.text = "Попытка №$count"
            textAnswer.text = "$name, я загадал число от $minimNumber до $maximNumber"
            interval.text = "$min  <  ?  <  $max"
        }
    }

    private fun newInterval() {
        binding.apply {
            groupGame.visibility = View.GONE
            groupInterval.visibility = View.VISIBLE
            buttonInterval.setOnClickListener {
                if (isIntervalTrue()) {
                    val a = edMin.text.toString().toInt()
                    val b = edMax.text.toString().toInt()

                    if (edMin.text.toString().toInt() >= edMax.text.toString().toInt()) {
                        Toast.makeText(
                            this@PlayActivity,
                            "Минимум - это минимальное число\nМаксимум - это максимальное число",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        edMin.setText("")
                        edMax.setText("")
                        groupInterval.visibility = View.GONE
                        groupGame.visibility = View.VISIBLE
                        startGame(a, b)
                    }
                }
            }
        }
    }

    private fun isIntervalTrue(): Boolean {
        var flag = true
        binding.apply {
            if (edMin.text.isNullOrEmpty()) {
                edMin.error = "Поле должно быть заполнено"
                flag = false
            } else if (edMin.text.toString().toInt() < 0 || edMin.text.toString().toInt() > 10000) {
                edMin.error = "Значения от 0 до 10.000"
                flag = false
            }

            if (edMax.text.isNullOrEmpty()) {
                edMax.error = "Поле должно быть заполнено"
                flag = false
            } else if (edMax.text.toString().toInt() < 0 || edMax.text.toString().toInt() > 10000) {
                edMax.error = "Значения от 0 до 10.000"
                flag = false
            }

            return flag
        }
    }

    private fun exitGame() {
            val i = Intent()
            i.putExtra("result", "В какую игру будем играть?")
            setResult(RESULT_OK, i)
            finish()
    }
}




