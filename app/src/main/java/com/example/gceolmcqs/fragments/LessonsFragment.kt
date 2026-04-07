package com.example.gceolmcqs.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gceolmcqs.R
import com.example.gceolmcqs.adapters.LessonsRecyclerAdapter
import com.example.gceolmcqs.databinding.FragmentLessonsBinding
import com.example.gceolmcqs.viewmodels.SyllabusViewModel


private const val CLASS_INDEX = "classIndex"
private const val CHAPTER_INDEX = "chapterIndex"

class LessonsFragment : Fragment() {

    private val viewModel: SyllabusViewModel by activityViewModels()
    private lateinit var binding: FragmentLessonsBinding

    private var classIndex: Int = 0
    private var chapterIndex: Int = 0

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
        // Inflate the layout for this fragment
        binding = FragmentLessonsBinding.inflate(inflater)
//        inflater.inflate(R.layout.fragment_lessons, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLessonsRecyclerView()
        setChapterName()
    }

    private fun setLessonsRecyclerView(){
        val lessons = viewModel.getChapterLessonsAt(classIndex, chapterIndex)
        val lessonsRecyclerAdapter = LessonsRecyclerAdapter(lessons)

        val loMan = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.recyclerView.layoutManager = loMan
        binding.recyclerView.adapter = lessonsRecyclerAdapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
    }

    private fun setChapterName(){
        binding.tvChapterName.text = getString(R.string.chapter_name, viewModel.getChapterNameAt(classIndex, chapterIndex))
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