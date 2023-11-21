package com.mobileapplication.uniquestboard

import java.util.UUID

public class User(
    val userID: UUID? = UUID.randomUUID(),
    itsc:String,
    pwdHash:String,
    profileImage:String,
){
    val itsc = itsc;
    val pwdHash = pwdHash;
    val profileImage = profileImage;
}