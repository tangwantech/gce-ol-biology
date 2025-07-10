package com.example.gceolmcqs.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gceolmcqs.MCQConstants
import com.example.gceolmcqs.R
import com.example.gceolmcqs.adapters.ExamTypeRecyclerViewAdapter
import com.example.gceolmcqs.adapters.Paper2ExamTypeRecyclerAdapter
import com.example.gceolmcqs.databinding.FragmentExamTypeBinding
import com.example.gceolmcqs.databinding.FragmentPaper2ExamTypeBinding
import com.example.gceolmcqs.viewmodels.Paper2ActivityViewModel

private const val SUBJECT_INDEX = "subjectIndex"
private const val EXAM_TYPE_INDEX = "examTypeIndex"


class Paper2ExamTypeFragment : Fragment(), Paper2ExamTypeRecyclerAdapter.OnRecyclerItemClickListener {
//    private var subjectIndex: Int? = null
//    private var examTypeIndex: Int? = null
    private lateinit var binding: FragmentExamTypeBinding
    private lateinit var binding2: FragmentPaper2ExamTypeBinding
    private val viewModel: Paper2ActivityViewModel by viewModels()

    private lateinit var onNavigateToPaper2FragmentListener: OnNavigateToPaper2FragmentListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Within onCreate of Paper2ExamTypeFragment")
        arguments?.let {
//            subjectIndex = it.getInt(SUBJECT_INDEX)
//            examTypeIndex = it.getInt(EXAM_TYPE_INDEX)
            viewModel.updateSubjectIndex(it.getInt(SUBJECT_INDEX))
            viewModel.updateCurrentExamTypeIndex(it.getInt(EXAM_TYPE_INDEX))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnNavigateToPaper2FragmentListener){
            onNavigateToPaper2FragmentListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExamTypeBinding.inflate(LayoutInflater.from(requireContext()))
//        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_paper2_exam_type, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        println("Within onViewCreated of Paper2ExamTypeFragment")
        setupRecyclerView()

    }

    override fun onResume() {
        super.onResume()


    }

    private fun setupRecyclerView(){
        val rvLayoutMan = LinearLayoutManager(requireActivity())
        rvLayoutMan.orientation = LinearLayoutManager.VERTICAL
        binding.rvExamTypeFragment.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        binding.rvExamTypeFragment.layoutManager = rvLayoutMan
//        val subjectIndex = requireArguments().getInt(SUBJECT_INDEX)
//        val examTypeIndex = requireArguments().getInt(EXAM_TYPE_INDEX)
        val rvAdapter = Paper2ExamTypeRecyclerAdapter(
            requireContext(),
            viewModel.getPaper2ExamItemTitles(viewModel.getSubjectIndex(), viewModel.getCurrentExamTypeIndex()),
            this
        )
        binding.rvExamTypeFragment.adapter = rvAdapter
        binding.rvExamTypeFragment.setHasFixedSize(true)
    }

    companion object {

        @JvmStatic
        fun newInstance(subjectIndex: Int, examTypeIndex: Int) =
            Paper2ExamTypeFragment().apply {
                arguments = Bundle().apply {
                    putInt(SUBJECT_INDEX, subjectIndex)
                    putInt(EXAM_TYPE_INDEX, examTypeIndex)

                }
            }
    }

    override fun onRecyclerItemClick(position: Int) {
        onNavigateToPaper2FragmentListener.onNavigateToPaper2Fragment(viewModel.getSubjectIndex(), viewModel.getCurrentExamTypeIndex(), position)
    }

    interface OnNavigateToPaper2FragmentListener{
        fun onNavigateToPaper2Fragment(subjectIndex: Int, examTypeIndex: Int, examItemTitleIndex: Int)
    }
}