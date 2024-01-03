package com.example.quiztesting.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

class SetInfo implements Parcelable {
    private long postedTime;
    private long dueDate;

    public SetInfo(long postedTime, long dueDate) {
        this.postedTime = postedTime;
        this.dueDate = dueDate;
    }

    public SetInfo() {
    }

    protected SetInfo(Parcel in) {
        postedTime = in.readLong();
        dueDate = in.readLong();
    }

    public static final Creator<SetInfo> CREATOR = new Creator<SetInfo>() {
        @Override
        public SetInfo createFromParcel(Parcel in) {
            return new SetInfo(in);
        }

        @Override
        public SetInfo[] newArray(int size) {
            return new SetInfo[size];
        }
    };

    public long getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(long postedTime) {
        this.postedTime = postedTime;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(postedTime);
        dest.writeLong(dueDate);
    }
}

public class PostedQuizModel implements Parcelable {
    private String ctgKey;
    HashMap<String, SetInfo> setKeyInfo;

    public PostedQuizModel() {
        setKeyInfo = new HashMap<String, SetInfo>();
    }

    protected PostedQuizModel(Parcel in) {
        ctgKey = in.readString();
    }

    public static final Creator<PostedQuizModel> CREATOR = new Creator<PostedQuizModel>() {
        @Override
        public PostedQuizModel createFromParcel(Parcel in) {
            return new PostedQuizModel(in);
        }

        @Override
        public PostedQuizModel[] newArray(int size) {
            return new PostedQuizModel[size];
        }
    };

    public String getCtgKey() {
        return ctgKey;
    }

    public void setCtgKey(String ctgKey) {
        this.ctgKey = ctgKey;
    }

    public HashMap<String, SetInfo> getSetKeyInfo() {
        return setKeyInfo;
    }

    public void setSetKeyInfo(HashMap<String, SetInfo> setKeyInfo) {
        this.setKeyInfo = setKeyInfo;
    }

    public void addSetKey(String setKey, long postedTime, long dueDate) {
        SetInfo setInfo = new SetInfo(postedTime, dueDate);
        this.setKeyInfo.put(setKey, setInfo);
    }

    public void addSetKey(String setKey) {
        SetInfo setInfo = new SetInfo();
        this.setKeyInfo.put(setKey, setInfo);
    }

    public void removeSetKey(String setKey) {
        this.setKeyInfo.remove(setKey);
    }

    public void addSetInfo(String setKey, long postedTime, long dueDate) {
        SetInfo setInfo = new SetInfo(postedTime, dueDate);
        this.setKeyInfo.put(setKey, setInfo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(ctgKey);
        dest.writeInt(setKeyInfo.size());

        for (Map.Entry<String, SetInfo> entry : setKeyInfo.entrySet()) {
            dest.writeString(entry.getKey()); // Write the setKey
            dest.writeParcelable(entry.getValue(), flags); // Write the SetInfo object
        }
    }
}
