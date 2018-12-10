package com.example.wcedla.selltea.database;

import org.litepal.crud.LitePalSupport;

public class BannerHotImageTable extends LitePalSupport {

    private String imgResource;

    private String title;

    public String getImgResource() {
        return imgResource;
    }

    public void setImgResource(String imgResource) {
        this.imgResource = imgResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
