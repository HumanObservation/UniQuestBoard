package com.mobileapplication.uniquestboard

import com.mobileapplication.uniquestboard.ui.common.Identifiable

public class User(
    itsc:String,
    pwdHash:String,
    profileImage:String,
): Identifiable(){
    val itsc = itsc;
    val pwdHash = pwdHash;
    val profileImage = profileImage;
}