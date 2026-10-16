package com.sdv.guessnumber.fragments.menu

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.sdv.guessnumber.R
import com.sdv.guessnumber.databinding.FragmentMenuBinding

class MenuFragment : DialogFragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding: FragmentMenuBinding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentMenuBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())

        val title = arguments?.getString(ARG_TITLE).orEmpty()
        val rules = arguments?.getString(ARG_RULES).orEmpty()

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

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_RULES = "arg_rules"

        fun newInstance(title: String, rules: String): MenuFragment {
            return MenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_RULES, rules)
                }
            }
        }
    }
}