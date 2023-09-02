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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        count = intent.getIntExtra("count", 0)
        answer = intent.getBooleanExtra("answer", false)
        number = intent.getIntExtra("number", 0)

        binding.textCount.text = "Попытка№ $count"
        binding.textAnswer.text = "$name, я загадал число от 0 до 100"

        binding.buttonForNumber.setOnClickListener {
            if (!binding.editNumber.text.isNullOrEmpty()) {
                binding.textAnswer.text = "Нажал"
                var numberInput = binding.editNumber.text.toString().toInt()
                binding.editNumber.setText("")
                guessNumber(numberInput)
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
            in 0 until number -> textAnswer = "Загаданное число больше $numberInput"
            in number + 1..100 -> textAnswer = "Загаданное число меньше $numberInput"
        }
        binding.textAnswer.text = textAnswer
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




