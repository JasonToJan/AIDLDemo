package com.xingfu.aidlservicedemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description: 实体类
 *
 * @version V1.0.0
 * Createdate: 2018/6/22
 * @author: wja 1211241203@qq.com
 */
public class Person implements Parcelable {

    private String name;

    public Person(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    protected Person(Parcel in) {
        this.name = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
