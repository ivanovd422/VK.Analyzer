package com.lab422.vkanalyzer.ui.login

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.lab422.vkanalyzer.databinding.DialogAuthInfoBinding

class DialogAuthInfo : AppCompatDialogFragment() {

    private lateinit var binding: DialogAuthInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return DialogAuthInfoBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity).setView(binding.root)

        binding.btnOk.setOnClickListener {
            this.dismiss()
        }

        return builder.create()
    }
}
