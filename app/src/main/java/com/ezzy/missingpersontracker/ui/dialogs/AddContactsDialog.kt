package com.ezzy.missingpersontracker.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ezzy.core.domain.Contact
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.ui.fragments.report.ReportMissingPersonViewModel
import com.ezzy.missingpersontracker.util.showToast
import com.ezzy.missingpersontracker.util.visible
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AddContactsDialog : DialogFragment() {

    private lateinit var closeText: TextView
    private lateinit var addButton: Button
    private lateinit var contactTypeDropDown: AutoCompleteTextView
    private lateinit var contactNameTextView: TextInputEditText
    private lateinit var contactPhoneTextView: TextInputEditText
    private lateinit var contactEmailTextView: TextInputEditText

    private val personViewModel: ReportMissingPersonViewModel by activityViewModels()
    private val personContacts = mutableListOf<Contact>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.add_contacts_dialog, null)

        view?.let {
            closeText = it.findViewById(R.id.closeText)
            addButton = it.findViewById(R.id.saveButton)
            contactTypeDropDown = it.findViewById(R.id.contact_type)
            contactNameTextView = it.findViewById(R.id.contact_name_textView)
            contactPhoneTextView = it.findViewById(R.id.contact_phone_textView)
            contactEmailTextView = it.findViewById(R.id.contact_email_textView)
        }

        closeText.setOnClickListener { dialog?.dismiss() }
        contactTypeDropDown.addTextChangedListener { text ->
            when (text.toString()) {
                "Phone" -> contactPhoneTextView.visible()
                "Email" -> contactEmailTextView.visible()
            }
        }

        addButton.setOnClickListener {
            saveContact()
        }

        builder.setView(view)
        return builder.create()
    }

    private fun saveContact() {
        var contact: Contact? = null
        if (contactNameTextView.text!!.isEmpty()) {
            showToast(getString(R.string.contact_name_err))
        } else {
            if (contactTypeDropDown.text.toString() == "Phone") {
                if (contactPhoneTextView.text!!.isEmpty()) {
                    showToast(getString(R.string.contact_phone_err))
                } else {
                    contact = Contact(
                        contactNameTextView.text.toString(),
                        contactPhoneTextView.text.toString(),
                        null
                    )
                }
            } else if (contactTypeDropDown.text.toString() == "Email") {
                if (contactEmailTextView.text!!.isEmpty()) {
                    showToast(getString(R.string.contact_email_err))
                } else {
                    contact = Contact(
                        contactNameTextView.text.toString(),
                        null,
                        contactEmailTextView.text.toString()
                    )
                }
            }
        }
        contact?.let { cnt ->
            personContacts.add(cnt)
            personViewModel.addPersonContacts(cnt)
            dialog?.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val contactType = resources.getStringArray(R.array.contact_type)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, contactType)
        contactTypeDropDown.setAdapter(adapter)
    }

}