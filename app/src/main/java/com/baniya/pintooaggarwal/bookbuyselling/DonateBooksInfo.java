package com.baniya.pintooaggarwal.bookbuyselling;

import java.io.Serializable;

/**
 * Created by Pintoo Aggarwal on 23-09-2017.
 */

public class DonateBooksInfo implements Serializable
{

    public String adBooksDetails;
    public String adBookQuantity;
    public String adName;
    public String adAddress;



    private String userId;
    private String  booksdetailid;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBooksdetailid() {
        return booksdetailid;
    }

    public void setBooksdetailid(String booksdetailid) {
        this.booksdetailid = booksdetailid;
    }

    public DonateBooksInfo()
    {

    }

    public DonateBooksInfo(String adBooksDetails, String adBookQuantity, String adName, String adAddress,
                           String adEmail, String adPhone, String adcategory) {
        this.adBooksDetails = adBooksDetails;
        this.adBookQuantity = adBookQuantity;
        this.adName = adName;
        this.adAddress = adAddress;
        this.adEmail = adEmail;
        this.adPhone = adPhone;
        this.adcategory = adcategory;
    }

    public String adEmail;
    public String adPhone;
    public String adcategory;



}
