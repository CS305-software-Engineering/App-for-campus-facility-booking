package com.example.cfb.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

import android.util.Log
import com.example.cfb.R
import com.example.cfb.models.BookingHistory


import java.text.SimpleDateFormat
import java.util.*


class ViewBookingHistoryAdapter(var context: Context, var bookingList: MutableList<BookingHistory>):
    RecyclerView.Adapter<ViewBookingHistoryAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var slot: TextView = itemView.findViewById(R.id.slot)
        var name: TextView = itemView.findViewById(R.id.name)
        var date: TextView = itemView.findViewById(R.id.date)
        var building: TextView = itemView.findViewById(R.id.buildingname)
        var statusButton: Button = itemView.findViewById(R.id.statusButton)


    }
    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {

        holder.name.text = bookingList[position].facilityName
        holder.building.text = bookingList[position].building
        holder.slot.text = "Slot: " + bookingList[position].slot

        var type = ""
        var date1 = bookingList[position].date
        var slot = bookingList[position].slot
        val date2 = date1.substring(6,8) + date1.substring(4,6) + date1.substring(0,4)
        val firestore =  FirebaseFirestore.getInstance().collection(date2)
        firestore.document(bookingList[position].facilityName).get()
            .addOnSuccessListener {
                type = it.getString("type").toString()

                var currentDate = SimpleDateFormat("yyyyMMdd").format(Date())



                if(currentDate.toInt() == date1.toInt()){
                    var time = SimpleDateFormat("HHmmss").format(Date())
                    var currenthour = time.substring(0,2)
                    var startTime = ""
                    var endTime = ""
                    Log.d("curti",currenthour)
                    if(slot[5] == 'A'){
                        startTime = slot.substring(0,2)
                    }
                    else{
                        startTime = (slot.substring(0,2).toInt() + 12).toString()
                    }
                    if(startTime == "24"){
                        startTime = "12"
                    }
                    if(slot[15] == 'A'){
                        endTime = slot.substring(10,12)
                    }
                    else{
                        endTime = (slot.substring(10,12).toInt() + 12).toString()
                    }
                    if(endTime == "24"){
                        endTime = "12"
                    }
                    Log.d("startTime",startTime)
                    Log.d("endTime",endTime)

                    if(currenthour.toInt() < startTime.toInt()) {
                        holder.statusButton.text = "Upcoming"
                        holder.statusButton.setBackgroundColor(Color.BLUE)
                    }
                    else if(currenthour.toInt() >= startTime.toInt() && currenthour.toInt() < endTime.toInt()){
                        holder.statusButton.text = "OnGoing"
                        holder.statusButton.setBackgroundColor(Color.GREEN)
                    }
                }
                else if(currentDate.toInt() < date1.toInt()){
                    holder.statusButton.text = "UpComing"
                    holder.statusButton.setBackgroundColor(Color.BLUE)
                }
                var date2 = ""
                var j = 0
                var i =0
                var n = date1.length
                while(j<n) {
                    if (i == 4 || i == 7){
                        date2 = "$date2/"
                        i++
                    }
                    else{
                        date2 += date1[j]
                        j++
                        i++
                    }
                }
                Log.d("res",date2)

                var n1 = type.length
                type = type.substring(0,n1-1)

                holder.name.text = "$type: " + bookingList[position].facilityName
                holder.date.text = "Date: " + date2

            }



    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.booking_history_item,parent,false)
        return DetailsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    fun setList(list: MutableList<BookingHistory>){
        bookingList = list

        notifyDataSetChanged()
    }
}
