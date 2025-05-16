package com.example.gceolmcqs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.gceolmcqs.adapters.DictionaryActivityRecyclerAdapter
import com.example.gceolmcqs.adapters.ExamTypeRecyclerViewAdapter
import com.example.gceolmcqs.databinding.ActivityDictionaryBinding
import com.example.gceolmcqs.databinding.DefinitionDialogBinding
import com.example.gceolmcqs.viewmodels.DictionaryActivityViewModel

class DictionaryActivity : AppCompatActivity(), DictionaryActivityRecyclerAdapter.OnItemClickListener {
    private lateinit var binding: ActivityDictionaryBinding
    private lateinit var viewModel: DictionaryActivityViewModel
    private lateinit var adapter: DictionaryActivityRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDictionaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setupViewModel()
    }

    override fun onResume() {
        super.onResume()
        title = getString(R.string.biology_dictionary)
        start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(this)[DictionaryActivityViewModel::class.java]
    }

    private fun setupSearchViewListener(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchKey(it)
                    adapter.notifyDataSetChanged()

                }

                return true
            }

        })
    }

    private fun setupRecyclerView(){
        val layoutMan = LinearLayoutManager(this)
        adapter = DictionaryActivityRecyclerAdapter(viewModel.getDisplayList(), this)
        binding.recyclerView.layoutManager = layoutMan
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

    }

    private fun start(){
        if (!viewModel.isUserDataInitialised()){
            viewModel.beginSetup(UtilityFunctions().getDeviceId(this), this, object: UserDataManager.UserDataManagerListener{
                override fun onSuccess() {
                    viewModel.initDictionaryRepo()
                    setupRecyclerView()
                    setupSearchViewListener()
                }

                override fun onUserDataUnavailable() {

                }
            })
        }else{
            viewModel.initDictionaryRepo()
            setupRecyclerView()
            setupSearchViewListener()
        }
    }

    override fun onItemClick(keyWord: String) {
        val definition = viewModel.getDefinition(keyWord)
        displayDefinition(keyWord, definition)


    }

    private fun displayDefinition(keyWord: String, definition: String){
        val dialogBinding = DefinitionDialogBinding.inflate(layoutInflater)
        dialogBinding.tvTitle.text = keyWord
        dialogBinding.tvDefinition.text = definition
        val dialog = AlertDialog.Builder(this).apply {
            setView(dialogBinding.root)
            setPositiveButton(getString(R.string.ok)){_, _ ->}
            setCancelable(false)
        }.create()
        dialog.show()
    }

    companion object{
        fun getIntent(context: Context): Intent{
            return Intent(context, DictionaryActivity::class.java)
        }
    }
}