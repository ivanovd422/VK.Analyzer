package com.lab422.vkanalyzer.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {

    @JvmField protected var holderModel: T? = null

    abstract fun onBind(model: T)

    fun bindHolder(item: Any?) {
        if (item == null) return
        val temp: T = item as T
        holderModel = temp

        onBind(temp)
    }
}
