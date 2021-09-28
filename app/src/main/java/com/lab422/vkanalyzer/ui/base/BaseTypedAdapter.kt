package com.lab422.vkanalyzer.ui.base

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lab422.common.StringProvider
import java.lang.RuntimeException

// todo rewrite adapter with LiveData inside
abstract class BaseTypedAdapter<T : Rawable>(
    generalDataList: List<RowDataModel<T, *>>,
    private val stringProvider: StringProvider,
    private val useDiffs: Boolean = true,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var previousData: List<RowDataModel<T, *>> = generalDataList.toList()
    private val adapterData: MutableLiveData<List<RowDataModel<T, *>>> = MutableLiveData(generalDataList)
    private val viewHolderFactories: MutableMap<Int, ViewHolderFactory> = mutableMapOf()

    init {
        adapterData.observe(
            lifecycleOwner,
            Observer { data ->
                if (useDiffs) {
                    val diffUtilCallback = RowDataModelDiffUtilCallback(previousData, data)
                    val diffResult = DiffUtil.calculateDiff(diffUtilCallback, true)
                    diffResult.dispatchUpdatesTo(this)
                } else {
                    notifyDataSetChanged()
                }
                previousData = data
            }
        )
    }

    fun reload(dataList: List<RowDataModel<T, *>>) {
        adapterData.value = dataList
    }

    protected fun addFactory(rowType: T, factory: ViewHolderFactory) {
        viewHolderFactories[rowType.rawValue] = factory
    }

    private fun viewTypeByPosition(position: Int): T {
        return adapterData.value!![position].rowType
    }

    override fun getItemCount() = adapterData.value!!.size

    override fun getItemViewType(position: Int): Int {
        return viewTypeByPosition(position).rawValue
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val factory = viewHolderFactories[viewType]
            ?: throw RuntimeException("Not found factory for viewType=$viewType")
        return factory.createViewHolder<T>(parent, viewType, stringProvider)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        @Suppress("UNCHECKED_CAST")
        (holder as? Bindable<T>)?.onBind(adapterData.value!![holder.adapterPosition])
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = Unit

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        val baseHolder = holder as Bindable<*>
        baseHolder.unBind()
        super.onViewDetachedFromWindow(holder)
    }

    fun getItem(position: Int): RowDataModel<T, *> {
        return adapterData.value!![position]
    }

    fun tryGetItem(position: Int): RowDataModel<T, *>? {
        val items = adapterData.value
        if (items != null && position in 0 until items.count()) {
            return items[position]
        }
        return null
    }

    override fun unregisterAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.unregisterAdapterDataObserver(observer)
        adapterData.removeObservers(lifecycleOwner)
    }
}
