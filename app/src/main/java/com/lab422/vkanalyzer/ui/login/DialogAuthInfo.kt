package com.lab422.vkanalyzer.ui.login

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AppCompatDialogFragment
import com.lab422.vkanalyzer.R

class DialogAuthInfo : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inputView =
            LayoutInflater.from(activity).inflate(R.layout.dialog_auth_info, null)
        val builder = AlertDialog.Builder(activity).setView(inputView)

        inputView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            this.dismiss()
        }

        return builder.create()
    }
}
