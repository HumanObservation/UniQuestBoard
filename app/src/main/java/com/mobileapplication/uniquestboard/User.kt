package com.mobileapplication.uniquestboard

public class User(
    itsc:String,
    pwdHash:String,
    profileImage:String,
): Identifiable(){
    val itsc = itsc;
    val pwdHash = pwdHash;
    val profileImage = profileImage;
}