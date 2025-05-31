package com.example.gceolmcqs.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.datamodels.PackageFormData
import com.example.gceolmcqs.datamodels.PackagesData
import com.example.gceolmcqs.repository.RemoteDatabaseManager
import com.google.gson.Gson

class PackageDialogViewModel: ViewModel() {
    private val packages = ArrayList<PackageFormData>()
    private val _packageTypesReady = MutableLiveData<Boolean>()
    val packageTypesReady: LiveData<Boolean> = _packageTypesReady
//    private var selectedPackage: PackageData? = null

    fun setPackages(jsonData: String){

        val packagesData = Gson().fromJson(jsonData, PackagesData::class.java)
        packages.addAll(packagesData.packages)

    }

    fun getPackages(): ArrayList<PackageFormData>{
        return packages
    }

    fun updatePackageDataAt(position: Int, isChecked: Boolean){
        for (index in 0 until packages.size){
            if( index == position){
                packages[index].isChecked = isChecked
            }else{
                packages[index].isChecked = false
            }
        }
//        println(packages)
    }

    fun getSubjectPackageAtIndex(index: Int): PackageFormData{
        return packages[index]
    }

//    fun getSelectedPackage(): PackageData{
//        return packages.first { it.isChecked }
//    }

    fun clearPackages(){
        packages.clear()
    }


}