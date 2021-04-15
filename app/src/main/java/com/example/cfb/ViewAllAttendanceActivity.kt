package com.example.cfb

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cfb.MarkAttendanceAdapter
import com.example.cfb.models.BookingHistory
import com.example.cfb.models.attendance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*

class ViewAllAttendanceActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var viewAllAttendanceAdapter: ViewAllAttendanceAdapter
    lateinit var id: String
    lateinit var text: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_attendance)

        val fireStore = FirebaseFirestore.getInstance()

        id = intent.extras?.get("id") as String

        var list: MutableList<attendance> = mutableListOf()

        val goback: ImageView = findViewById(R.id.backB)
        text =  findViewById(R.id.text)

        goback.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }


        recyclerView = findViewById(R.id.recyclerHistory)
        viewAllAttendanceAdapter = ViewAllAttendanceAdapter(this, list)


        recyclerView.adapter = viewAllAttendanceAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)



        fetchToDoList()

    }

    private fun fetchToDoList() {
        doAsync {
            var list: MutableList<attendance> = mutableListOf()
            val firestore =  FirebaseFirestore.getInstance()
            firestore.collection("BookingHistory").document(id).collection("Attendees").get()
                .addOnSuccessListener {
                    for(document in it){
                        list.add(document.toObject(attendance::class.java))
                    }
                    (recyclerView.adapter as ViewAllAttendanceAdapter).notifyDataSetChanged()
                    text.text = "Count: " + list.size
                }
            uiThread {

                viewAllAttendanceAdapter.setList(list)
            }
        }
    }
}