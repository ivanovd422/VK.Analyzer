package com.lab422.vkanalyzer.ui.mainScreen

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(), LifecycleObserver {

    private val menuItems: MutableLiveData<List<BarItem>> = MutableLiveData()
    private val selectedTabState: MutableLiveData<BarItem> = MutableLiveData()

    private val barItems: List<BarItem> = BottomBarProvider().getBarItems()
    private val defaultBarItem: BarItem = getBarItemById(BottomBarProvider.PHOTOS_NEAR_ID)

    init {
        initLiveData()
    }

    fun getBottomBarItems(): LiveData<List<BarItem>> = menuItems

    fun getCurrentTab(): LiveData<BarItem> = selectedTabState

    fun onBottomMenuClicked(itemId: Int) {
        val item = getBarItemById(itemId)
        if (item == selectedTabState.value) {
            return
        }
        selectTab(item)
    }

    private fun selectTab(item: BarItem) {
        selectedTabState.value = item
    }

    private fun initLiveData() {
        menuItems.value = barItems
        selectedTabState.value = defaultBarItem
    }

    private fun getBarItemById(itemId: Int): BarItem = barItems.find { it.id == itemId }!!

}