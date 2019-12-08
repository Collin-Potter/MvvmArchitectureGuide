package com.example.mvvmarchitectureguide.di

import com.example.mvvmarchitectureguide.dashboardmodule.ui.DashboardFragment
import com.example.mvvmarchitectureguide.homemodule.ui.HomeFragment
import com.example.mvvmarchitectureguide.notificationsmodule.ui.NotificationsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
//    @ContributesAndroidInjector
//    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeDashboardFragment(): DashboardFragment

//    @ContributesAndroidInjector
//    abstract fun contributeNotificationsFragment(): NotificationsFragment
}