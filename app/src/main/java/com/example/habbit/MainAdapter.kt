package com.example.habbit

import android.graphics.Color
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.habbit.databinding.HabitItemBinding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import java.util.ArrayList

class MainAdapter(val habitList: List<HabitBase>, val fragment: MainScreen): RecyclerView.Adapter<MainAdapter.MainViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.MainViewHolder {
        val layout = R.layout.habit_item
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        //val layoutInflater = LayoutInflater.from(parent.context)
        //binding = DataBindingUtil.inflate(layoutInflater, layout, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainAdapter.MainViewHolder, position: Int) {
        holder.name.text = habitList[position].name

        holder.box.setChecked(habitList[position].resetMask)
        if (habitList[position].resetMask){
            holder.name.setTextColor(habitList[position].color.toInt())
        }

        if (habitList[position].tracking) {
            holder.streak.text = habitList[position].streak.toString()
        }
        else {
            holder.streak.text = "-"
        }
    }

    inner class MainViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val name = view.findViewById<TextView>(R.id.itemName)
        val streak = view.findViewById<TextView>(R.id.itemStreak)
        val box = view.findViewById<CheckBox>(R.id.itemCheck)

        init {
            box.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed){
                    fragment.boxChecked = isChecked
                    fragment.checkingBox(habitList[position].habitId!!.toLong())
                }
            }
            name.setOnClickListener {fragment.onItemSelect(habitList[position].habitId!!.toLong(),
                habitList[position].color.toInt(), habitList[position].name, habitList[position].tracking)}
        }
    }

    override fun getItemCount(): Int {
        return if(habitList!!.isEmpty()) 0 else habitList!!.size
    }
}