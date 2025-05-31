package com.example.gceolmcqs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.gceolmcqs.databinding.ActivityNotesBinding
import com.example.gceolmcqs.databinding.DefinitionDialogBinding
import com.example.gceolmcqs.fragments.NotesFragment
import com.example.gceolmcqs.fragments.TableOfContentsFragment
import com.example.gceolmcqs.viewmodels.NotesActivityViewModel

class NotesActivity : AppCompatActivity(), TableOfContentsFragment.OnTableOfContentClickLister, NotesFragment.OnShowDefinitionListener {
    private lateinit var binding: ActivityNotesBinding
    private val viewModel: NotesActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.chapters)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        start()
    }

    override fun onSupportNavigateUp(): Boolean {
        if (supportFragmentManager.backStackEntryCount == 0){
            finish()
        }else{
            supportFragmentManager.popBackStack()
        }
        return super.onSupportNavigateUp()

    }

    private fun gotoTableOfContentsFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.apply {
            replace(binding.fragmentContainer.id, TableOfContentsFragment.newInstance(viewModel.getChapterNames()))
            commit()
        }

    }

    private fun gotoNotesFragment(itemPosition: Int){
        viewModel.updateCurrentFragmentIndex(1)
        val notesFragment = NotesFragment.newInstance(itemPosition)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.apply {
            replace(binding.fragmentContainer.id, notesFragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun beginSetup(){
        val id = UtilityFunctions().getDeviceId(this)
        viewModel.beginSetup(id, this, object : UserDataManager.UserDataManagerListener{
            override fun onSuccess() {
                runOnUiThread {
                    viewModel.initNotesDataRepository()
//                    gotoCurrentFragment()
                    gotoTableOfContentsFragment()
                }

            }

            override fun onUserDataUnavailable() {

            }
        })


    }

    private fun start(){
        if (!viewModel.isNotesDataInitialised() && !viewModel.isUserDataInitialised()){
            beginSetup()
        }else{
            viewModel.initNotesDataRepository()
//            gotoCurrentFragment()
            gotoTableOfContentsFragment()

        }
    }

//    private fun gotoCurrentFragment(){
//        val count = supportFragmentManager.backStackEntryCount
//        var currentIndex = 0
//        if (count > 0){
//            currentIndex = count - 1
//
//        }
//        when (currentIndex){
//            0 -> {
//                gotoTableOfContentsFragment()
//            }else ->{
////                gotoNotesFragment()
//            }
//        }
//    }


    override fun onResume() {
        super.onResume()

    }

    override fun onTableOfContentItemClick(itemPosition: Int) {
        gotoNotesFragment(itemPosition)
    }

    companion object{
        fun getIntent(context: Context): Intent{
            return Intent(context, NotesActivity::class.java)
        }
    }

    override fun onShowDefinition(term: String) {
        runOnUiThread {
            println("Getting definition for $term")
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

    override fun onGotoExercise(chapterIndex: Int, exerciseNumber: String) {
        println("Navigating to exercise $exerciseNumber")
    }

}