package com.troy.cardhelper.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenlongfei on 16/1/27.
 */
public class VerCodeData implements Parcelable {
    private String code_type;
    private String img_content;
    private String img_type;
    private String t;

    public VerCodeData() {
    }

    public String getCode_type() {
        return code_type;
    }

    public void setCode_type(String code_type) {
        this.code_type = code_type;
    }

    public String getImg_content() {
        return img_content;
    }

    public void setImg_content(String img_content) {
        this.img_content = img_content;
    }

    public String getImg_type() {
        return img_type;
    }

    public void setImg_type(String img_type) {
        this.img_type = img_type;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code_type);
        dest.writeString(this.img_content);
        dest.writeString(this.img_type);
        dest.writeString(this.t);
    }

    protected VerCodeData(Parcel in) {
        this.code_type = in.readString();
        this.img_content = in.readString();
        this.img_type = in.readString();
        this.t = in.readString();
    }

    public static final Creator<VerCodeData> CREATOR = new Creator<VerCodeData>() {
        public VerCodeData createFromParcel(Parcel source) {
            return new VerCodeData(source);
        }

        public VerCodeData[] newArray(int size) {
            return new VerCodeData[size];
        }
    };
}
