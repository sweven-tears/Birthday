package com.cdtc.birthday.util;

/**
 * Created by Sweven on 2018/9/23.
 * Email:sweventears@Foxmail.com
 */
public class BirthBean {
    private String name;
    private String birthday;
    private int imageStyle;

    public BirthBean(String name, String birthday, int imageStyle) {
        this.name = name;
        this.birthday = birthday;
        this.imageStyle = imageStyle;
    }

    public BirthBean() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getImageStyle() {
        return imageStyle;
    }

    public void setImageStyle(int imageStyle) {
        this.imageStyle = imageStyle;
    }

    @Override
    public String toString() {
        return "BirthBean{" +
                "name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", imageStyle=" + imageStyle +
                '}';
    }
}
