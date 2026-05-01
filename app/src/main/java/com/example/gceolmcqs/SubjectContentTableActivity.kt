package com.example.gceolmcqs

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.gceolmcqs.adapters.SubjectContentTableViewPagerAdapter
import com.example.gceolmcqs.datamodels.SubjectPackageData
import com.example.gceolmcqs.fragments.ExamTypeFragment
import com.google.android.material.tabs.TabLayout

class SubjectContentTableActivity : AppCompatActivity(),
    ExamTypeFragment.OnPackageExpiredListener,
    ExamTypeFragment.OnContentAccessDeniedListener,
    ExamTypeFragment.OnGotoPaperActivityListener {

    private lateinit var viewModel: SubjectContentTableViewModel
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_content_table)
        initActivityViews()
        initViewModel()
        setupActivityViewListeners()
        setupViewObservers()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Initial setup trigger
        start()
    }

    private fun setTitle() {
        title = viewModel.getSubjectName() + " " + getString(R.string.paper_1)
    }

    private fun initActivityViews() {
        tabLayout = findViewById(R.id.homeTab)
        viewPager = findViewById(R.id.homeViewPager)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[SubjectContentTableViewModel::class.java]
        // Only set if not already restored
        if (intent.hasExtra(MCQConstants.SUBJECT_INDEX)) {
            viewModel.setSubjectIndex(intent.getIntExtra(MCQConstants.SUBJECT_INDEX, 0))
        }
    }

    private fun setupViewObservers() {
        viewModel.getIsPackageActive().observe(this, Observer {
            if (it == false) {
                // showAlertDialog()
            }
        })

        viewModel.subjectPackageData.observe(this, Observer { subjectPackageData ->
            setUpSubjectContentTab(subjectPackageData)
        })
    }

    private fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setMessage(resources.getString(R.string.package_expired_message))
            setPositiveButton("Ok") { _, _ ->
                exitActivity()
            }
            setCancelable(false)
        }.create()
        alertDialog.show()
    }

    private fun exitActivity() {
        this.finish()
    }

    private fun setUpSubjectContentTab(subjectPackageData: SubjectPackageData?) {
        if (subjectPackageData == null) return
        
        val subjectIndex = viewModel.getSubjectIndex()
        val tabIndex = viewModel.getCurrentTabIndex()
        val tabFragments: ArrayList<Fragment> = ArrayList()

        val count = viewModel.getExamTypesCount()
        if (count == 0) {
            return
        }

        for (fragmentIndex in 0 until count) {
            val fragment =
                ExamTypeFragment.newInstance(
                    fragmentIndex,
                    viewModel.getSubjectName(),
                    subjectPackageData.expiresOn ?: "",
                    subjectPackageData.packageName ?: "",
                    subjectIndex
                )
            tabFragments.add(fragment)
        }

        val viewPagerAdapter = SubjectContentTableViewPagerAdapter(
            this.supportFragmentManager,
            tabFragments,
            viewModel.getExamTitles()
        )
        viewPager.adapter = viewPagerAdapter
        viewPager.currentItem = tabIndex
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setupActivityViewListeners() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { updateCurrentTabIndex(it) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    fun updateCurrentTabIndex(index: Int) {
        viewModel.updateCurrentTabIndex(index)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    override fun onResume() {
        super.onResume()
        // Ensure state is ready when returning to activity
        start()
        updateUsersStats()
    }

    private fun start() {
        if (!viewModel.isPaper1DataInitialised() && !viewModel.isUserDataInitialised()) {
            beginSetup()
        } else {
            // Already initialized, ensure UI is updated
            viewModel.initPaper1DataRepository()
            setTitle()
            viewModel.loadSubjectPackageDataFromUserDataRepository()
        }
    }

    override fun onShowPackageExpired() {
        showAlertDialog()
    }

    override fun onCheckIfPackageHasExpired(): Boolean {
        return viewModel.getPackageStatus()
    }

    override fun onContentAccessDenied() {
        val contentAccessDeniedDialog = AlertDialog.Builder(this)
        contentAccessDeniedDialog.apply {
            setMessage(resources.getString(R.string.content_access_denied_Message))
            setPositiveButton("Ok") { d, _ ->
                d.dismiss()
            }
        }.create().show()
    }

    private fun beginSetup() {
        val id = UtilityFunctions().getDeviceId(this)
        viewModel.beginSetup(id, this, object : UserDataManager.UserDataManagerListener {
            override fun onSuccess() {
                runOnUiThread {
                    viewModel.initPaper1DataRepository()
                    setTitle()
                    viewModel.loadSubjectPackageDataFromUserDataRepository()
                }
            }
            override fun onUserDataUnavailable() {}
        })
    }

    override fun onGotoPaperActivity(intent: Intent) {
        val examItemIndex = intent.getIntExtra(MCQConstants.EXAM_ITEM_INDEX, 0)
        if (examItemIndex == 0) {
            startActivity(intent)
        } else {
            if (!viewModel.getPackageStatus()) {
                showAlertDialog()
            } else {
                startActivity(intent)
            }
        }
    }

    fun updateUsersStats(){
        val params = hashMapOf("test" to "test")
        val id = UtilityFunctions().getDeviceId(this)
        ConnectivityTester.checkConnection(this, params, object: ConnectivityTester.OnTestConnectionListener{
            override fun onConnectionAvailable() {
                viewModel.updateScoresStatsInRemoteServer(id)
            }

            override fun onConnectionUnavailable() {
                Log.i("ConnectivityTester", "onConnectionUnavailable")
            }
        })
    }
}
