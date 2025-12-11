package com.ride_sharing.linc.viewmodel

import androidx.lifecycle.ViewModel
import com.ride_sharing.linc.view.NavigationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// ViewModel for tab selection
open class ViewModelForSelectedTab : ViewModel() {
    private val _selectedTab = MutableStateFlow(NavigationItem.Home)
    val selectedTab: StateFlow<NavigationItem> = _selectedTab
    fun selectTab(tab: NavigationItem) { _selectedTab.value = tab }
}

