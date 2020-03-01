package com.lab422.vkanalyzer.ui.base

class RowDataModel<T : Rawable, U : Any>(val rowType: T, val rowId: String, val value: U?) : Diffable {
    override fun isSame(same: Diffable): Boolean {
        if (same !is RowDataModel<*, *>) {
            return false
        }
        return  rowId isSame same.rowId &&
            rowType isSame same.rowType
    }
    override fun isContentSame(same: Diffable): Boolean {
        if (same !is RowDataModel<*, *>) {
            return false
        }
        return  rowType isContentSame same.rowType &&
            value isContentSame same.value
    }
}

@Suppress("FunctionName")
fun <T : Rawable> RowDataModel(rowType: T, rowId: String): RowDataModel<T, Any> {
    return RowDataModel(rowType, rowId, null)
}

private infix fun Any?.isContentSame(cmp: Any?)= compareBy(this, cmp) {
    isContentSame(it)
}

private infix fun Any?.isSame(cmp: Any?) =  compareBy(this, cmp) {
    isSame(it)
}

private fun compareBy(first: Any?, second: Any?, cmp: Diffable.(Diffable) -> Boolean): Boolean {
    if (first == null || second == null) {
        return first == null && second == null
    }
    return when {
        first is Diffable && second is Diffable ->
            first.cmp(second)
        first is Rawable && second is Rawable && first.javaClass == second.javaClass ->
            first.rawValue == second.rawValue
        else ->
            first == second
    }
}