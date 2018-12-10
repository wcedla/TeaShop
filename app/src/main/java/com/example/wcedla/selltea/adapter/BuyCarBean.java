package com.example.wcedla.selltea.adapter;

public class BuyCarBean {

    private String id;

    private String img;

    private String title;

    private String count;

    private String price;

    public BuyCarBean(String id,String img,String title,String count,String price)
    {
        this.id=id;
        this.img=img;
        this.title=title;
        this.count=count;
        this.price=price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
