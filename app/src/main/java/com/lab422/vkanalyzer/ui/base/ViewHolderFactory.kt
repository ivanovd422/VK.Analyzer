package com.lab422.vkanalyzer.ui.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lab422.vkanalyzer.utils.stringProvider.StringProvider

interface ViewHolderFactory {
    fun <T> createViewHolder(parent: ViewGroup, viewType: Int, stringProvider: StringProvider): RecyclerView.ViewHolder
}