package com.example.app_server

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.util.Log
import androidx.core.content.ContextCompat

data class ContactData(
    val name: String,
    val phones: List<String>,
    val emails: List<String>
)

data class Contact(
    val id: Long,
    val data: ContactData
)

class ContactUtil(val applicationContext: Context) {
    val contentResolver: ContentResolver = applicationContext.contentResolver

    fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            throw IllegalStateException("Read contacts permission is denied")
        }
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            throw IllegalStateException("Write contacts permission is denied")
        }
    }

    fun deduplicateContacts(): Int {
        var count = 0
        val seen = mutableSetOf<ContactData>()
        for (contact in getAllContacts()) {
            if (!seen.add(contact.data)) {
                removeContact(contact.id)
                count++
            }
        }
        return count
    }

    fun getAllContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()

        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME
        )

        contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)) ?: ""

                val contact = Contact(
                    id = id,
                    data = ContactData(
                        name = name,
                        phones = getContactPhones(id),
                        emails = getContactEmails(id),
                    )
                )
                contacts.add(contact)
            }
        }
        return contacts
    }

    fun removeContact(contactId: Long) {
        val uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
        contentResolver.delete(uri, null, null)
    }

    private fun getContactPhones(contactId: Long): List<String> {
        val phones = mutableListOf<String>()
        contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                phones.add(cursor.getString(0))
            }
        }
        return phones
    }

    private fun getContactEmails(contactId: Long): List<String> {
        val emails = mutableListOf<String>()
        contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS),
            "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                emails.add(cursor.getString(0))
            }
        }
        return emails
    }
}