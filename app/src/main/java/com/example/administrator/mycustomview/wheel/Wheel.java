package com.example.administrator.mycustomview.wheel;


/**
 * Created by shake on 17-6-1.
 * 自定义轮播图的bean类，这只是暂时用这个bean类代替。假如是要真用到项目中，还是需要重新创建别的bean。然后替换掉
 */
public class Wheel {

    private int imageResource;

    private String des;

    public Wheel() {
    }

    public Wheel(int imageResource, String des) {
        this.imageResource = imageResource;
        this.des = des;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }


}
