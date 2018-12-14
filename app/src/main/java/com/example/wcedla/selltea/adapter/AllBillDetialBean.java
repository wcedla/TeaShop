package com.example.wcedla.selltea.adapter;

public class AllBillDetialBean {

    private String billId;

    private String billAdress;

    private String buyId;

    private String buyCount;

    private String mailPrice;

    private String buyMessage;

    private String buyNo;

    private String buyDate;

    private String buyFlag;

    public AllBillDetialBean(String billId,String billAdress,String buyId,String buyCount,String mailPrice,String buyMessage,String buyNo,String buyDate,String buyFlag)
    {
        this.billId=billId;
        this.billAdress=billAdress;
        this.buyId=buyId;
        this.buyCount=buyCount;
        this.mailPrice=mailPrice;
        this.buyMessage=buyMessage;
        this.buyNo=buyNo;
        this.buyDate=buyDate;
        this.buyFlag=buyFlag;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillAdress() {
        return billAdress;
    }

    public void setBillAdress(String billAdress) {
        this.billAdress = billAdress;
    }

    public String getBuyId() {
        return buyId;
    }

    public void setBuyId(String buyId) {
        this.buyId = buyId;
    }

    public String getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(String buyCount) {
        this.buyCount = buyCount;
    }

    public String getMailPrice() {
        return mailPrice;
    }

    public void setMailPrice(String mailPrice) {
        this.mailPrice = mailPrice;
    }

    public String getBuyMessage() {
        return buyMessage;
    }

    public void setBuyMessage(String buyMessage) {
        this.buyMessage = buyMessage;
    }

    public String getBuyNo() {
        return buyNo;
    }

    public void setBuyNo(String buyNo) {
        this.buyNo = buyNo;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getBuyFlag() {
        return buyFlag;
    }

    public void setBuyFlag(String buyFlag) {
        this.buyFlag = buyFlag;
    }
}
