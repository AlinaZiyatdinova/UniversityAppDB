package com.example.zd5.DBUniversity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5.R

internal class UniversityAdapter(private var itemsList: List<University>) :
    RecyclerView.Adapter<UniversityAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemTextView_title: TextView = view.findViewById(R.id.textview_title)
        var itemTextView_web: TextView = view.findViewById(R.id.textview_web)
        var itemTextView_country: TextView = view.findViewById(R.id.textview_country)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_university, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.itemTextView_title.text = item.title.toString()
        holder.itemTextView_web.text = item.web.toString()
        holder.itemTextView_country.text = item.country.toString()
    }
    override fun getItemCount(): Int {
        return itemsList.size
    }
}