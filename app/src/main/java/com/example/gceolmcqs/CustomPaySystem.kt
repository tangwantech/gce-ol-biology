package com.example.gceolmcqs

import android.util.Log
import com.example.gceolmcqs.datamodels.SubscriptionFormData
import com.example.gceolmcqs.datamodels.TransactionInfo
import com.example.gceolmcqs.repository.RestRepository
import com.google.gson.Gson
import org.json.JSONObject

class CustomPaySystem {
    private var restRepository: RestRepository = RestRepository()
    fun initiateNewTransaction(params: HashMap<String, String>, subscriptionData: SubscriptionFormData, listener: CreateTransactionListener){
        restRepository.query(
            RestRepository.CREATE_TRANSACTION,
            params,
            object : RestRepository.OnQueryListener{
                override fun onSuccess(result: String) {
//                    Log.d("CustomPaySystem", result)
//                    val jsonResult = JSONObject(result).getString("result")
                    val transactionInfo = Gson().fromJson<TransactionInfo>(result, TransactionInfo::class.java)
                    Log.d("CustomPaySystem", transactionInfo.toString())
                    listener.onTransactionCreated(transactionInfo)
                }

                override fun onError(error: String?) {
                    Log.d("CustomPaySystem", error!!)
                    listener.onCreateTransactionFailed(error)
                }
            })

    }

    fun getTransactionStatus(params: HashMap<String, String>, listener: TransactionStatusChangeListener){
        restRepository.query(RestRepository.GET_TRANSACTION_STATUS, params, object: RestRepository.OnQueryListener{
            override fun onSuccess(result: String) {
                Log.d("GetTransactionStatus", result)
                try {
                    val status = if (result.startsWith("{")) {
                        val json = JSONObject(result)
                        if (json.has("status")) json.getString("status")
                        else if (json.has("transactionStatus")) json.getString("transactionStatus")
                        else result
                    } else {
                        result
                    }

                    when (status.uppercase()) {
                        MCQConstants.SUCCESSFUL, "SUCCESS", "SUCCESSFUL" -> listener.onTransactionSuccessful()
                        MCQConstants.FAILED, "FAIL", "FAILED" -> listener.onTransactionFailed()
                        else -> listener.onTransactionPending()
                    }
                } catch (e: Exception) {
                    listener.onTransactionPending()
                }
            }

            override fun onError(error: String?) {
                Log.d("GetTransactionStatus", error ?: "Unknown error")
                listener.onTransactionFailed()
            }
        })
    }

    interface CreateTransactionListener{
        fun onTransactionCreated(result: TransactionInfo)
        fun onCreateTransactionFailed(error: String)
    }

    interface TransactionStatusChangeListener {
        fun onTransactionPending()
        fun onTransactionFailed()
        fun onTransactionSuccessful()
    }
}