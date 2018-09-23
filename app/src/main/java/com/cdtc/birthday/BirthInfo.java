package com.cdtc.birthday;

/**
 * Created by Sweven on 2018/9/23.
 * Email:sweventears@Foxmail.com
 */
public class BirthInfo {
    private String name;
    private String birthday;
    private int imageStyle;

    BirthInfo(String name, String birthday, int imageStyle) {
        this.name = name;
        this.birthday = birthday;
        this.imageStyle = imageStyle;
    }

    BirthInfo() {

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
}
