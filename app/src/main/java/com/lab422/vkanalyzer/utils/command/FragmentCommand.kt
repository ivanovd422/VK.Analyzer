package com.lab422.vkanalyzer.utils.command

import android.content.Context
import androidx.fragment.app.Fragment

interface FragmentCommand : Command {
    override fun execute(context: Context): Fragment
}
