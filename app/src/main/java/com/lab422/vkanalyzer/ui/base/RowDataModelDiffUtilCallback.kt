package com.lab422.vkanalyzer.ui.base

import androidx.recyclerview.widget.DiffUtil

class RowDataModelDiffUtilCallback<T>(
    private val oldValues: List<RowDataModel<T, *>>,
    private val newValues: List<RowDataModel<T, *>>
) : DiffUtil.Callback() where T : Rawable, T : Any {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldValues[oldItemPosition].isSame(newValues[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldValues[oldItemPosition].isContentSame(newValues[newItemPosition])

    override fun getOldListSize() = oldValues.size

    override fun getNewListSize() = newValues.size
}
