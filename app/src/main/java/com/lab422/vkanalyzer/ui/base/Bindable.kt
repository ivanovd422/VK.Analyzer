package com.lab422.vkanalyzer.ui.base

interface Bindable<T : Rawable> {
    fun onBind(model: RowDataModel<T, *>)
    fun unBind()
}
