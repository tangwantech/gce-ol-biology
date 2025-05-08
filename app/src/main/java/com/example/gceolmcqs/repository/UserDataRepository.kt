package com.example.gceolmcqs.repository

import com.example.gceolmcqs.datamodels.AppData
import com.example.gceolmcqs.datamodels.UserData
import com.example.gceolmcqs.roomDB.UserDataDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDataRepository(private val userDataDao: UserDataDao) {
    companion object{
        private var userData: UserData? = null

    }

    fun insertUserDataToLocaldb(userData: UserData, listener: UserDataRepositoryListener){
        CoroutineScope(Dispatchers.IO).launch{
            userDataDao.insertUserData(userData)
            withContext(Dispatchers.Main){
                listener.onSuccess()
            }
        }
    }

    private fun updateUserDataInLocaldb(){
        CoroutineScope(Dispatchers.IO).launch {
            userDataDao.update(userData!!)
        }
    }

    fun loadUserDataFromLocaldb(listener: UserDataRepositoryListener){
        CoroutineScope(Dispatchers.IO).launch{
            val tempList = userDataDao.getUserData()
            withContext(Dispatchers.Main){
                if (tempList.isNotEmpty()){
                    userData = tempList[0]
                    listener.onSuccess()
                }else{
                    listener.onError("No data found")
                }
            }
        }
    }

    fun updateSubscriptionData(subscription: String){
        userData?.subscription = subscription
        updateUserDataInLocaldb()


    }




    fun getSubscriptionData(): String?{
        return if (userData != null){
            userData!!.subscription
        }else{
            null
        }
    }

    fun updateAppData(appData: String){
        userData?.appData = appData
        updateUserDataInLocaldb()

    }

    fun getAppDataString(): String?{
        return if (userData != null){
            userData!!.appData
        }else{
            null
        }
    }

    interface UserDataRepositoryListener{
        fun onSuccess()
        fun onError(error: String)
    }


}