package com.example.todoapp


import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.todoapp.Model.ToDoModel
import com.example.todoapp.Utils.DatabaseHandler
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Objects

class AddNewTask : BottomSheetDialogFragment() {
    private var newTaskText: EditText? = null
    private var newTaskSaveButton: Button? = null
    private var db: DatabaseHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_task, container, false)
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newTaskText = view.findViewById(R.id.newTaskText)
        newTaskSaveButton = getView()!!.findViewById(R.id.newTaskButton)
        var isUpdate = false
        val bundle = arguments
        if (bundle != null) {
            isUpdate = true
            val task = bundle.getString("task")
            newTaskText!!.setText(task)
            assert(task != null)
            if (task!!.length > 0) newTaskSaveButton!!.setTextColor(
                ContextCompat.getColor(
                    Objects.requireNonNull(
                        context
                    )!!, R.color.colorPrimaryDark
                )
            )
        }
        db = DatabaseHandler(activity)
        db!!.openDatabase()
        newTaskText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() == "") {
                    newTaskSaveButton!!.setEnabled(false)
                    newTaskSaveButton!!.setTextColor(Color.GRAY)
                } else {
                    newTaskSaveButton!!.setEnabled(true)
                    newTaskSaveButton!!.setTextColor(
                        ContextCompat.getColor(
                            Objects.requireNonNull(
                                context
                            )!!, R.color.colorPrimaryDark
                        )
                    )
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        val finalIsUpdate = isUpdate
        newTaskSaveButton!!.setOnClickListener(View.OnClickListener {
            val text = newTaskText!!.getText().toString()
            if (finalIsUpdate) {
                db!!.updateTask(bundle!!.getInt("id"), text)
            } else {
                val task = ToDoModel()
                task.task = text
                task.status = 0
                db!!.insertTask(task)
            }
            dismiss()
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        val activity: Activity? = activity
        if (activity is DialogCloseListner) (activity as DialogCloseListner).handleDialogClose(
            dialog
        )
    }

    companion object {
        const val TAG = "ActionBottomDialog"
        @JvmStatic
        fun newInstance(): AddNewTask {
            return AddNewTask()
        }
    }
}