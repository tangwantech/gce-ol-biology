package com.example.gceolmcqs.repository

import android.util.Log
import com.example.gceolmcqs.MCQConstants
import com.example.gceolmcqs.SubjectPackageActivator
import com.example.gceolmcqs.datamodels.AppData
import com.example.gceolmcqs.datamodels.CampayCredentials
import com.example.gceolmcqs.datamodels.SubjectPackageData
import com.example.gceolmcqs.datamodels.UserData
import com.google.gson.Gson
import org.json.JSONObject

class RemoteDatabaseManager {
    companion object {
        private var restRepository: RestRepository = RestRepository()
        fun signupUser(params: HashMap<String, String>, listener: OnSignupListener) {
            restRepository.query(
                RestRepository.SIGN_UP,
                params,
                object : RestRepository.OnQueryListener {
                    override fun onSuccess(result: String) {

//                        var appData = JSONObject(result).getString("appData")

                        val subscriptionData = JSONObject(result).getString("subscription")

                        getAppDataFromRemoteDatabase(params, object : OnQueryAppDataListener{
                            override fun onQueryAppDataSuccessful(result: String) {
                                val appData = result
                                val userData = UserData(0, subscriptionData, appData)
                                listener.onSignupSuccessful(userData)
                            }

                            override fun onError(error: String?) {
                                listener.onError(error)
                            }

                        })


                    }

                    override fun onError(error: String?) {
                        listener.onError(error)
                    }


                })
        }

        fun updateUserSubscriptionInRemoteDatabase(
            params: HashMap<String, String>,
            listener: OnUpdateListener
        ) {
            restRepository.query(
                RestRepository.UPDATE_SUBSCRIPTION,
                params,
                object : RestRepository.OnQueryListener {
                    override fun onSuccess(result: String) {
                        listener.onUpdateSuccessful()
                    }

                    override fun onError(error: String?) {
                        listener.onError(error)
                    }

                })
        }

        fun getAppDataFromRemoteDatabase(
            params: HashMap<String, String>,
            listener: OnQueryAppDataListener
        ) {
            restRepository.query(
                RestRepository.GET_APP_DATA,
                params,
                object : RestRepository.OnQueryListener {
                    override fun onSuccess(result: String) {
                        listener.onQueryAppDataSuccessful(result)
                    }

                    override fun onError(error: String?) {

                    }

                })
        }

        fun checkAppVersionInRemoteDatabase(
            params: HashMap<String, String>,
            listener: OnQueryAppDataListener
        ) {
            restRepository.query(
                RestRepository.GET_APP_VERSION,
                params,
                object : RestRepository.OnQueryListener {
                    override fun onSuccess(result: String) {
                        val temp = JSONObject(result).getBoolean("newVersionAvailable")

                    }

                    override fun onError(error: String?) {

                    }

                })
        }

        fun queryPackageTypes(
            params: HashMap<String, String>,
            listener: OnQueryPackageTypesListener
        ) {
            restRepository.query(
                RestRepository.GET_PACKAGE_TYPES,
                params,
                object : RestRepository.OnQueryListener {
                    override fun onSuccess(result: String) {
                        listener.onSuccess(result)
                    }

                    override fun onError(error: String?) {
                        listener.onError(error)
                    }
                })
        }

        fun getSubscriptionFromRemoteDatabase(
            params: HashMap<String, String>,
            listener: OnQuerySubscriptionListener
        ) {
            restRepository.query(
                RestRepository.GET_SUBSCRIPTION,
                params,
                object : RestRepository.OnQueryListener {
                    override fun onSuccess(result: String) {
                        Log.d("Result", result)
                        listener.onQuerySubscriptionSuccessful(result)
                    }

                    override fun onError(error: String?) {
                        listener.onError(error)
                    }
                })
        }

    }
    interface OnSignupListener {
        fun onSignupSuccessful(userData: UserData)
        fun onError(error: String?)
    }

    interface OnUpdateListener {
        fun onUpdateSuccessful()
        fun onError(error: String?)
    }

    interface OnQueryPackageTypesListener {
        fun onSuccess(result: String)
        fun onError(error: String?)
    }

    interface OnQueryListener{
        fun onSuccess()
        fun onError(error: String?)
    }

    interface OnQueryAppDataListener {
        fun onQueryAppDataSuccessful(result: String)
        fun onError(error: String?)
    }

    interface OnQuerySubscriptionListener {
        fun onQuerySubscriptionSuccessful(result: String)
        fun onError(error: String?)
    }

    interface OnCheckAppVersionForUpdateListener {
        fun onCheckAppVersionSuccessful(status: Boolean)
        fun onError(error: String?)
    }

    interface OnQueryCampayCredentialsListener {
        fun onSuccess(campayCredentials: CampayCredentials)
        fun onError(error: String?)
    }

}