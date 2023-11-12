package com.mobileapplication.uniquestboard.ui.questPublish

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.mobileapplication.uniquestboard.databinding.FragmentQuestPublishBinding
import com.mobileapplication.uniquestboard.ui.base.QuestsContainer
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

class QuestPublishFragment : QuestsContainer() {

    companion object {
        fun newInstance() = QuestPublishFragment()
    }

    private val viewModel: QuestPublishViewModel by viewModels()
    override val TAG:String = "QuestDetailFragment"
    private var _binding: FragmentQuestPublishBinding? = null
    private val binding get() = _binding!!

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
        setUpDateEditText()
    }

    fun setUpContact(){
        val whatsappCheckBox = binding.includeQuestPublishForm.whatsappCheckBox
        val whatsappInputField = binding.includeQuestPublishForm.whatsappInputField
        val instagramCheckBox = binding.includeQuestPublishForm.instagramCheckBox
        val instagramInputField = binding.includeQuestPublishForm.InstagramInputField
        whatsappCheckBox.setOnClickListener(){check->
            whatsappInputField.isEnabled = whatsappCheckBox.isChecked
        }
        instagramCheckBox.setOnClickListener(){check->
            instagramInputField.isEnabled = instagramCheckBox.isChecked
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpDateEditText() {
        val dateEditText = binding.includeQuestPublishForm.dateEditText;
        val timeEditText = binding.includeQuestPublishForm.timeEditText
        val curDatePlusOneDay = df_dateOnly.format(LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")).plusDays(1))
        dateEditText.setText(curDatePlusOneDay)
        viewModel.selectedDate = curDatePlusOneDay
        val curTime = df_timeOnly.format(LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")))
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
        var mYear = ca[Calendar.YEAR]
        var mMonth = ca[Calendar.MONTH]
        var mDay = ca[Calendar.DAY_OF_MONTH]
        val datePicker = context?.let {
            DatePickerDialog(
                it,
                { _, year, month, dayOfMonth ->
                    // 处理用户选择的日期
                    mYear = year
                    mMonth = month
                    mDay = dayOfMonth
                    val selectedDate = "$year/${month + 1}/$dayOfMonth"
                    dateEditText.setText(selectedDate)
                    viewModel.selectedDate = selectedDate;
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

    }
}