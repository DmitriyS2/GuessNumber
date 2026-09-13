package ru.netology.guessnumber.fragments.startScreen

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.netology.guessnumber.R
import ru.netology.guessnumber.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            ObjectAnimator.ofPropertyValuesHolder(
                textQuestion,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0F, 1.2F, 1F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0F, 1.2F, 1F)
            ).setDuration(1000L)
                .start()

            ObjectAnimator.ofPropertyValuesHolder(
                textGuessNumber,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0F, 1.2F, 1F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0F, 1.2F, 1F)
            )
                .setDuration(1000L)
                .start()
            buttonStartGame.alpha = 0f
            buttonStartGame.animate().alpha(1f).translationYBy(-50f).setStartDelay(1000).duration =
                1000

            buttonStartGame.setOnClickListener {
                findNavController().navigate(R.id.action_startFragment_to_playFragment)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}