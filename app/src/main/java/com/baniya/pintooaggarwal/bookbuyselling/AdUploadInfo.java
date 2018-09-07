package com.baniya.pintooaggarwal.bookbuyselling;

import java.io.Serializable;

/**
 * Created by Pintoo Aggarwal on 30-08-2017.
 */

public class AdUploadInfo implements Serializable {

    public String adTitle;
    public String adPrice;
    public String adDescription;
    public String adName;
    public String adlocation;

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public String getAdEmail() {
        return adEmail;
    }

    public void setAdEmail(String adEmail) {
        this.adEmail = adEmail;
    }

    public String adEmail;
    public String adPhone;
    public String adcategory;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String imageUrl;
    private String userId;
    private String AdId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAdId() {
        return AdId;
    }

    public void setAdId(String adId) {
        AdId = adId;
    }

    public AdUploadInfo() {

    }
    public AdUploadInfo(String title, String price,String description,
                        String name,String email,String phone,String location,String category) {

        this.adTitle= title;
        this.adPrice = price;
        this.adDescription = description;

        this.adName = name;
        this.adEmail = email;
        this.adPhone = phone;
        this.adlocation = location;
        this.adcategory= category;

    }



}
