package com.example.todoapp


import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Adapter.ToDoAdapter
import com.example.todoapp.AddNewTask.Companion.newInstance
import com.example.todoapp.Model.ToDoModel
import com.example.todoapp.Utils.DatabaseHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Collections

class MainActivity : AppCompatActivity(), DialogCloseListner {
    private var tasksRecyclerView: RecyclerView? = null
    private var tasksAdapter: ToDoAdapter? = null
    private var fab: FloatingActionButton? = null
    private var taskList: List<ToDoModel?>? = null
    private var db: DatabaseHandler? = null

    //private View fab;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //getSupportActionBar().hide();
        db = DatabaseHandler(this)
        db!!.openDatabase()
        taskList = ArrayList()
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView!!.setLayoutManager(LinearLayoutManager(this))
        tasksAdapter = ToDoAdapter(db!!, this@MainActivity)
        tasksRecyclerView!!.setAdapter(tasksAdapter)
        fab = findViewById(R.id.fab)
        val itemTouchHelper = ItemTouchHelper(RecyclerItemTouchHelper(tasksAdapter!!))
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView)
        taskList = db!!.allTasks
        Collections.reverse(taskList)
        tasksAdapter!!.setTasks(taskList as List<ToDoModel>)
        fab!!.setOnClickListener(View.OnClickListener {
            newInstance().show(
                supportFragmentManager, AddNewTask.TAG
            )
        })
    }

    override fun handleDialogClose(dialog: DialogInterface?) {
        taskList = db!!.allTasks
        Collections.reverse(taskList)
        tasksAdapter!!.setTasks(taskList as List<ToDoModel>)
        tasksAdapter!!.notifyDataSetChanged()
    }
}
