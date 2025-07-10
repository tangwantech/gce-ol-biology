package com.example.gceolmcqs.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import androidx.fragment.app.viewModels
import com.example.gceolmcqs.R
import com.example.gceolmcqs.databinding.FragmentPaper2QuestionsSolutionBinding
import com.example.gceolmcqs.fragments.NotesFragment.OnShowDefinitionListener
import com.example.gceolmcqs.viewmodels.Paper2ActivityViewModel

private const val SUBJECT_INDEX = "subjectIndex"
private const val EXAM_TYPE_INDEX = "examTypeIndex"
private const val EXAM_TYPE_ITEM_INDEX = "examTypeItemIndex"

/**
 * A simple [Fragment] subclass.
 * Use the [Paper2QuestionsSolutionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Paper2QuestionsSolutionFragment : Fragment() {
    private var subjectIndex: Int? = null
    private var examTypeIndex: Int? = null
    private var examTypeItemIndex: Int? = null
    private val viewModel: Paper2ActivityViewModel by viewModels()

    private lateinit var binding: FragmentPaper2QuestionsSolutionBinding
    private lateinit var listener: OnShowDefinitionListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subjectIndex = it.getInt(SUBJECT_INDEX)
            examTypeIndex = it.getInt(EXAM_TYPE_INDEX)
            examTypeItemIndex = it.getInt(EXAM_TYPE_ITEM_INDEX)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnShowDefinitionListener){
            listener = context
        }else{
            throw RuntimeException("$context must implement OnShowDefinitionListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPaper2QuestionsSolutionBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        setupWebView()
    }

    private fun setTitle(){
        requireActivity().title = viewModel.getPaper2ExamItemTitleAt(subjectIndex!!, examTypeIndex!!, examTypeItemIndex!!)
    }

    private fun setupWebView(){

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true

        binding.webView.addJavascriptInterface(WebAppInterface(this), "Android")
        // To load from assets:

        val path = viewModel.getPaper2FilePath(subjectIndex!!, examTypeIndex!!, examTypeItemIndex!!)
        binding.webView.loadUrl(path)

    }

    // Add this function to your WebViewActivity
    fun showDefinition(term: String){
        println("Showing definition for $term")
        listener.onShowDefinition(term)
    }

    class WebAppInterface(private val fragment: Paper2QuestionsSolutionFragment) {
        @JavascriptInterface
        fun getDefinition(term: String) {
            fragment.showDefinition(term)
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(subjectIndex: Int, examTypeIndex: Int, examTypeItemIndex: Int) =
            Paper2QuestionsSolutionFragment().apply {
                arguments = Bundle().apply {
                    putInt(SUBJECT_INDEX, subjectIndex)
                    putInt(EXAM_TYPE_INDEX, examTypeIndex)
                    putInt(EXAM_TYPE_ITEM_INDEX, examTypeItemIndex)
                }
            }
    }

    interface OnShowDefinitionListener{
        fun onShowDefinition(term: String)
    }
}