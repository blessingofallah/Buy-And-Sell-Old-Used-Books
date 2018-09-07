package com.baniya.pintooaggarwal.bookbuyselling;

import java.io.Serializable;

/**
 * Created by Pintoo Aggarwal on 04-09-2017.
 */

public class ProfileInfo implements Serializable {
    public String ProfileName;
    public String ProfileAddress;

    public String ProfilePhone;
    public String ProfileLocation;
    //private String imageUrl;
    private String userId;
    private String AdId;
//    public String getImageUrl() {
//        return imageUrl;
//    }

//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }


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

//    public ProfileInfo(String profileLocation)
//    {
//
//        this.ProfileLocation = profileLocation;
//    }

    public ProfileInfo(String profileName, String profileAddress, String profilePhone, String profileLocation) {
        this.ProfileName = profileName;
        this.ProfileAddress= profileAddress;
        this.ProfilePhone = profilePhone;
        this.ProfileLocation = profileLocation;
    }
}
