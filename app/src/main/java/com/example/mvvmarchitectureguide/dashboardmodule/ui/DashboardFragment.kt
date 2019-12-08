package com.example.mvvmarchitectureguide.dashboardmodule.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.example.mvvmarchitectureguide.R
import com.example.mvvmarchitectureguide.databinding.FragmentDashboardBinding
import com.example.mvvmarchitectureguide.di.Injectable
import com.example.mvvmarchitectureguide.di.injectViewModel
import com.example.mvvmarchitectureguide.ui.VerticalItemDecoration
import com.example.mvvmarchitectureguide.ui.hide
import com.example.mvvmarchitectureguide.util.ConnectivityUtil
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class DashboardFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dashboardViewModel =
            ViewModelProviders.of(this, viewModelFactory)[DashboardViewModel::class.java]
//        dashboardViewModel.connectivityAvailable = ConnectivityUtil.isConnected(context!!)
        dashboardViewModel.connectivityAvailable = false

        val binding = FragmentDashboardBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = UserAdapter()
        binding.recyclerView.addItemDecoration(
            VerticalItemDecoration(resources.getDimension(R.dimen.margin_normal).toInt(), true)
        )
        binding.recyclerView.adapter = adapter

        //TODO: Remove this line and add realistic best practices data gathering functionality
        GlobalScope.launch {
            dashboardViewModel.fillList()
        }
        subscribeUI(binding, adapter)

        return binding.root
    }

    private fun subscribeUI(binding: FragmentDashboardBinding, adapter: UserAdapter) {
        dashboardViewModel.users.observe(viewLifecycleOwner) {
            binding.progressBar.hide()
            adapter.submitList(it)
        }
    }
}