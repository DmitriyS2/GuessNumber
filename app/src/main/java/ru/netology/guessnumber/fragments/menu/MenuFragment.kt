package ru.netology.guessnumber.fragments.menu

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.netology.guessnumber.R
import ru.netology.guessnumber.databinding.FragmentMenuBinding

class MenuFragment(val title: String, val rules: String) : DialogFragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding: FragmentMenuBinding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentMenuBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())

        binding.fragmentRules.text = rules
        builder.setView(binding.root)
        return builder
            .setIcon(R.drawable.info_24)
            .setTitle("Правила игры \n$title")
            .create()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}