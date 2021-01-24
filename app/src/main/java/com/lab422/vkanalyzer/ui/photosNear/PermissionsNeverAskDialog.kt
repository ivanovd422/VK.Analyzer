package com.lab422.vkanalyzer.ui.photosNear

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDialogFragment
import com.lab422.vkanalyzer.R
import kotlinx.android.synthetic.main.dialog_permissions_never_ask.view.*

class PermissionsNeverAskDialog : AppCompatDialogFragment() {

    interface OpenPermissionsSettingsAction {
        fun onOpenPermissionsSettingsClicked()
    }

    private var listener: OpenPermissionsSettingsAction? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inputView = LayoutInflater.from(activity).inflate(R.layout.dialog_permissions_never_ask, null)
        val builder = AlertDialog.Builder(activity).setView(inputView)

        inputView.btn_accept.setOnClickListener {
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
