package com.example.aidl;

import com.example.aidl.callback.AsyncCallback;

interface PhoneRemove {
    void removeContacts(AsyncCallback callback);
}