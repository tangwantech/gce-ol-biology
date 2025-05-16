package com.example.gceolmcqs

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.gceolmcqs.viewmodels.SplashActivityViewModel
import com.example.gceolmcqs.databinding.ActivitySplashBinding
import com.example.gceolmcqs.databinding.TermsOfUseLayoutBinding
//import com.parse.ParseException
import kotlinx.coroutines.*

class GCEFirstActivity : AppCompatActivity() {
    private val serverRetryLimit = 2
    private lateinit var viewModel: SplashActivityViewModel
    private lateinit var pref: SharedPreferences
//    private var termsOfServiceDialog: AlertDialog? = null
    private var initializingAppDialog: AlertDialog? = null
    private var dialog: AlertDialog? = null
    private lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)
//        Toast.makeText(this, "onCreate Splash Activity", Toast.LENGTH_SHORT).show()
        setupViewModel()
        checkTerms()

    }

    private fun checkTerms(){
        val termsAccepted = pref.getBoolean(MCQConstants.TERMS_ACCEPTED, false)
        if(!termsAccepted){
            hideProgressBar()
//            Toast.makeText(this, "Terms NOT accepted", Toast.LENGTH_SHORT).show()
            displayTermsOfServiceDialog()
        }else{
//            Toast.makeText(this, "Terms accepted", Toast.LENGTH_SHORT).show()
            beginSetup()

        }
    }

    private fun hideProgressBar(){
        binding.loProgressBar.visibility = View.GONE
    }

    private fun beginNetworkCheckTimeoutCount(){
        println("Network timeout check started...")
        NetworkTimeout.checkTimeout(MCQConstants.NETWORK_TIME_OUT_DURATION, object: NetworkTimeout.OnNetWorkTimeoutListener{
            override fun onNetworkTimeout() {
                displayErrorDialog()
            }
        })
    }

    private fun stopNetworkTimer(){
        NetworkTimeout.stopTimer()
    }

    private fun beginSetup(){
//        beginNetworkCheckTimeoutCount()
        val id = UtilityFunctions().getDeviceId(this)
        viewModel.beginSetup(id, this, object : UserDataManager.UserDataManagerListener{
            override fun onSuccess() {
                runOnUiThread{
//                    stopNetworkTimer()
//                    Toast.makeText(this@GCEFirstActivity,"Setup complete...Navigating to Main Activity", Toast.LENGTH_SHORT).show()
                    gotoMainActivity()
                }
            }

            override fun onUserDataUnavailable() {
                runOnUiThread{
//                    displayInternetConnectionDialog()
                    displayErrorDialog()
                }
            }

        })

    }

    fun displayErrorDialog(){
        dialog?.dismiss()
        dialog = AlertDialog.Builder(this).apply {
            setMessage(R.string.network_timeout)
            setNegativeButton("Exit"){_, _ ->
                finish()
            }

        }.create()
        dialog?.show()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[SplashActivityViewModel::class.java]
    }

    private fun displayInternetConnectionDialog(){
//        displayTermsOfServiceDialog()

    }

    private fun displayTermsOfServiceDialog(){
//        val view = LayoutInflater.from(this).inflate(R.layout.terms_of_use_layout, null)
        val dialogBinding = TermsOfUseLayoutBinding.inflate(layoutInflater, null, false)
        dialogBinding.btnTerms.setOnClickListener {
            gotoTermsOfServiceActivity()
        }

       dialogBinding.btnPrivacyPolicy.setOnClickListener {
            gotoPrivacyPolicy()
        }

        dialog?.dismiss()
        dialog = AlertDialog.Builder(this).create()
        dialog?.setTitle(resources.getString(R.string.agreement))
        dialog?.setView(dialogBinding.root)
        dialog?.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.accept)) { _, _ ->
            binding.loProgressBar.visibility = View.VISIBLE
            saveTermsOfServiceAcceptedStatus()
//            verifyDeviceIdInRemoteDatabase()
            beginSetup()
        }
        dialog?.setButton(AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.decline)) { _, _ ->
            finish()
        }
        dialog?.setCancelable(false)
        dialog?.show()
    }

    private fun gotoTermsOfServiceActivity(){
        startActivity(TermsOfServiceActivity.getIntent(this))
    }

    private fun gotoPrivacyPolicy() {
        val uri = Uri.parse(MCQConstants.PRIVACY_POLICY)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(MCQConstants.PRIVACY_POLICY)))
        }
    }

    private fun errorDialog(errorMessage: String?){
        val timeoutDialog = AlertDialog.Builder(this).apply {
            setMessage(errorMessage)
            setNegativeButton("Exit"){_, _ ->
                finish()
            }
        }.create()
        timeoutDialog.show()
    }



    private fun gotoMainActivity(){
        CoroutineScope(Dispatchers.IO).launch{
            delay(2000L)
            withContext(Dispatchers.Main){
                val intent = Intent(this@GCEFirstActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
//
            }
        }
    }


    private fun saveTermsOfServiceAcceptedStatus(){
        pref.edit().apply {
            putBoolean(MCQConstants.TERMS_ACCEPTED, true)
            apply()
        }.commit()
    }



}