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
                    var taker : String?;
                    if(js.has("taker"))
                    {
                        taker = js.getString("taker");
                    }
                    else
                    {
                        taker = null;
                    }
                    var quest = Quest(
                        LocalDateTime.parse(js.getString("publish_date"),df),
                        LocalDateTime.parse(js.getString("expired_date"),df),
                        js.getString("publisher"),
                        taker,
                        js.getString("title"),
                        js.getString("description"),
                        enumValues<Status>().firstOrNull { it.ordinal == js.getString("status").toInt() }!!,
                        image,
                        js.getString("reward"),
                        contact,
                        js.getString("order_id")
                    )
                    callback.onSuccess(quest)
                }},
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