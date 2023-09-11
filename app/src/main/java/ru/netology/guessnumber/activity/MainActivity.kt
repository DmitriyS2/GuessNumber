package ru.netology.guessnumber.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ru.netology.guessnumber.databinding.ActivityMainBinding

var name:String? = ""
var minimNumber = 0
var maximNumber = 100

class MainActivity : AppCompatActivity() {
    private var launcher:ActivityResultLauncher<Intent>? = null

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if(result.resultCode == RESULT_OK) {
                val text = result.data?.getIntExtra("result", 0)
                binding.textComp.text = "Поздравляю! Ты угадал мое число за $text раз"
            }
        }

        binding.textComp.text = "Привет! Меня зовут Макс. А тебя?"

        binding.buttonOk.setOnClickListener {
            if(!binding.textInput.text.isNullOrEmpty()) {
                name = binding.textInput.text.toString()
                binding.textComp.text = "$name, начинаем игру!"
                binding.textInput.setText("")
                binding.groupEditTextOk.visibility = View.GONE
                binding.buttonStart.visibility = View.VISIBLE
            }
        }

        binding.buttonStart.setOnClickListener{
            //binding.buttonStart.visibility = View.GONE
            Log.d("MyLog", "button START")
            val i = Intent(this, PlayActivity::class.java)
            i.putExtra("name", name)
            i.putExtra("count", 0)
            i.putExtra("answer", false)
            i.putExtra("number", (minimNumber..maximNumber).random())


            launcher?.launch(i)
        }
    }
}