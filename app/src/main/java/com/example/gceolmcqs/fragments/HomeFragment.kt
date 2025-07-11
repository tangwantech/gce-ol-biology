package com.example.gceolmcqs.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gceolmcqs.ActivationExpiryDatesGenerator
import com.example.gceolmcqs.MCQConstants
import com.example.gceolmcqs.R
import com.example.gceolmcqs.SubscriptionCountDownTimer
import com.example.gceolmcqs.databinding.FragmentHomeBinding
import com.example.gceolmcqs.datamodels.SubjectPackageData

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private lateinit var listener: OnHomeFragmentListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnHomeFragmentListener){
            listener = context
        }
    }

    fun setSubscriptionPackage(subjectPackageData: SubjectPackageData){
        println("withing HomeFragment...... $subjectPackageData")
        setupListeners(subjectPackageData)
        binding?.subscriptionPackageCard?.subjectTitleTv?.text = subjectPackageData.subjectName
        binding?.subscriptionPackageCard?.tvPackageType?.text = subjectPackageData.packageName

        if(subjectPackageData.isPackageActive != null){
            binding?.subscriptionPackageCard?.expireInLo?.visibility = View.VISIBLE
            val timeLeft = ActivationExpiryDatesGenerator.getTimeRemaining(subjectPackageData.activatedOn!!, subjectPackageData.expiresOn!!)
            setupExpiryCountDown(timeLeft, subjectPackageData)
        }else{
            binding?.subscriptionPackageCard?.tvSubjectStatus?.text = MCQConstants.NA
            binding?.subscriptionPackageCard?.btnSubscribe?.isEnabled = false
        }

    }

    private fun setupExpiryCountDown(timeLeft: Long, subjectPackageData: SubjectPackageData){
        SubscriptionCountDownTimer(0).apply {
            startTimer(timeLeft, object : SubscriptionCountDownTimer.OnTimeRemainingListener{
                override fun onTimeRemaining(expiresIn: String) {
                    binding?.subscriptionPackageCard?.expiresInTv?.text = requireContext().resources.getString(R.string.expires_in, expiresIn)
                    binding?.subscriptionPackageCard?.tvSubjectStatus?.text = requireContext().resources.getString(R.string.active)
                    binding?.subscriptionPackageCard?.tvSubjectStatus?.setTextColor(requireContext().resources.getColor(R.color.color_green))
                    binding?.subscriptionPackageCard?.btnSubscribe?.isEnabled = false

                }
                override fun onExpired() {
                    binding?.subscriptionPackageCard?.expireInLo?.visibility = View.GONE
                    binding?.subscriptionPackageCard?.tvSubjectStatus?.text = requireContext().resources.getString(R.string.expired)
                    binding?.subscriptionPackageCard?.tvSubjectStatus?.setTextColor(requireContext().resources.getColor(R.color.color_red))
                    binding?.subscriptionPackageCard?.btnSubscribe?.isEnabled = true
                    subjectPackageData.isPackageActive = false
                    listener.onPackageExpired(subjectPackageData)
                }
            })
        }
    }

    private fun setupListeners(subjectPackageData: SubjectPackageData){
        binding?.subscriptionPackageCard?.btnSubscribe?.setOnClickListener{
            listener.onSubscribeButtonClicked(subjectPackageData.subjectIndex!!, subjectPackageData.subjectName!!)
        }

        binding?.tvPaper1?.setOnClickListener {
            listener.onPaper1ButtonClicked(subjectPackageData.subjectIndex!!, subjectPackageData.isPackageActive!!, subjectPackageData.packageName!!)
        }

        binding?.tvPaper2?.setOnClickListener {
            listener.onPaper2ButtonClick(subjectPackageData.subjectIndex!!, subjectPackageData.isPackageActive!!, subjectPackageData.packageName!!)
        }
//
//        binding?.tvNotes?.setOnClickListener {
//            listener.onNotesButtonClicked()
//        }
//        binding?.tvDictionary?.setOnClickListener {
//            listener.onDictionaryButtonClick()
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    interface OnHomeFragmentListener{
        fun onSubscribeButtonClicked(position: Int, subjectName: String)
        fun onPackageExpired(subjectPackageData: SubjectPackageData)
//        fun onNotesButtonClicked()
        fun onPaper1ButtonClicked(position: Int, isPackageActive: Boolean?, packageName: String?)
        fun onPaper2ButtonClick(position: Int, isPackageActive: Boolean?, packageName: String?)
//        fun onDictionaryButtonClick()

    }
}