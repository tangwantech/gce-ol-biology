//package com.example.gceolmcqs
//
//import com.example.gceolmcqs.repository.RestRepository
//import com.parse.ParseObject
//import com.parse.ParseQuery
//import com.parse.ParseUser
//
//class AppDataUpdater {
//    companion object{
//        fun update(onAppDataUpdateListener: AppDataUpdateListener) {
//            val parseQuery = ParseQuery.getQuery<ParseObject>(MCQConstants.OL_DATA_CLASS)
//            parseQuery.getInBackground(MCQConstants.APP_DATA_KEY){ parseObject, e ->
//                if (e == null){
//                    val appData = parseObject.getString(MCQConstants.APP_DATA)
//                    val currentData = ParseUser.getCurrentUser().getString(MCQConstants.APP_DATA)
//                    if (appData != currentData){
//                        ParseUser.getCurrentUser().put(MCQConstants.APP_DATA, appData!!)
//                        ParseUser.getCurrentUser().saveInBackground {
//                            if (it == null){
//                                onAppDataUpdateListener.onAppDataUpdated()
//                            }else{
//                                onAppDataUpdateListener.onError()
//                            }
//                        }
//                    }else{
//                        onAppDataUpdateListener.onAppDataUpToDate()
//                    }
//                }else{
//                    onAppDataUpdateListener.onError()
//                }
//            }
//        }
//    }
//
//    fun updateAppData(params: HashMap<String, String>){
//        val oldAppData = ""
//        RestRepository().query(RestRepository.GET_APP_DATA, params, object: RestRepository.OnQueryListener{
//            override fun onSuccess(result: String) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onError(error: String?) {
//                TODO("Not yet implemented")
//            }
//        })
//    }
//
//    interface AppDataUpdateListener{
//        fun onAppDataUpdated()
//        fun onError()
//        fun onAppDataUpToDate()
//    }
//
//}