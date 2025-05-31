package com.example.gceolmcqs.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import androidx.fragment.app.activityViewModels

import com.example.gceolmcqs.NotesActivity
import com.example.gceolmcqs.R
import com.example.gceolmcqs.databinding.FragmentNotesBinding
import com.example.gceolmcqs.viewmodels.NotesActivityViewModel

private const val CHAPTER_INDEX = "chapterIndex"
class NotesFragment : Fragment() {
    private lateinit var binding: FragmentNotesBinding
    private lateinit var activity: NotesActivity
    private lateinit var listener: OnShowDefinitionListener
    private val viewModel: NotesActivityViewModel by activityViewModels()

    @SuppressLint("SetJavaScriptEnabled")
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
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnShowDefinitionListener){
            listener = context
        }else{
            throw RuntimeException("$context must implement OnShowDefinitionListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = requireContext().getString(R.string.lesson_notes)
        setupWebView()

    }

    private fun setupWebView(){

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true

        binding.webView.addJavascriptInterface(WebAppInterface(this), "Android")
        // To load from assets:

        val path = viewModel.getFilePath(requireArguments().getInt(CHAPTER_INDEX))
        binding.webView.loadUrl(path)

    }

    // Add this function to your WebViewActivity
    fun showDefinition(term: String){
        println("Showing definition for $term")
        listener.onShowDefinition(term)
    }

    fun gotoExercise(exerciseNumber: String){
        listener.onGotoExercise(requireArguments().getInt(CHAPTER_INDEX), exerciseNumber)
    }

    class WebAppInterface(private val fragment: NotesFragment) {
        @JavascriptInterface
        fun getDefinition(term: String) {
            fragment.showDefinition(term)
        }

        @JavascriptInterface
        fun gotoExercise(exerciseNumber: String){
            fragment.gotoExercise(exerciseNumber)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(chapterIndex: Int) =
            NotesFragment().apply {
                arguments = Bundle().apply {
                    putInt(CHAPTER_INDEX, chapterIndex)

                }
            }
    }

    interface OnShowDefinitionListener{
        fun onShowDefinition(term: String)
        fun onGotoExercise(chapterIndex: Int, exerciseNumber: String)
    }
}