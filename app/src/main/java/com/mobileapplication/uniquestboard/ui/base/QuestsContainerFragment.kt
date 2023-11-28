package com.mobileapplication.uniquestboard.ui.base

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mobileapplication.uniquestboard.GlobalVariables
import com.mobileapplication.uniquestboard.ui.common.Contact
import com.mobileapplication.uniquestboard.ui.common.Quest
import com.mobileapplication.uniquestboard.ui.common.Status
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

interface VolleyCallback {
    fun onSuccess(response: Quest)
    fun onError(error: String)
}
public enum class ContainerAction{
    GetByPublisher,
    GetByTaker,
    GetByCurStatus
}
open class QuestsContainer:Fragment() {
    open val TAG = "QuestsContainer"
    open lateinit var action:ContainerAction
    val numOfQuestsPerGet:Int = 10;
    @RequiresApi(Build.VERSION_CODES.O)
    val df = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
    @RequiresApi(Build.VERSION_CODES.O)
    val df_dateOnly = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    @RequiresApi(Build.VERSION_CODES.O)
    val df_timeOnly = DateTimeFormatter.ofPattern("HH:mm")


    //variable under is just for test
    val taker = mutableListOf<String>("someone")
    var contact = Contact("55556666","@admin")
    var status = Status.PENDING;
    val image = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    val testQuest = Quest(
        LocalDateTime.now(),
        LocalDateTime.now(),
        "else",
        taker,
        "the test title somehow the price is higher than expected 6666666666666666666666666666666666666666666666666666666666666666666" +
                "11111111111111111111111111111111111111111111111111111111111111111111111111111"
                ,
        "the test content tttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
                "555555555555555555555555555555555555555555555555555555555555555555555555555555555" +
                "888888888888888888888888888888888888888888888888888888888888888888888888888888888" +
                "111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr",
        status,
        image,
        "thankfulness",
        contact,
        "g"
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun getQuests(action: ContainerAction, publisherID:String?, receiverID:String?, curStatus: Status?):MutableList<Quest>{
        //根据条件查找一定数量（numOfQuestsPerGet）的quest，并返回list
        return when(action){
//            ContainerAction.GetByPublisher->{
//
//            }
//            ContainerAction.GetByTaker->{
//
//            }
//            ContainerAction.GetByCurStatus->{
//
//            }
            else->{
                Log.e(TAG,"Unknow Action!")
                var questList = mutableListOf<Quest>(testQuest)
                return questList
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getAQuest(questID: String?, callback: VolleyCallback){
        //get quest from database
        var rq = Volley.newRequestQueue(requireActivity().getApplicationContext());
        var url : String = "http://${GlobalVariables.ip}:${GlobalVariables.port}/android/DB_getDetails.php";
        var sr = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                val jsonObject: JSONObject = JSONObject(response.toString())
                for(i in 1..jsonObject.length())
                {
                    Log.i(i.toString(), jsonObject.getString(i.toString()))
                    val result = jsonObject.getString(i.toString()).toString()
                    val sub = result.substring(1, result.length - 1);
                    val js: JSONObject = JSONObject(sub)
                    Log.i(i.toString(), js.getString("title"));
                    val taker = mutableListOf<String>()
                    taker.add("someone")
                    var ct = js.getString("contact");
                    var ctsub = ct.substring(0, 2);
                    var contact : Contact;
                    if(ctsub == "IG")
                    {
                        contact = Contact(null, js.getString("contact"))
                    }
                    else
                    {
                        contact = Contact(js.getString("contact"), null)
                    }
                    var status = Status.COMPLETED;
                    var quest = Quest(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        js.getString("publisher"),
                        taker,
                        js.getString("title"),
                        js.getString("description"),
                        status,
                        image,
                        js.getString("reward"),
                        contact,
                        js.getString("order_id")
                    )
                    callback.onSuccess(quest)
                }

                Toast.makeText(requireActivity().getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show(); },
            Response.ErrorListener { e -> callback.onError(e.toString()); Log.e("w", e.toString()); Toast.makeText(requireActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show() })
        {
            override fun getParams(): MutableMap<String, String>? {
                var params = HashMap<String, String>();
                params.put("order_id", questID!!);
                return params;
            }
        }
        rq.add(sr);
    }
}