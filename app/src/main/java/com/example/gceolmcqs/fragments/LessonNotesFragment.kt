package com.example.gceolmcqs.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import com.example.gceolmcqs.R
import com.example.gceolmcqs.databinding.FragmentLessonNotesBinding
import com.example.gceolmcqs.databinding.DefinitionDialogBinding
import com.example.gceolmcqs.repository.DictionaryRepository

private const val ARG_FILENAME = "filename"
private const val ARG_TITLE = "title"

class LessonNotesFragment : Fragment() {
    private var filename: String? = null
    private var title: String? = null
    private lateinit var binding: FragmentLessonNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            filename = it.getString(ARG_FILENAME)
            title = it.getString(ARG_TITLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLessonNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = title ?: "Lesson Notes"
        setupWebView()
    }

    private fun setupWebView() {
        binding.webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.allowFileAccess = true
            
            addJavascriptInterface(AndroidInterface(), "Android")
            
            filename?.let {
                val url = "file:///android_asset/notes/$it"
                loadUrl(url)
            }
        }
    }

    inner class AndroidInterface {
        @JavascriptInterface
        fun getDefinition(term: String) {
            activity?.runOnUiThread {
                showDefinitionDialog(term)
            }
        }
    }

    private fun showDefinitionDialog(term: String) {
        val definition = DictionaryRepository.getDefinition(term)
        
        val dialogBinding = DefinitionDialogBinding.inflate(layoutInflater)
        dialogBinding.tvTitle.text = term
        dialogBinding.tvDefinition.text = definition
        
        AlertDialog.Builder(requireContext()).apply {
            setView(dialogBinding.root)
            setPositiveButton(getString(R.string.ok)) { _, _ -> }
            setCancelable(false)
        }.show()
    }

    companion object {
        @JvmStatic
        fun newInstance(filename: String, title: String) =
            LessonNotesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FILENAME, filename)
                    putString(ARG_TITLE, title)
                }
            }
    }
}
