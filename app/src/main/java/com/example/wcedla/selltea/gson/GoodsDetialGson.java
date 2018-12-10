package com.example.wcedla.selltea.gson;

import com.google.gson.annotations.SerializedName;

public class GoodsDetialGson {

    @SerializedName("id")
    public String goodsId;

    public String img1;

    public String img2;

    public String img3;

    public String img4;

    @SerializedName("name")
    public String goodsName;

    @SerializedName("nowprice")
    public String nowPrice;

    @SerializedName("originprice")
    public String originCost;

    @SerializedName("mailprice")
    public String mailPrice;

    @SerializedName("salecount")
    public String sellCount;

    @SerializedName("longdetial")
    public String longDetialImgs;
}
