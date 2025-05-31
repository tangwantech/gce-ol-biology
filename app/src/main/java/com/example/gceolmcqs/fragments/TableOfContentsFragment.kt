package com.example.gceolmcqs.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gceolmcqs.R
import com.example.gceolmcqs.adapters.SimpleRecyclerAdapter
import com.example.gceolmcqs.databinding.FragmentTableOfContentsBinding

private const val CHAPTERS = "chapters"
class TableOfContentsFragment : Fragment() {
    private lateinit var binding: FragmentTableOfContentsBinding
    private lateinit var listener: OnTableOfContentClickLister
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTableOfContentClickLister){
            listener = context
        }else{
            throw RuntimeException("$context must implement OnTableOfContentClickLister")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTableOfContentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            val chapters = it.getStringArrayList(CHAPTERS)!!
            setupRecyclerView(chapters)
        }

    }

    private fun setupRecyclerView(chapters: List<String>){
        val layoutMan = LinearLayoutManager(requireContext())
        binding.rvTableOfContent.layoutManager = layoutMan
        binding.rvTableOfContent.addItemDecoration(DividerItemDecoration(requireContext(), layoutMan.orientation))
        val adapter = SimpleRecyclerAdapter(chapters, listener)
        binding.rvTableOfContent.adapter = adapter
        binding.rvTableOfContent.setHasFixedSize(true)
    }

    interface OnTableOfContentClickLister{
        fun onTableOfContentItemClick(itemPosition: Int)
    }

    companion object {
        @JvmStatic
        fun newInstance(chapters: List<String>) =
            TableOfContentsFragment().apply {
                val temp = ArrayList<String>()
                temp.addAll(chapters)
                arguments = Bundle().apply {
                    putStringArrayList(CHAPTERS, temp)
                }
            }
    }
}