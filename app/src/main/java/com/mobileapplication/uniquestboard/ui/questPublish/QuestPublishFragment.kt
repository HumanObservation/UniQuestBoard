package com.mobileapplication.uniquestboard.ui.questPublish

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.mobileapplication.uniquestboard.GlobalVariables
import com.mobileapplication.uniquestboard.R
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mobileapplication.uniquestboard.GlobalVariables
import com.mobileapplication.uniquestboard.databinding.FragmentQuestPublishBinding
import com.mobileapplication.uniquestboard.ui.base.QuestsContainer
import com.mobileapplication.uniquestboard.ui.common.Contact
import com.mobileapplication.uniquestboard.ui.common.Quest
import com.mobileapplication.uniquestboard.ui.common.Status
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Locale

interface VolleyCallback {
    fun onSuccess(response: Quest)
    fun onError(error: String)
}
class QuestPublishFragment : QuestsContainer() {

    companion object {
        fun newInstance() = QuestPublishFragment()
    }

    val viewModel: QuestPublishViewModel by viewModels()
    override val TAG:String = "QuestPublishFragment"
    private var _binding: FragmentQuestPublishBinding? = null
    public val binding get() = _binding!!
    private lateinit var newQuest:Quest
    private lateinit var newQuestContact:Contact
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestPublishBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpContact()
        setUpDateTimeEditText()
        setUpEditTexts()
        setUpPublishButton()
    }

    fun setUpContact(){
        val whatsappCheckBox = binding.includeQuestPublishForm.whatsappCheckBox
        val whatsappInputField = binding.includeQuestPublishForm.whatsappInputField
        val instagramCheckBox = binding.includeQuestPublishForm.instagramCheckBox
        val instagramInputField = binding.includeQuestPublishForm.instagramInputField
        whatsappCheckBox.setOnClickListener(){check->
            whatsappInputField.isEnabled = whatsappCheckBox.isChecked
            instagramCheckBox.isEnabled = !whatsappCheckBox.isChecked;
        }
        instagramCheckBox.setOnClickListener(){check->
            instagramInputField.isEnabled = instagramCheckBox.isChecked
            whatsappCheckBox.isEnabled = !instagramCheckBox.isChecked;
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpDateTimeEditText() {
        val dateEditText = binding.includeQuestPublishForm.dateEditText;
        val timeEditText = binding.includeQuestPublishForm.timeEditText
        val defaultExpireTime = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")).plusDays(1)
        viewModel.expireTime = defaultExpireTime
        val curDatePlusOneDay = df_dateOnly.format(defaultExpireTime)
        dateEditText.setText(curDatePlusOneDay)
        viewModel.selectedDate = curDatePlusOneDay
        val curTime = df_timeOnly.format(defaultExpireTime)
        timeEditText.setText(curTime)
        viewModel.selectedTime = curTime
        dateEditText.setOnTouchListener{ _, event ->
            if (event?.action == MotionEvent.ACTION_UP) {
                // 在这里显示你自定义的对话框
                showDateDialog(dateEditText)
                // 返回 true 表示触摸事件被消耗，不再传递给其他监听器
                true
            } else {
                // 返回 false 表示不处理此事件，继续传递给其他监听器
                false
            }
        }

        timeEditText.setOnTouchListener{ _, event ->
            if (event?.action == MotionEvent.ACTION_UP) {
                // 在这里显示你自定义的对话框
                showTimeDialog(timeEditText)
                // 返回 true 表示触摸事件被消耗，不再传递给其他监听器
                true
            } else {
                // 返回 false 表示不处理此事件，继续传递给其他监听器
                false
            }
        }
    }

    private fun showDateDialog(dateEditText:EditText){
        val ca = Calendar.getInstance()
        val datePicker = context?.let {
            DatePickerDialog(
                it,
                { _, year, month, dayOfMonth ->
                    // 处理用户选择的日期
                    ca.set(Calendar.YEAR, year)
                    ca.set(Calendar.MONTH, month)
                    ca.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                    val selectedDate = sdf.format(ca.time)
                    viewModel.selectedDate = selectedDate;
                    dateEditText.setText(viewModel.selectedDate)
                },
                ca.get(Calendar.YEAR),
                ca.get(Calendar.MONTH),
                ca.get(Calendar.DAY_OF_MONTH)
            )
        }
        datePicker?.datePicker?.minDate = ca.timeInMillis
        datePicker?.show()
    }

    private fun showTimeDialog(timeEditText:EditText){
        val ca = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            ca.set(Calendar.HOUR_OF_DAY, hour)
            ca.set(Calendar.MINUTE, minute)
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val selectedTime = sdf.format(ca.time)
            viewModel.selectedTime = selectedTime
            timeEditText.setText(viewModel.selectedTime)
        }

        TimePickerDialog(requireContext(), timeSetListener, ca.get(Calendar.HOUR_OF_DAY), ca.get(Calendar.MINUTE), true).show()
    }
    private fun setUpEditTexts(){
        val editTextToViewModelValMap: MutableMap<EditText, String> =
            mutableMapOf(
                binding.includeQuestPublishForm.title to viewModel.title,
                binding.includeQuestPublishForm.content to viewModel.content,
                binding.includeQuestPublishForm.reward to viewModel.reward,
                binding.includeQuestPublishForm.whatsappInputField to viewModel.whatsapp,
                binding.includeQuestPublishForm.instagramInputField to viewModel.instagram
            )
        binding.includeQuestPublishForm.title.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.title = s.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.includeQuestPublishForm.content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.content = s.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        viewModel.reward = binding.includeQuestPublishForm.reward.text.toString()
        binding.includeQuestPublishForm.reward.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.reward = s.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.includeQuestPublishForm.whatsappInputField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.whatsapp = s.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding.includeQuestPublishForm.instagramInputField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.instagram = s.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding.includeQuestPublishForm.dateEditText.addTextChangedListener(object : TextWatcher {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun afterTextChanged(s: Editable?) {
                viewModel.expireTime = LocalDateTime.parse(viewModel.selectedDate + " " + viewModel.selectedTime,df)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.includeQuestPublishForm.timeEditText.addTextChangedListener(object : TextWatcher {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun afterTextChanged(s: Editable?) {
                viewModel.expireTime = LocalDateTime.parse(viewModel.selectedDate + " " + viewModel.selectedTime,df)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpPublishButton(){
        binding.includeQuestPublishForm.publishButton.setOnClickListener(){
            if(!generateQuest()) return@setOnClickListener
            quest = generateQuest()
            var json = quest.serializeQuest()

            Log.d(TAG,json)
            var rq = Volley.newRequestQueue(requireActivity().applicationContext);
            var url : String = "http://${GlobalVariables.ip}:${GlobalVariables.port}/android/DB_QuestPublish.php";
            var sr = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response -> Log.i("t", response.toString()); },
                Response.ErrorListener { e -> Log.e("e", e.toString()) })
            {
                override fun getParams(): MutableMap<String, String>? {
                    var params = HashMap<String, String>();
                    params.put("itsc", GlobalVariables.user.itsc);
                    params.put("title", quest.title);
                    params.put("description", quest.content);
                    params.put("publisher", GlobalVariables.user.itsc);
                    params.put("publish_date", quest.publishTime.toString());
                    params.put("expired_date", quest.expiredTime.toString());
                    if(quest.contact.whatsapp == null)
                    {
                        params.put("contact", quest.contact.instagram!!);
                    }
                    else
                    {
                        params.put("contact", quest.contact.whatsapp!!);
                    }
                    params.put("reward", quest.reward);
                    return params;
                }
            }
            rq.add(sr);
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public fun generateQuest() : Boolean{
        if(!generateContact()) return false
        if(viewModel.title.isNullOrBlank()){
            Log.i(TAG,"Title is empty!")
            Toast.makeText(context,"Title is empty!",Toast.LENGTH_LONG).show();
            return false;
        }
        if(viewModel.content.isNullOrBlank()){
            Log.i(TAG,"Content is empty!")
            Toast.makeText(context,"Content is empty!",Toast.LENGTH_LONG).show();
            return false;
        }
        if(viewModel.reward.isNullOrBlank()){
            Log.i(TAG,"Reward is empty!")
            Toast.makeText(context,"Reward is empty!",Toast.LENGTH_LONG).show();
            return false;
        }
        newQuest = Quest(
            LocalDateTime.now(),
            viewModel.expireTime,
            GlobalVariables.user.itsc,
            mutableListOf(),
            viewModel.title,
            viewModel.content,
            Status.PENDING,
            mutableListOf(),
            viewModel.reward,
            contact,
            "h")
        return newQuest;
    }

    public fun generateContact():Boolean{
        val whatsappCheckBox = binding.includeQuestPublishForm.whatsappCheckBox
        val whatsappInputField = binding.includeQuestPublishForm.whatsappInputField
        val instagramCheckBox = binding.includeQuestPublishForm.instagramCheckBox
        val instagramInputField = binding.includeQuestPublishForm.instagramInputField
        newQuestContact = Contact(null,null)
        if(whatsappCheckBox.isChecked){
            if(whatsappInputField.text.isNullOrBlank()){
                Log.i(TAG,"whatsapp checked but is empty!")
                Toast.makeText(context,"whatsapp checked but is empty!",Toast.LENGTH_LONG).show();
                return false;
            }
            newQuestContact.whatsapp = whatsappInputField.text.toString()
        }
        if(instagramCheckBox.isChecked){
            if(instagramInputField.text.isNullOrBlank()){
                Log.i(TAG,"instagram checked but is empty!")
                Toast.makeText(context,"instagram checked but is empty!",Toast.LENGTH_LONG).show();
                return false;
            }
            newQuestContact.instagram = instagramInputField.text.toString()
        }
        if(!whatsappCheckBox.isChecked && !instagramCheckBox.isChecked){
            Log.i(TAG,"You have to provide at least one contact information!")
            Toast.makeText(this.requireContext(),"You have to provide at least one contact information!",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}