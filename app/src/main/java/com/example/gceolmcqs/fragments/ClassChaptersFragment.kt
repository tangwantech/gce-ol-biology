package com.example.gceolmcqs.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.gceolmcqs.R
import com.example.gceolmcqs.adapters.SyllabusChapterItemClickLister
import com.example.gceolmcqs.adapters.SyllabusChaptersRecyclerAdapter
import com.example.gceolmcqs.databinding.FragmentClassChaptersBinding
import com.example.gceolmcqs.viewmodels.SyllabusViewModel

private const val CLASS_INDEX = "classIndex"
private const val ARG_PARAM2 = "param2"

class ClassChaptersFragment : Fragment() {
    private lateinit var binding: FragmentClassChaptersBinding
    private val viewModel: SyllabusViewModel by activityViewModels()
    private var classIndex: Int = 0
    private lateinit var listener: SyllabusChapterItemClickLister

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SyllabusChapterItemClickLister){
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            classIndex = it.getInt(CLASS_INDEX)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentClassChaptersBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        val loMan = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.recyclerView.layoutManager = loMan
        val adapter = SyllabusChaptersRecyclerAdapter(viewModel.getChapterNamesForClassAt(classIndex), listener)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                loMan.orientation
            )
        )
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun updateTitle(){
        val className = viewModel.getClassNameAt(classIndex)
//        println("$className syllabus")
        requireActivity().title = "$className notes"
    }

    override fun onResume() {
        super.onResume()
        updateTitle()
    }

    companion object {

        @JvmStatic
        fun newInstance(classIndex: Int) =
            ClassChaptersFragment().apply {
                arguments = Bundle().apply {
                    putInt(CLASS_INDEX, classIndex)
                }
            }
    }
}