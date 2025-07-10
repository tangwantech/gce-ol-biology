package com.example.gceolmcqs.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.viewModels
import com.example.gceolmcqs.R
import com.example.gceolmcqs.adapters.Paper2TabPagerAdapter
import com.example.gceolmcqs.adapters.SubjectContentTableViewPagerAdapter
import com.example.gceolmcqs.databinding.FragmentPaper2ExamTypesTabBinding
import com.example.gceolmcqs.viewmodels.Paper2ActivityViewModel
import com.google.android.material.tabs.TabLayout


private const val SUBJECT_INDEX = "subjectIndex"
private const val SUBJECT_PACKAGE_NAME = "subjectPackageName"
private const val EXPIRES_ON = "expiresOn"
private const val TAB_INDEX = "tabIndex"
private const val PAPER2_EXAM_TYPES_TAB = "paper2ExamTypesTab"

class Paper2ExamTypesTabFragment : Fragment() {
    private var subjectIndex: Int? = null
    private var subjectPackageName: String? = null
    private var expiresOn: String? = null
    private lateinit var pref: SharedPreferences
    private var currentTabIndex  = 0

    private lateinit var binding: FragmentPaper2ExamTypesTabBinding
    private val viewModel: Paper2ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subjectIndex = it.getInt(SUBJECT_INDEX, 0)
            subjectPackageName = it.getString(SUBJECT_PACKAGE_NAME)
            expiresOn = it.getString(EXPIRES_ON)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPaper2ExamTypesTabBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = requireActivity().getSharedPreferences(PAPER2_EXAM_TYPES_TAB, 0)
        setUpExamTypesTab()
//        setupTabListeners()
        setTitle()

    }

    private fun setTitle(){
        val subjectIndex = viewModel.getSubjectIndex()
        requireActivity().title = viewModel.getSubjectName(subjectIndex) + " " + getString(R.string.paper_2)
    }

    override fun onResume() {
        super.onResume()
//        setUpExamTypesTab()
//        setupTabListeners()
//        setTitle()
    }

    private fun setUpExamTypesTab() {
        println("Setting up paper2 exam types tab")
        val fragments = ArrayList<Fragment>()
        val examTypeTitles = viewModel.getPaper2ExamTitles(subjectIndex!!)
        examTypeTitles.forEachIndexed { index, s ->
            binding.layoutHomeTab.homeTab.addTab(binding.layoutHomeTab.homeTab.newTab().setText(s))
            fragments.add(Paper2ExamTypeFragment.newInstance(subjectIndex!!, index))
        }



        println("supportFragmentManager within on Paper2ExamTypesTabFragment ${requireActivity().supportFragmentManager}")
        val pagerAdapter = Paper2TabPagerAdapter(childFragmentManager, fragments)
//        val pagerAdapter = SubjectContentTableViewPagerAdapter(requireActivity().supportFragmentManager)
        binding.layoutHomeTab.homeViewPager.adapter = pagerAdapter
        binding.layoutHomeTab.homeViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.layoutHomeTab.homeTab))


        binding.layoutHomeTab.homeTab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.layoutHomeTab.homeViewPager.currentItem = tab?.position!!

//                println("tabPosition: ${tab.position}")
                saveSelectedTab(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun saveSelectedTab(index: Int){
        pref.edit().apply {
            putInt(TAB_INDEX, index)
        }.apply()
    }

    companion object {

        @JvmStatic
        fun newInstance(subjectIndex: Int, subjectPackageName: String, expiresOn: String) =
            Paper2ExamTypesTabFragment().apply {
                arguments = Bundle().apply {
                    putInt(SUBJECT_INDEX, subjectIndex)
                    putString(SUBJECT_PACKAGE_NAME, subjectPackageName)
                    putString(EXPIRES_ON, expiresOn)

                }
            }
    }
}