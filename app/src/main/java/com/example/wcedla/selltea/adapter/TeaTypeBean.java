package com.example.wcedla.selltea.adapter;

public class TeaTypeBean {

    private String name;

    private String img;

    public String getName() {
        return name;
    }

    public TeaTypeBean(String name,String img)
    {
        this.name=name;
        this.img=img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
