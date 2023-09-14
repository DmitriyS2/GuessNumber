package ru.netology.guessnumber.activity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.netology.guessnumber.databinding.FragmentMenuBinding

class MenuFragment : DialogFragment() {
    var _binding:FragmentMenuBinding? = null
    val binding:FragmentMenuBinding get() = _binding!!

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentMenuBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        //super.onCreate(savedInstanceState)
        binding.fragmentRules.text = "\n    Компьютер загадывает число в рамках указанного интервала.\n\nВ меню можно изменять границы интервала.\n\n"
        builder.setView(binding.root)
        return builder
            .setTitle("Правила игры GuessNumber")
            .create()
        }
    }



