package com.example.gceolmcqs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.gceolmcqs.adapters.SyllabusChapterItemClickLister
import com.example.gceolmcqs.databinding.ActivitySyllabusBinding
import com.example.gceolmcqs.fragments.ClassChaptersFragment
import com.example.gceolmcqs.fragments.LessonsFragment
import com.example.gceolmcqs.repository.SyllabusRepository
import com.example.gceolmcqs.viewmodels.SyllabusViewModel

class SyllabusActivity : AppCompatActivity(), SyllabusChapterItemClickLister {
    private lateinit var binding:ActivitySyllabusBinding
    private lateinit var viewModel: SyllabusViewModel
    private lateinit var fragmentMan: FragmentManager
    private var classIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySyllabusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        updateClassIndex()
        initSyllabusViewModel()
        gotoChaptersFragment()

    }

    private fun updateClassIndex(){
        classIndex = intent.getIntExtra(CLASS_INDEX, 0)
    }

    companion object{
        private const val CLASS_INDEX = "classIndex"
        fun getIntent(context: Context, classIndex:Int): Intent {
            val intent = Intent(context, SyllabusActivity::class.java)
            intent.putExtra(CLASS_INDEX, classIndex)
            return intent
        }
    }

    override fun onResume() {
        super.onResume()
        title = getString(R.string.syllabus)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                if(fragmentMan.backStackEntryCount == 0){
                    finish()
                }else{
                    fragmentMan.popBackStack()
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initSyllabusViewModel(){
        viewModel = ViewModelProvider(this)[SyllabusViewModel::class.java]
        viewModel.initSyllabusRepository(this)
    }

    private fun gotoChaptersFragment(){
        val classChaptersFragment = ClassChaptersFragment.newInstance(classIndex)
        fragmentMan = supportFragmentManager
        fragmentMan.beginTransaction().replace(R.id.fragmentContainer, classChaptersFragment).commit()
    }

    private fun gotoLessonsFragment(chapterIndex: Int){
        val lessonsFragment = LessonsFragment.newInstance(classIndex, chapterIndex)
        fragmentMan = supportFragmentManager
        fragmentMan.beginTransaction().apply {
            replace(R.id.fragmentContainer, lessonsFragment)
            addToBackStack(null)
            commit()

        }
    }


    override fun onItemClick(itemIndex: Int) {
        gotoLessonsFragment(itemIndex)
    }

}