package com.example.quiztesting.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class TaskStatus implements Parcelable {

    HashMap<String, String> hashMap;

    public TaskStatus(HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }

    public TaskStatus() {
    }

    protected TaskStatus(Parcel in) {
        hashMap = (HashMap<String, String>) in.readSerializable();
    }

    public static final Creator<TaskStatus> CREATOR = new Creator<TaskStatus>() {
        @Override
        public TaskStatus createFromParcel(Parcel in) {
            return new TaskStatus(in);
        }

        @Override
        public TaskStatus[] newArray(int size) {
            return new TaskStatus[size];
        }
    };

    public HashMap<String, String> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeSerializable(hashMap);
    }
}
