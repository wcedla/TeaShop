package com.example.wcedla.selltea.adapter;

import java.util.List;

public class AllBillShowBean {

    private String billId;

    private String billStatus;

    private List<ConfirmToBuyBean> goodsBeanList;

    public AllBillShowBean(String billId,String billStatus,List<ConfirmToBuyBean> goodsBeanList)
    {
        this.billId=billId;
        this.billStatus=billStatus;
        this.goodsBeanList=goodsBeanList;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public List<ConfirmToBuyBean> getGoodsBeanList() {
        return goodsBeanList;
    }

    public void setGoodsBeanList(List<ConfirmToBuyBean> goodsBeanList) {
        this.goodsBeanList = goodsBeanList;
    }
}
