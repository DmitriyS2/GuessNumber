package ru.netology.guessnumber.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.netology.guessnumber.R
import ru.netology.guessnumber.databinding.FragmentEndBinding
import ru.netology.guessnumber.util.StringArg

class EndFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentEndBinding.inflate(inflater, container, false)
        val number = arguments?.textArg?.toInt() ?: 0

        binding.text2.text = getString(R.string.number_guess_x_times, number)

        binding.buttonShare.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.victory)
            val path = MediaStore.Images.Media.insertImage(
                activity?.contentResolver,
                bitmap,
                "Title",
                null
            )
            val uri = Uri.parse(path)
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, getString(R.string.message_victory, number))
                type = "image/*"
            }
            val shareIntent =
                Intent.createChooser(intent, getString(R.string.chooser_share_post))

            startActivity(shareIntent)
        }

        binding.buttonNewGame.setOnClickListener {
            findNavController()
                .navigate(R.id.action_endFragment_to_playFragment)
        }

        return binding.root
    }

}