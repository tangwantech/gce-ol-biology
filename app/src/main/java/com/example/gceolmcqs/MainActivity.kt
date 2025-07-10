package com.example.gceolmcqs

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.ViewModelProvider

import com.example.gceolmcqs.databinding.ActivityMainBinding
import com.example.gceolmcqs.datamodels.SubjectPackageData
import com.example.gceolmcqs.fragments.HomeFragment
import com.example.gceolmcqs.repository.LocalUserDataRepository
//import com.example.gceolmcqs.repository.SubscriptionDataRepository

//import com.example.gceolmcqs.repository.RemoteRepoManager
import com.example.gceolmcqs.viewmodels.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(),
    HomeFragment.SubscriptionPackageListener
{

    private lateinit var viewModel: MainActivityViewModel

    private lateinit var pref: SharedPreferences
    private lateinit var binding: ActivityMainBinding
//    private lateinit var homeRecyclerViewAdapter: HomeRecyclerViewAdapter
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = getSharedPreferences("Main", MODE_PRIVATE)
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        setupViewModel()

//        checkInternetConnectivity()
//        setupAppUsageReminderSharedPreference()
//        startReminderService()



    }


    private  fun checkInternetConnectivity(){
//        val internetAvailable = CheckInternetConnectivity().isInternetAvailable(this)
        val params = hashMapOf("" to "")
        ConnectivityTester.checkConnection(this, params, object : ConnectivityTester.OnTestConnectionListener{
            override fun onConnectionAvailable() {
                runOnUiThread{
                    checkForLatestAppVersion()
                }
            }

            override fun onConnectionUnavailable() {
                runOnUiThread {
                    val latestVersion = pref.getString(MCQConstants.VERSION_STR, null)
                    if (latestVersion != null){
                        val installedVersion = VersionChecker().getInstalledVersion(packageManager, packageName)
                        if (installedVersion != latestVersion){
                            displayUpdateAppDialog(latestVersion)
                        }
                    }
                }

            }

        })


    }

    private fun checkForLatestAppVersion(){
        val id = UtilityFunctions().getDeviceId(this)
        val installedVersion = VersionChecker().getInstalledVersion(packageManager, packageName)
        viewModel.checkForLatestVersionAvailable(id, object: VersionChecker.OnCheckVersionListener{
            override fun onResult(version: String) {
                saveVersionToSharedPref(version)
                if (installedVersion != version){
                    runOnUiThread {
                        displayUpdateAppDialog(version)
                    }

                }else{
//                    updateAppData()
//                    startUpdateToAppData()

                }
            }

            override fun onError(error: String?) {

            }

        })
    }

    private fun saveVersionToSharedPref(latestVersion: String){
        pref.edit().putString(MCQConstants.VERSION_STR, latestVersion).apply()
    }

    private fun saveAppDataUpdateStatusToSharedPref(status: Boolean){
        pref.edit().putBoolean(MCQConstants.APP_DATA_UPDATE_STATUS, status).apply()
    }


    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
    }


    private fun gotoSubjectContentTableActivity(position: Int) {
        val intent = Intent(this, SubjectContentTableActivity::class.java)

        intent.apply {
            putExtra(MCQConstants.SUBJECT_INDEX, position)
        }
        startActivity(intent)
    }


    private fun shareApp() {
//        val uri = Uri.parse(MCQConstants.APP_URL)
        val appMsg = "${resources.getString(R.string.share_message)}\nLink: ${MCQConstants.APP_URL}"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = MCQConstants.TYPE
        intent.putExtra(Intent.EXTRA_TEXT, appMsg)
        startActivity(intent)
    }

    private fun gotoAppURL() {
        val uri = Uri.parse(MCQConstants.APP_URL)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(MCQConstants.APP_URL)))
        }
    }

//    private fun startUpdateToAppData(){
//        val appDataUpdateStatus = pref.getBoolean(MCQConstants.APP_DATA_UPDATE_STATUS, false)
//        if (!appDataUpdateStatus){
//            updateAppData()
//        }
//    }



    private fun displayErrorDialog(message: String){
        if (dialog != null){
            dialog?.dismiss()
        }
        dialog = AlertDialog.Builder(this).apply {
            setMessage(message)
            setPositiveButton(getString(R.string.ok)){d, _ ->
                d.dismiss()
            }
        }.create()
        dialog?.show()
    }

    private fun  displayAppDataIsUpToDateDialog(){
        if(dialog != null){
            dialog?.dismiss()
        }
        dialog = AlertDialog.Builder(this).apply {
            setMessage("App data is up to date.")
            setPositiveButton(getString(R.string.ok)){d, _ ->
                d.dismiss()
            }
        }.create()
        dialog?.show()
    }

    private fun checkingForUpdateToAppDataDialog(){
        if(dialog != null){
            dialog?.dismiss()
        }
        dialog = AlertDialog.Builder(this).apply {
            setMessage(getString(R.string.checking_for_latest_update))
            setCancelable(false)
        }.create()
        dialog?.show()
    }

    private fun displayAppDataUpDatedDialog(){
        if(dialog != null){
            dialog?.dismiss()
        }
        dialog = AlertDialog.Builder(this).apply {
            setMessage(getString(R.string.app_data_updated_successfully))
            setPositiveButton(getString(R.string.exit)){_, _ ->
                finish()
            }
            setCancelable(false)
        }.create()
        dialog?.show()
    }



    private fun gotoAboutUs(){
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

//    private fun gotoTermsOfServiceActivity(){
//        startActivity(TermsOfServiceActivity.getIntent(this))
//    }

    private fun setTitle(){
        title = ""
    }

    override fun onResume() {
        super.onResume()
        setTitle()
        if (!viewModel.isUserDataInitialised()){
            beginSetup()
        }else{
            setupSubscriptionFragmentStatusView()
            checkInternetConnectivity()
        }

    }

    private fun beginSetup(){
        val id = UtilityFunctions().getDeviceId(this)
        viewModel.beginSetup(id, this, object : UserDataManager.UserDataManagerListener{
            override fun onSuccess() {
                runOnUiThread {
//                        viewModel.setSubjectPackageData()
                    setupSubscriptionFragmentStatusView()
                }

            }

            override fun onUserDataUnavailable() {

            }
        })

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.share -> {
//
                shareApp()
            }
            R.id.rateUs -> {
                gotoAppURL()
            }
            R.id.about -> {
                gotoAboutUs()
            }
//            R.id.updateAppData -> {
////                updateAppData()
//            }
            R.id.exit -> {
                showExitDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        showExitDialog()

    }



//    private fun updateAppData(){
//
//        checkingForUpdateToAppDataDialog()
//        viewModel.updateAppData(object: AppDataUpdater.AppDataUpdateListener{
//            override fun onAppDataUpdated() {
//                saveAppDataUpdateStatusToSharedPref(true)
////                NetworkTimeout.stopTimer()
////                checkingDialog.dismiss()
//                displayAppDataUpDatedDialog()
//            }
//
//            override fun onError() {
////                NetworkTimeout.stopTimer()
//                displayErrorDialog(getString(R.string.network_timeout))
//            }
//
//            override fun onAppDataUpToDate() {
////                NetworkTimeout.stopTimer()
////                checkingDialog.dismiss()
//                saveAppDataUpdateStatusToSharedPref(true)
//                displayAppDataIsUpToDateDialog()
//            }
//        })
//
////        NetworkTimeout.checkTimeout(MCQConstants.NETWORK_TIME_OUT_DURATION, object: NetworkTimeout.OnNetWorkTimeoutListener{
////            override fun onNetworkTimeout() {
//////                checkingDialog.dismiss()
////                displayErrorDialog(getString(R.string.network_timeout))
////            }
////        })
//    }
    override fun onPackageExpired(subjectPackageData: SubjectPackageData) {
        viewModel.updateSubscriptionData(subjectPackageData, object : LocalUserDataRepository.OnUpdateUserDataListener{

            override fun onUpdateSuccessful() {

            }
        })
    }



    override fun onPaper1ButtonClicked(position: Int, isPackageActive: Boolean?, packageName: String?) {
        setIndexOfCurrentSubject(position)
        if(packageName == MCQConstants.NA){
//            Toast.makeText(this, "Please activate your Trial Package", Toast.LENGTH_LONG).show()
        }else{
            isPackageActive?.let{
                gotoSubjectContentTableActivity(position)
            }

        }

    }


    override fun onSubscribeButtonClicked(position: Int, subjectName: String) {
//        setSubjectPackageDataToActivate(position, subjectPackageData)
        gotoSubscriptionActivity(position, subjectName)
    }

    private fun gotoSubscriptionActivity(subjectIndex: Int, subjectName: String){
        startActivity(SubscriptionActivity.getIntent(this, subjectIndex, subjectName))
    }

    private fun gotoPaper2Activity(subjectIndex: Int){
        startActivity(Paper2Activity.getIntent(this, 0))
    }


    private fun showExitDialog() {
        val dialogExit = AlertDialog.Builder(this)
        dialogExit.apply {
            setMessage(getString(R.string.exit_message))
            setNegativeButton(resources.getString(R.string.cancel)) { p, _ ->
                p.dismiss()
            }
            setPositiveButton(resources.getString(R.string.exit)) { _, _ ->
                this@MainActivity.finish()
            }
            setCancelable(false)
        }.create().show()
    }


    override fun onNotesButtonClicked() {
        startActivity(NotesActivity.getIntent(this))
    }

    override fun onPaper2ButtonClick() {
        gotoPaper2Activity(0)
    }

    override fun onDictionaryButtonClick() {
        startActivity(DictionaryActivity.getIntent(this))
    }

    private fun setIndexOfCurrentSubject(position: Int){
        viewModel.setIndexOfCurrentSubject(position)
    }

    private fun displayUpdateAppDialog(latestVersion: String){
        AlertDialog.Builder(this).apply {
            setMessage(getString(R.string.update_app_message))
            setPositiveButton("update"){_, _ ->
                gotoAppURL()
            }
            setNegativeButton(getString(R.string.cancel)){_, _ ->
                finish()
            }
            setCancelable(false)
        }.create().show()
    }

    private fun setupSubscriptionFragmentStatusView(){

        val fragmentContainer = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as HomeFragment
        fragmentContainer.setSubscriptionFragment(viewModel.getSubjectPackageData())

    }

}
