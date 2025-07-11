//package com.example.gceolmcqs.adapters
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentContainerView
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.replace
//import androidx.lifecycle.Lifecycle
//import androidx.recyclerview.widget.RecyclerView
//import com.example.gceolmcqs.R
//import com.example.gceolmcqs.databinding.HomeFragmentRecyclerContainerViewItemBinding
//import com.example.gceolmcqs.databinding.SubjectItemCardBinding
//import com.example.gceolmcqs.datamodels.SubjectPackageData
//import com.example.gceolmcqs.fragments.HomeFragment
//
//
//class HomeRecyclerViewAdapter(
//    private val subjectsPackages: List<Fragment>,
//    private val fm: FragmentManager,
//    private val lifecycle: Lifecycle
////    private val onHomeRecyclerItemListener: OnHomeRecyclerItemListener
//
//) : RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {
//
//    private lateinit var binding: HomeFragmentRecyclerContainerViewItemBinding
//
//    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
//
////        val container: FragmentContainerView = view.findViewById(R.id.fragmentContainer)
//
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
////        val view = LayoutInflater.from(context).inflate(R.layout.subject_item_card, parent, false)
//        binding = HomeFragmentRecyclerContainerViewItemBinding.inflate(LayoutInflater.from(parent.context))
//
//        return ViewHolder(binding.root)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
////        val fragmentContainer = fm.findFragmentById(R.id.fragmentContainerId) as HomeFragment
////        fragmentContainer.setSubscriptionPackage(subjectsPackages[position])
////
//        fm.beginTransaction().apply {
//            replace(binding.fragmentContainerId.id, subjectsPackages[position])
////            commitNowAllowingStateLoss()
//        }.commit()
//
//    }
//
//    override fun getItemCount(): Int {
//        return subjectsPackages.size
//    }
//
//    interface OnHomeRecyclerItemListener {
//        fun onSubscribeButtonClicked(position: Int, subjectName: String)
//        fun onPaper1ButtonClicked(position: Int)
//        fun onPaper2ButtonClicked(position: Int)
//        fun onPackageExpired(index: Int)
//
//    }
//
//}