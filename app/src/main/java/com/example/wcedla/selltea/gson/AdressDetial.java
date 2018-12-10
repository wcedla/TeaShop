package com.example.wcedla.selltea.gson;

import com.google.gson.annotations.SerializedName;

public class AdressDetial {

    public String id;

    @SerializedName("username")
    public String userName;

    @SerializedName("adressname")
    public String adressName;

    @SerializedName("adressphone")
    public String adressPhone;

    @SerializedName("adresstext")
    public String adressText;
}
