package com.example.wcedla.selltea.adapter;

public class AdressBean {

    private String id;

    private String adressName;

    private String adressPhone;

    private String adressText;

    public AdressBean(String id,String adressName,String adressPhone,String adressText)
    {
        this.id=id;
        this.adressName=adressName;
        this.adressPhone=adressPhone;
        this.adressText=adressText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdressName() {
        return adressName;
    }

    public void setAdressName(String adressName) {
        this.adressName = adressName;
    }

    public String getAdressPhone() {
        return adressPhone;
    }

    public void setAdressPhone(String adressPhone) {
        this.adressPhone = adressPhone;
    }

    public String getAdressText() {
        return adressText;
    }

    public void setAdressText(String adressText) {
        this.adressText = adressText;
    }
}
