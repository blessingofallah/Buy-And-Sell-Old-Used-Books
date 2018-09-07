package com.baniya.pintooaggarwal.bookbuyselling.imagelib.model;

/**
 * Created by Pintoo Aggarwal on 4/1/2018.
 */

public class User {
    public String uid;
    public String email;
    public String firebaseToken;

    public User(){

    }

    public User(String uid, String email, String firebaseToken){
        this.uid = uid;
        this.email = email;
        this.firebaseToken = firebaseToken;
    }
}
