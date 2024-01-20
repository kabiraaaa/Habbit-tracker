package com.example.habbit

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.habbit.databinding.AddhabbitScreenBinding
import com.thebluealliance.spectrum.SpectrumPalette

class AddHabbitScreen: Fragment(), SpectrumPalette.OnColorSelectedListener {

    private lateinit var binding: AddhabbitScreenBinding
    //private var habitDatabase: HabitDatabase? = null
    //private var habitDao: HabitDAO? = null
    private var check: Boolean = false

    private lateinit var addHabbitViewModel: AddHabbitViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        //habitDatabase = HabitDatabase.getInstance(context!!)
        binding = DataBindingUtil.inflate(inflater, R.layout.addhabbit_screen, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = HabitDatabase.getInstance(application)!!.habitDAO
        val addHabbitViewModelFactory = AddHabbitViewModelFactory(dataSource,application)
        addHabbitViewModel = ViewModelProviders.of(this, addHabbitViewModelFactory).get(AddHabbitViewModel::class.java)
        binding.addHabbitViewModel = addHabbitViewModel
        binding.lifecycleOwner = this
        binding.addhabitButton.setOnClickListener { view: View ->
            when (binding.habitText.text.toString()) {
                "" -> Toast.makeText(context, "No name given", Toast.LENGTH_SHORT).show()
                else -> {
                    //addHabbitViewModel.name = binding.habitText.text.toString()
                    //addHabbitViewModel.track = binding.checkBox.isChecked
                    //val row = HabitBase(null,binding.habitText.text.toString(),check,this.color.toString(), 0, false)
                    //habitDatabase?.habitDAO()?.insertHabit(row)
                    addHabbitViewModel.addToDatabase(binding.habitText.text.toString(), binding.checkBox.isChecked)
                    var lista = dataSource.selectAllHabit()
                    println(lista)
                    view.findNavController().navigate(R.id.action_addHabbitScreen_to_mainScreen)
                }
            }
        }
        binding.colorPalette.setOnColorSelectedListener(this)
        return binding.root
    }

    override fun onColorSelected(@ColorInt color: Int) {
//        activity?.custom_toolbar?.setBackgroundColor(color)
        addHabbitViewModel.color = color
        activity?.window!!.statusBarColor = color
    }
}