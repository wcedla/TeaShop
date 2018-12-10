package com.example.wcedla.selltea.adapter;

public class ConfirmToBuyBean {

    private String id;

    private String img;

    private String title;

    private String buyCount;

    private String totalPrice;

    public ConfirmToBuyBean(String id,String img,String title,String buyCount,String totalPrice)
    {
        this.id=id;
        this.img=img;
        this.title=title;
        this.buyCount=buyCount;
        this.totalPrice=totalPrice;
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

    public String getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(String buyCount) {
        this.buyCount = buyCount;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
