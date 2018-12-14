package com.example.wcedla.selltea.gson;

import com.google.gson.annotations.SerializedName;

public class AllBillDetial {

    @SerializedName("id")
    public String billId;

    @SerializedName("buyadress")
    public String billAdress;

    @SerializedName("buyid")
    public String buyId;

    @SerializedName("buycount")
    public String buyCount;

    @SerializedName("mailprice")
    public String mailPrice;

    @SerializedName("buymessage")
    public String buyMessage;

    @SerializedName("billno")
    public String buyNo;

    @SerializedName("buildtime")
    public String buyDate;

    @SerializedName("flag")
    public String buyFlag;
}
