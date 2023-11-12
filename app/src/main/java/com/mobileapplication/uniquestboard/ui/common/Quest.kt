package com.mobileapplication.uniquestboard.ui.common

import com.mobileapplication.uniquestboard.ui.base.Identifiable
import java.time.LocalDateTime

public enum class Status{
    PENDING,      // 待接取
    IN_PROGRESS,  // 正在进行
    COMPLETED,    // 完成
    FAILED,       // 失败 //接取但接受人未完成任务
    EXPIRED,      // 过期 //到过期时间仍没有被接取
    INTERRUPTED   // 中断 //接取但发布者取消任务
}

public class Contact(
    whatsapp:String,
    instagram:String,
){
    var whatsapp:String = whatsapp;
    var instagram:String = instagram;
}

public class Quest(
    publishTime : LocalDateTime,
    expiredTime : LocalDateTime,
    publisher:String,
    taker:MutableList<String>,
    title:String,
    content:String,
    status: Status,
    images:MutableList<String>,
    reward:String,
    contact: Contact,
): Identifiable(){

    val publishTime:LocalDateTime = publishTime;
    val expiredTime:LocalDateTime = expiredTime;
    val publisher = publisher;
    var taker = taker;
    var title = title;
    var content = content;
    var status = status;
    var images = images;
    var reward = reward;
    var contact = contact;

}