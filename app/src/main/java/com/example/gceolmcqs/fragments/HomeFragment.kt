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
    private var countDownTimer: SubscriptionCountDownTimer? = null

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
        setupListeners(subjectPackageData)
        binding?.subscriptionPackageCard?.subjectTitleTv?.text = subjectPackageData.subjectName
        binding?.subscriptionPackageCard?.tvPackageType?.text = getString(R.string.package_info, subjectPackageData.packageName)

        val activatedOn = subjectPackageData.activatedOn
        val expiresOn = subjectPackageData.expiresOn

        if (!activatedOn.isNullOrBlank() && !expiresOn.isNullOrBlank() && activatedOn != MCQConstants.NA && expiresOn != MCQConstants.NA) {
            val isActive = ActivationExpiryDatesGenerator().checkExpiry(activatedOn, expiresOn)
            val timeLeft = ActivationExpiryDatesGenerator.getTimeRemaining(activatedOn, expiresOn)

            if (isActive && timeLeft > 0) {
                binding?.subscriptionPackageCard?.expiresInTv?.visibility = View.VISIBLE
                setupExpiryCountDown(timeLeft, subjectPackageData)
            } else {
                setPackageExpiredState(subjectPackageData)
            }
        } else {
            countDownTimer?.cancelTimer()
            countDownTimer = null
            binding?.subscriptionPackageCard?.expiresInTv?.visibility = View.GONE
            binding?.subscriptionPackageCard?.tvSubjectStatus?.text = getString(R.string.status_info, MCQConstants.NA)
            binding?.subscriptionPackageCard?.btnSubscribe?.isEnabled = false
        }
    }

    private fun setPackageExpiredState(subjectPackageData: SubjectPackageData) {
        countDownTimer?.cancelTimer()
        countDownTimer = null
        binding?.subscriptionPackageCard?.expiresInTv?.visibility = View.GONE
        binding?.subscriptionPackageCard?.tvSubjectStatus?.text = getString(R.string.status_info, getString(R.string.expired))
        binding?.subscriptionPackageCard?.tvSubjectStatus?.setTextColor(requireContext().resources.getColor(R.color.color_red))
        binding?.subscriptionPackageCard?.btnSubscribe?.isEnabled = true
        subjectPackageData.isPackageActive = false
        listener.onPackageExpired(subjectPackageData)
    }

    private fun setupExpiryCountDown(timeLeft: Long, subjectPackageData: SubjectPackageData){
        countDownTimer?.cancelTimer()
        countDownTimer = SubscriptionCountDownTimer(0).apply {
            startTimer(timeLeft, object : SubscriptionCountDownTimer.OnTimeRemainingListener{
                override fun onTimeRemaining(expiresIn: String) {
                    binding?.subscriptionPackageCard?.expiresInTv?.text = getString(R.string.expires_in, expiresIn)
                    binding?.subscriptionPackageCard?.tvSubjectStatus?.text = getString(R.string.status_info, getString(R.string.active))
                    binding?.subscriptionPackageCard?.tvSubjectStatus?.setTextColor(requireContext().resources.getColor(R.color.color_green))
                    binding?.subscriptionPackageCard?.btnSubscribe?.isEnabled = false
                }
                override fun onExpired() {
                    setPackageExpiredState(subjectPackageData)
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

        binding?.subscriptionPackageCard?.tvDictionary?.setOnClickListener {
            listener.onDictionaryButtonClick()
        }

        binding?.subscriptionPackageCard?.tvSyllabus?.setOnClickListener {
            listener.onSyllabusButtonClick()
        }
    }

    override fun onPause() {
        super.onPause()
        countDownTimer?.cancelTimer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancelTimer()
        countDownTimer = null
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
        fun onPaper1ButtonClicked(position: Int, isPackageActive: Boolean?, packageName: String?)
        fun onPaper2ButtonClick(position: Int, isPackageActive: Boolean?, packageName: String?)
        fun onDictionaryButtonClick()
        fun onSyllabusButtonClick()

    }
}
