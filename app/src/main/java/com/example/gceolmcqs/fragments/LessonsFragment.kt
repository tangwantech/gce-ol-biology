package com.example.gceolmcqs.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gceolmcqs.R
import com.example.gceolmcqs.adapters.LessonsRecyclerAdapter
import com.example.gceolmcqs.databinding.FragmentLessonsBinding
import com.example.gceolmcqs.datamodels.Lesson
import com.example.gceolmcqs.viewmodels.SyllabusViewModel


private const val CLASS_INDEX = "classIndex"
private const val CHAPTER_INDEX = "chapterIndex"

class LessonsFragment : Fragment(), LessonsRecyclerAdapter.OnLessonClickListener {

    private val viewModel: SyllabusViewModel by activityViewModels()
    private lateinit var binding: FragmentLessonsBinding
    private lateinit var lessonClickListener: OnLessonInteractionListener

    private var classIndex: Int = 0
    private var chapterIndex: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLessonInteractionListener) {
            lessonClickListener = context
        } else {
            throw RuntimeException("$context must implement OnLessonInteractionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            classIndex = it.getInt(CLASS_INDEX)
            chapterIndex = it.getInt(CHAPTER_INDEX)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLessonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLessonsRecyclerView()
        updateTitle()
    }

    private fun setLessonsRecyclerView(){
        val lessons = viewModel.getChapterLessonsAt(classIndex, chapterIndex)
        val lessonsRecyclerAdapter = LessonsRecyclerAdapter(lessons, this)

        val loMan = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.recyclerView.layoutManager = loMan
        binding.recyclerView.adapter = lessonsRecyclerAdapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
    }

    private fun updateTitle(){
        val title = viewModel.getChapterNameAt(classIndex, chapterIndex)
        requireActivity().title = title
    }

    override fun onLessonClick(lesson: Lesson) {
        if (lesson.filename.isNotEmpty()) {
            lessonClickListener.onLessonSelected(lesson)
        } else {
            Toast.makeText(requireContext(), "No notes available for this lesson", Toast.LENGTH_SHORT).show()
        }
    }

    interface OnLessonInteractionListener {
        fun onLessonSelected(lesson: Lesson)
    }

    companion object {
        @JvmStatic
        fun newInstance(classIndex: Int, chapterIndex: Int) =
            LessonsFragment().apply {
                arguments = Bundle().apply {
                    putInt(CLASS_INDEX, classIndex)
                    putInt(CHAPTER_INDEX, chapterIndex)
                }
            }
    }
}
