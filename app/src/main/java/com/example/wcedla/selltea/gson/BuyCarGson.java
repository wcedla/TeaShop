package com.example.wcedla.selltea.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BuyCarGson {

    @SerializedName("buycar")
    public List<BuyCarDetial> buyCarDetialList;
}
