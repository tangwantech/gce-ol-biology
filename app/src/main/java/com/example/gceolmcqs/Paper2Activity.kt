package com.example.gceolmcqs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.gceolmcqs.databinding.ActivityPaper2Binding
import com.example.gceolmcqs.databinding.DefinitionDialogBinding
import com.example.gceolmcqs.fragments.Paper2ExamTypeFragment
import com.example.gceolmcqs.fragments.Paper2ExamTypeFragment.OnPackageExpiredListener
import com.example.gceolmcqs.fragments.Paper2ExamTypesTabFragment
import com.example.gceolmcqs.fragments.Paper2QuestionsSolutionFragment
import com.example.gceolmcqs.viewmodels.Paper2ActivityViewModel

class Paper2Activity : AppCompatActivity(),
    Paper2ExamTypeFragment.OnNavigateToPaper2FragmentListener,
    Paper2QuestionsSolutionFragment.OnShowDefinitionListener,
    OnPackageExpiredListener
{
    companion object{
        const val SUBJECT_INDEX = "subjectIndex"
        fun getIntent(context: Context, subjectIndex: Int): Intent {
            val intent = Intent(context, Paper2Activity::class.java)
            intent.putExtra(SUBJECT_INDEX, subjectIndex)
            return intent
        }
    }


    private lateinit var binding: ActivityPaper2Binding
    private val viewModel: Paper2ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaper2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        start()

//        println("subjectName: ${viewModel.getSubjectName(viewModel.getSubjectIndex())} At ${viewModel.getSubjectIndex()}")

    }



    override fun onSupportNavigateUp(): Boolean {

        if (supportFragmentManager.backStackEntryCount == 0){
            finish()
        }else{
            supportFragmentManager.popBackStack()

        }
        return super.onSupportNavigateUp()

    }

    private fun start(){
        if (!viewModel.isPaper2DataInitialised() && !viewModel.isUserDataInitialised()){
            beginSetup()
        }else{
            displayTabFragment()

        }
    }

    private fun displayTabFragment(){
        viewModel.updateSubjectIndex(intent.getIntExtra(SUBJECT_INDEX, 0))
        viewModel.initPaper2DataRepository()
        gotoExamTypesTabFragment()
    }

    private fun beginSetup(){
        val id = UtilityFunctions().getDeviceId(this)
        viewModel.beginSetup(id, this, object : UserDataManager.UserDataManagerListener{
            override fun onSuccess() {
                runOnUiThread {
                    displayTabFragment()
                }
            }

            override fun onUserDataUnavailable() {

            }
        })


    }

    private fun gotoExamTypesTabFragment(){
        val fragment = Paper2ExamTypesTabFragment.newInstance(viewModel.getSubjectIndex(), "", "")
        val transaction = supportFragmentManager.beginTransaction()
        transaction.apply {
            replace(binding.fragmentContainer.id, fragment)
            commit()
        }

    }

    private fun gotoPaper2Fragment(
        subjectIndex: Int,
        examTypeIndex: Int,
        examItemTitleIndex: Int
    ){
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = Paper2QuestionsSolutionFragment.newInstance(subjectIndex, examTypeIndex, examItemTitleIndex)
        transaction.apply {
            replace(binding.fragmentContainer.id, fragment)
            addToBackStack(null)
            commit()
        }

    }

    override fun onNavigateToPaper2Fragment(
        subjectIndex: Int,
        examTypeIndex: Int,
        examItemTitleIndex: Int
    ) {

       gotoPaper2Fragment(subjectIndex, examTypeIndex, examItemTitleIndex)
    }

    override fun onShowDefinition(term: String) {
        runOnUiThread {
//            println("Getting definition for $term")
            val definition = viewModel.getDefinition(term)
            val dialogBinding = DefinitionDialogBinding.inflate(layoutInflater)
            dialogBinding.tvTitle.text = term
            dialogBinding.tvDefinition.text = definition
            val dialog = AlertDialog.Builder(this).apply {
                setView(dialogBinding.root)
                setPositiveButton(getString(R.string.ok)){_, _ ->}
                setCancelable(false)
            }.create()
            dialog.show()
        }

    }

    private fun showAlertDialog(){
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

    override fun onPaper2PackageExpired() {
        showAlertDialog()
    }

}