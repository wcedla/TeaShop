package com.example.wcedla.selltea.gson;

import com.google.gson.annotations.SerializedName;

public class SearchShowDetial {

    @SerializedName("id")
    public String goodsId;

    public String img;

    public String title;

    @SerializedName("nowprice")
    public String nowPrice;

    @SerializedName("origincost")
    public String originCost;
}
