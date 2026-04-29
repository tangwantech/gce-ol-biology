package com.example.gceolmcqs.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gceolmcqs.MCQConstants
import com.example.gceolmcqs.PaperActivity
import com.example.gceolmcqs.R
import com.example.gceolmcqs.adapters.ExamTypeRecyclerViewAdapter
import com.example.gceolmcqs.databinding.DialogExamStatsBinding
import com.example.gceolmcqs.viewmodels.ExamTypeFragmentViewModel


class ExamTypeFragment : Fragment(), ExamTypeRecyclerViewAdapter.OnRecyclerItemClickListener {
    private lateinit var examTypeFragmentViewModel: ExamTypeFragmentViewModel
    private lateinit var onPackageExpiredListener: OnPackageExpiredListener
    private lateinit var onContentAccessDeniedListener: OnContentAccessDeniedListener
    private lateinit var onGotoPaperActivityListener: OnGotoPaperActivityListener

    private lateinit var rvExamTypeFragment: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnPackageExpiredListener){
            onPackageExpiredListener = context
        }
        if(context is OnContentAccessDeniedListener){
            onContentAccessDeniedListener = context
        }
        if (context is OnGotoPaperActivityListener){
            onGotoPaperActivityListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exam_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvExamTypeFragment = view.findViewById(R.id.rvExamTypeFragment)
        examTypeFragmentViewModel = ViewModelProvider(this)[ExamTypeFragmentViewModel::class.java]
        
        setupStatsObserver()
    }
    
    private fun setupStatsObserver() {
        examTypeFragmentViewModel.performanceData.observe(viewLifecycleOwner) { pair ->
            pair?.let { (examTitle, score) ->
                showStatsDialogWithData(examTitle, score)
                examTypeFragmentViewModel.clearPerformanceData()
            }
        }
    }

    private fun setupRecyclerView(){
        val rvLayoutMan = LinearLayoutManager(requireActivity())
        rvLayoutMan.orientation = LinearLayoutManager.VERTICAL
        
        while (rvExamTypeFragment.itemDecorationCount > 0) {
            rvExamTypeFragment.removeItemDecorationAt(0)
        }
        
        rvExamTypeFragment.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        rvExamTypeFragment.layoutManager = rvLayoutMan
        val subjectIndex = requireArguments().getInt(MCQConstants.SUBJECT_INDEX)
        val examTypeIndex = requireArguments().getInt(MCQConstants.EXAM_TYPE_INDEX)
        
        val examTitles = examTypeFragmentViewModel.getExamItemTitles(subjectIndex, examTypeIndex)
        val rvAdapter = ExamTypeRecyclerViewAdapter(
            requireContext(),
            examTitles,
            this
        )
        rvExamTypeFragment.adapter = rvAdapter
        rvExamTypeFragment.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }

    private fun showStatsDialogWithData(examTitle: String, score: com.example.gceolmcqs.datamodels.ExamScoreEntity?) {
        val dialogBinding = DialogExamStatsBinding.inflate(layoutInflater)
        dialogBinding.tvDialogTitle.text = examTitle
        
        val red = ContextCompat.getColor(requireContext(), R.color.color_red)
        val green = ContextCompat.getColor(requireContext(), R.color.color_green)
        val gray = android.graphics.Color.GRAY

        if (score != null && score.attempts > 0) {
            dialogBinding.tvDialogAttempts.text = score.attempts.toString()
            
            // Highest Score & Grade
            dialogBinding.tvDialogHighScore.text = "${score.highScore}/50"
            val highColor = if (score.highScore < 25) red else green
            dialogBinding.tvDialogHighScore.setTextColor(highColor)
            dialogBinding.tvDialogHighGrade.text = score.highGrade
            dialogBinding.tvDialogHighGrade.setTextColor(highColor)

            // Lowest Score & Grade
            dialogBinding.tvDialogLowScore.text = "${score.lowScore}/50"
            val lowColor = if (score.lowScore < 25) red else green
            dialogBinding.tvDialogLowScore.setTextColor(lowColor)
            dialogBinding.tvDialogLowGrade.text = score.lowGrade
            dialogBinding.tvDialogLowGrade.setTextColor(lowColor)

            // Recent Score & Grade
            dialogBinding.tvDialogRecentScore.text = "${score.recentScore}/50"
            val recentColor = if (score.recentScore < 25) red else green
            dialogBinding.tvDialogRecentScore.setTextColor(recentColor)
            dialogBinding.tvDialogRecentGrade.text = score.recentGrade
            dialogBinding.tvDialogRecentGrade.setTextColor(recentColor)
            
        } else {
            dialogBinding.tvDialogAttempts.text = "0"
            
            dialogBinding.tvDialogHighScore.text = "NA"
            dialogBinding.tvDialogHighScore.setTextColor(gray)
            dialogBinding.tvDialogHighGrade.text = "NA"
            dialogBinding.tvDialogHighGrade.setTextColor(gray)
            
            dialogBinding.tvDialogLowScore.text = "NA"
            dialogBinding.tvDialogLowScore.setTextColor(gray)
            dialogBinding.tvDialogLowGrade.text = "NA"
            dialogBinding.tvDialogLowGrade.setTextColor(gray)
            
            dialogBinding.tvDialogRecentScore.text = "NA"
            dialogBinding.tvDialogRecentScore.setTextColor(gray)
            dialogBinding.tvDialogRecentGrade.text = "NA"
            dialogBinding.tvDialogRecentGrade.setTextColor(gray)
        }

        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .setView(dialogBinding.root)
            .create()
        
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogBinding.btnCloseDialog.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onRecyclerItemClick(position: Int) {
        gotoPaperActivity(position)
    }

    override fun onStatsClick(position: Int) {
        val subjectIndex = requireArguments().getInt(MCQConstants.SUBJECT_INDEX)
        val examTypeIndex = requireArguments().getInt(MCQConstants.EXAM_TYPE_INDEX)
        val examTitles = examTypeFragmentViewModel.getExamItemTitles(subjectIndex, examTypeIndex)
        
        if (position < examTitles.size) {
            val examTitle = examTitles[position]
            examTypeFragmentViewModel.loadPerformanceData(examTitle)
        }
    }
    
    private fun gotoPaperActivity(examYearIndex: Int){
        val subjectIndex = requireArguments().getInt(MCQConstants.SUBJECT_INDEX)
        val intent = Intent(requireContext(), PaperActivity::class.java)
        intent.apply {
            putExtra(MCQConstants.SUBJECT_INDEX, subjectIndex)
            putExtra(MCQConstants.EXPIRES_ON, requireArguments().getString(MCQConstants.EXPIRES_ON))
            putExtra(MCQConstants.SUBJECT_NAME, requireArguments().getString(MCQConstants.SUBJECT_NAME))
            putExtra(MCQConstants.EXAM_TYPE_INDEX, requireArguments().getInt(MCQConstants.EXAM_TYPE_INDEX))
            putExtra(MCQConstants.EXAM_ITEM_INDEX, examYearIndex)
        }
        onGotoPaperActivityListener.onGotoPaperActivity(intent)
    }

    interface OnPackageExpiredListener{
        fun onShowPackageExpired()
        fun onCheckIfPackageHasExpired():Boolean
    }
    interface OnContentAccessDeniedListener{
        fun onContentAccessDenied()
    }
    interface OnGotoPaperActivityListener{
        fun onGotoPaperActivity(intent: Intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            examTypeIndex: Int,
            subjectName: String,
            expiresOn: String,
            packageName: String,
            subjectIndex: Int
        ) = ExamTypeFragment().apply {
            arguments = Bundle().apply {
                putInt(MCQConstants.EXAM_TYPE_INDEX, examTypeIndex)
                putString(MCQConstants.SUBJECT_NAME, subjectName)
                putString(MCQConstants.EXPIRES_ON, expiresOn)
                putString(MCQConstants.PACKAGE_NAME, packageName)
                putInt(MCQConstants.SUBJECT_INDEX, subjectIndex)
            }
        }
    }
}
