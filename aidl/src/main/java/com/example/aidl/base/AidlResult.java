package com.example.aidl.base;

import android.os.Parcel;
import android.os.Parcelable;

public class AidlResult implements Parcelable {
    public int code;
    public String message;
    public int deletedCount;

    public AidlResult(int code, String message, int deletedCount) {
        this.code = code;
        this.message = message;
        this.deletedCount = deletedCount;
    }

    protected AidlResult(Parcel in) {
        code = in.readInt();
        message = in.readString();
        deletedCount = in.readInt();
    }

    public static final Creator<AidlResult> CREATOR = new Creator<AidlResult>() {
        @Override
        public AidlResult createFromParcel(Parcel in) {
            return new AidlResult(in);
        }

        @Override
        public AidlResult[] newArray(int size) {
            return new AidlResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(message);
        dest.writeInt(deletedCount);
    }
}