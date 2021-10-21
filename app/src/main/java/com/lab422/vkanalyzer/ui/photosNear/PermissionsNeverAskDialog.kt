package com.lab422.vkanalyzer.ui.photosNear

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.lab422.vkanalyzer.databinding.DialogPermissionsNeverAskBinding

class PermissionsNeverAskDialog : AppCompatDialogFragment() {

    interface OpenPermissionsSettingsAction {
        fun onOpenPermissionsSettingsClicked()
    }

    private lateinit var binding: DialogPermissionsNeverAskBinding

    private var listener: OpenPermissionsSettingsAction? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DialogPermissionsNeverAskBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity).setView(binding.root)

        binding.btnAccept.setOnClickListener {
            listener?.onOpenPermissionsSettingsClicked()
            this.dismiss()
        }

        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = if (parentFragment is OpenPermissionsSettingsAction)
            parentFragment as OpenPermissionsSettingsAction
        else
            null
    }
}
