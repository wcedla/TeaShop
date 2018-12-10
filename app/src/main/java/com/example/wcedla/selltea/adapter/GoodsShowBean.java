package com.example.wcedla.selltea.adapter;

public class GoodsShowBean {

    private String goodsId;

    private String goodsImg;

    private String goodsTitle;

    private String nowPrice;

    private String originCost;

    public GoodsShowBean(String goodsId,String goodsImg,String goodsTitle,String nowPrice,String originCost)
    {
        this.goodsId=goodsId;
        this.goodsImg=goodsImg;
        this.goodsTitle=goodsTitle;
        this.nowPrice=nowPrice;
        this.originCost=originCost;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(String nowPrice) {
        this.nowPrice = nowPrice;
    }

    public String getOriginCost() {
        return originCost;
    }

    public void setOriginCost(String originCost) {
        this.originCost = originCost;
    }
}
