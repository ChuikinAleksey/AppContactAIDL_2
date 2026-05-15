package com.example.aidl.base;

import android.os.Parcel;
import android.os.Parcelable;

public class AidlException implements Parcelable {
    public String errorMessage;
    public int errorCode;

    public AidlException(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    protected AidlException(Parcel in) {
        errorMessage = in.readString();
        errorCode = in.readInt();
    }

    public static final Creator<AidlException> CREATOR = new Creator<AidlException>() {
        @Override
        public AidlException createFromParcel(Parcel in) {
            return new AidlException(in);
        }

        @Override
        public AidlException[] newArray(int size) {
            return new AidlException[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(errorMessage);
        dest.writeInt(errorCode);
    }
}
