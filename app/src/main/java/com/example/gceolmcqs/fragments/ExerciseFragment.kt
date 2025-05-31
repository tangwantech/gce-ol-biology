package com.example.gceolmcqs.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.gceolmcqs.R
import com.example.gceolmcqs.databinding.FragmentExerciseBinding
import com.example.gceolmcqs.viewmodels.NotesActivityViewModel


private const val CHAPTER_INDEX = "chapterIndex"
private const val EXERCISE_NUMBER = "exerciseNumber"
class ExerciseFragment : Fragment() {
    private lateinit var binding: FragmentExerciseBinding
    private val viewModel: NotesActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(chapterIndex: Int, exerciseNumber: String) =
            ExerciseFragment().apply {
                arguments = Bundle().apply {
                    putInt(CHAPTER_INDEX, chapterIndex)
                    putString(EXERCISE_NUMBER, exerciseNumber)
                }
            }
    }
}