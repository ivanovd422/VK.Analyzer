package com.lab422.vkanalyzer.ui.photosNear

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.lab422.vkanalyzer.databinding.DialogPermissionsInfoBinding

class PermissionsDialog : AppCompatDialogFragment() {

    private lateinit var binding: DialogPermissionsInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return DialogPermissionsInfoBinding.inflate(inflater, container, false).also {
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
