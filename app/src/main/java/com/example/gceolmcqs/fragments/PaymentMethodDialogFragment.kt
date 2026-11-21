package com.example.gceolmcqs.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.example.gceolmcqs.MCQConstants
import com.example.gceolmcqs.R

class PaymentMethodDialogFragment : DialogFragment() {
    private lateinit var onPaymentMethodDialogListeners: OnPaymentMethodDialogListeners

    private lateinit var rbMtn: RadioButton
    private lateinit var rbOrange: RadioButton
    private lateinit var rgPaymentMethod: RadioGroup

    private var positiveBtn: Button? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnPaymentMethodDialogListeners) {
            onPaymentMethodDialogListeners = context
        }

    }


    private fun initViews(): View{

        val view = requireActivity().layoutInflater.inflate(R.layout.payment_method, null)

        rgPaymentMethod = view.findViewById(R.id.rgPaymentMethod)

        rbMtn = view.findViewById(R.id.rbMtn)
        rbOrange = view.findViewById(R.id.rbOrange)

        return view
    }

    private fun setUpViewListeners(){

        rgPaymentMethod.setOnCheckedChangeListener { _, id ->
            when(id){
                R.id.rbMtn -> {

                    onPaymentMethodDialogListeners.onPaymentTypeSelected(MCQConstants.MTN_MOMO)

                }

                R.id.rbOrange -> {

                    onPaymentMethodDialogListeners.onPaymentTypeSelected(MCQConstants.ORANGE_MOMO)
                }

            }
            positiveBtn?.isEnabled = true
        }

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = initViews()
        val btnNext: Button = view.findViewById(R.id.btnNext)
        val btnCancel: Button = view.findViewById(R.id.btnCancel)

        btnNext.isEnabled = false
        positiveBtn = btnNext

        btnNext.setOnClickListener {
            onPaymentMethodDialogListeners.onPaymentMethodNextButtonClicked()
            dismiss()
        }

        btnCancel.setOnClickListener {
            onPaymentMethodDialogListeners.onPaymentMethodCancelButtonClicked()
            dismiss()
        }

        setUpViewListeners()

        return AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).apply {
            setView(view)
        }.create()
    }

    companion object {
        fun newInstance(): DialogFragment {
            val paymentMethodDialogFragment = PaymentMethodDialogFragment()
            val bundle = Bundle().apply {
            }
            paymentMethodDialogFragment.arguments = bundle
            return paymentMethodDialogFragment
        }
    }


    interface OnPaymentMethodDialogListeners {
        fun onPaymentMethodNextButtonClicked()
        fun onPaymentMethodCancelButtonClicked()
        fun onPaymentTypeSelected(momoPartner: String)
    }
}