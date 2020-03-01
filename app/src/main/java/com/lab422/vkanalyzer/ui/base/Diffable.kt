package com.lab422.vkanalyzer.ui.base

interface Diffable{
    fun isSame(same: Diffable): Boolean
    fun isContentSame(same: Diffable): Boolean
}