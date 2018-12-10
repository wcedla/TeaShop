package com.example.wcedla.selltea.adapter;

public class SortGridBean {

    public SortGridBean(int imageId,String title,int backgroundId)
    {
        this.imageId=imageId;
        this.title=title;
        this.backgroundId=backgroundId;
    }

    private int imageId;

    private String title;

    private int backgroundId;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBackgroundId(int backgroundId) {
        this.backgroundId = backgroundId;
    }

    public int getBackgroundId() {
        return backgroundId;
    }
}
