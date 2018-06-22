package com.xf.test1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description: 简单实体类
 *
 * @version V1.0.0
 * Createdate: 2018/6/22
 * @author: wja 1211241203@qq.com
 */
public class Entity implements Parcelable {

    private String name;

    private int parameter1;

    private double parameter2;

    public Entity(String name){
        this.name=name;
    }

    public Entity(String name, int parameter1, double parameter2) {
        this.name = name;
        this.parameter1 = parameter1;
        this.parameter2 = parameter2;
    }

    public int getParameter1() {
        return parameter1;
    }

    public void setParameter1(int parameter1) {
        this.parameter1 = parameter1;
    }

    public double getParameter2() {
        return parameter2;
    }

    public void setParameter2(double parameter2) {
        this.parameter2 = parameter2;
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
        dest.writeInt(this.parameter1);
        dest.writeDouble(this.parameter2);
    }

    protected Entity(Parcel in) {
        this.name = in.readString();
        this.parameter1 = in.readInt();
        this.parameter2 = in.readDouble();
    }

    public static final Creator<Entity> CREATOR = new Creator<Entity>() {
        @Override
        public Entity createFromParcel(Parcel source) {
            return new Entity(source);
        }

        @Override
        public Entity[] newArray(int size) {
            return new Entity[size];
        }
    };

    @Override
    public String toString() {
        return "Entity{" +
                "name='" + name + '\'' +
                ", parameter1=" + parameter1 +
                ", parameter2=" + parameter2 +
                '}';
    }
}
