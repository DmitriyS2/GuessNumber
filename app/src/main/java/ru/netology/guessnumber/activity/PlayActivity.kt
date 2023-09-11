package ru.netology.guessnumber.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.guessnumber.databinding.ActivityPlayBinding
import kotlin.properties.Delegates

class PlayActivity : AppCompatActivity() {

    lateinit var binding: ActivityPlayBinding
    var count by Delegates.notNull<Int>()
    var number by Delegates.notNull<Int>()
    var answer: Boolean = false
    var min = minimNumber
    var max = maximNumber

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            count = intent.getIntExtra("count", 0)
            answer = intent.getBooleanExtra("answer", false)
            number = intent.getIntExtra("number", 0)
            name = intent.getStringExtra("name")

        }


        binding.apply {
            textCount.text = "Попытка №$count"
            textAnswer.text = "$name, я загадал число от 0 до 100"
            interval.text = "$min  <  X  <  $max"
        }

        binding.buttonForNumber.setOnClickListener {
            val text = binding.editNumber.text.toString()
            val lengthMax = maximNumber.toString().length+1
            if (!binding.editNumber.text.isNullOrEmpty() && text.length<=lengthMax) {
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
        var textAnswer: String = ""
        when (numberInput) {
            number -> {
                answer = true
                isExit()
            }

            in min+1 until number -> {
                textAnswer = "Загаданное число больше $numberInput"
                min = numberInput
            }

            in minimNumber..min -> textAnswer = "Эмм...Ну я как бы уже сказал, что мое число больше $min"

            in number + 1 until max -> {
                textAnswer = "Загаданное число меньше $numberInput"
                max = numberInput
            }

            in max..maximNumber -> textAnswer = "Я уже вроде говорил, что мое число меньше $max"

            in maximNumber+1..maximNumber*100 -> textAnswer = "Это слишком много и уж точно мое число меньше $numberInput"
        }
        binding.textAnswer.text = textAnswer
        binding.interval.text = "$min  <  X  <  $max"
    }

    private fun isExit() {
        if (answer) {
            val i = Intent()
            i.putExtra("result", count)
            setResult(RESULT_OK, i)
            finish()
        }
    }
}




