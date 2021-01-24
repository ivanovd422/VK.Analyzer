package com.lab422.vkanalyzer.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseTypedViewHolder<T : Rawable>(view: View) : RecyclerView.ViewHolder(view), Bindable<T> {

    override fun onBind(model: RowDataModel<T, *>) {
    }

    override fun unBind() {
    }
}
