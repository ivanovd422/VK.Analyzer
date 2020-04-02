package com.lab422.vkanalyzer.ui.login

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDialogFragment
import com.lab422.vkanalyzer.R
import kotlinx.android.synthetic.main.dialog_auth_info.view.*

class DialogAuthInfo : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inputView = LayoutInflater.from(activity).inflate(R.layout.dialog_auth_info, null)
        val builder = AlertDialog.Builder(activity).setView(inputView)

        inputView.btn_ok.setOnClickListener {
            this.dismiss()
        }

        return builder.create()
    }
}