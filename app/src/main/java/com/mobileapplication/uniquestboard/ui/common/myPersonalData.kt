package com.mobileapplication.uniquestboard.ui.common

class myPersonalData(id : String, name : String) {
    var user_id : String get() {return user_id};
    var user_name : String get() {return user_name};

    init {
        user_id = id;
        user_name = name;
    }
}