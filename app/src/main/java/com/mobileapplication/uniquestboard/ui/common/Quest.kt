package com.mobileapplication.uniquestboard.ui.common
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.util.UUID
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.time.format.DateTimeFormatter

enum class Status {
    PENDING,      // 待接取
    IN_PROGRESS,  // 正在进行
    COMPLETED,    // 完成
    FAILED,       // 失败 //接取但接受人未完成任务
    EXPIRED,      // 过期 //到过期时间仍没有被接取
    INTERRUPTED   // 中断 //接取但发布者取消任务
}

data class Contact(
    var whatsapp: String?,
    var instagram: String?,
) : java.io.Serializable

data class Quest(
    var publishTime: LocalDateTime,
    var expiredTime: LocalDateTime,
    var publisher: String,
    var taker: MutableList<String>,
    var title: String,
    var content: String,
    var status: Status,
    var images: MutableList<String>,
    var reward: String,
    var contact: Contact,
    val questID: UUID? = UUID.randomUUID(),
) : java.io.Serializable{
    fun serializeQuest():String{
        var serializedQuest = SerializedQuest(
            publishTime.toString(),
            expiredTime.toString(),
            publisher,
            taker,
            title,
            content,
            status.toString(),
            images,
            reward,
            contact,
            questID.toString()
        )
        val gson = Gson()
//        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val json: String = gson.toJson(serializedQuest)
//        var json = Json.encodeToString(serializedQuest)
        return json
    }
}
data class SerializedQuest(
    var publishTime: String,
    var expiredTime: String,
    var publisher: String,
    var taker: MutableList<String>,
    var title: String,
    var content: String,
    var status: String,
    var images: MutableList<String>,
    var reward: String,
    var contact: Contact,
    val questID: String

): java.io.Serializable{
    @RequiresApi(Build.VERSION_CODES.O)
    fun deserializeQuest(){
        val curStatus = Status.valueOf(status)
        var quest = Quest(
            LocalDateTime.parse(publishTime),
            LocalDateTime.parse(expiredTime),
            publisher,
            taker,
            title,
            content,
            curStatus,
            images,
            reward,
            contact,
            UUID.fromString(questID)
        )
        Log.d("@@@",quest.publishTime.toString())
    }
}