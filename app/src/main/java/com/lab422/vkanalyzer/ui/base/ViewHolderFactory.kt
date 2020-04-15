package com.lab422.vkanalyzer.ui.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lab422.common.StringProvider

interface ViewHolderFactory {
    fun <T> createViewHolder(parent: ViewGroup, viewType: Int, stringProvider: com.lab422.common.StringProvider): RecyclerView.ViewHolder
}