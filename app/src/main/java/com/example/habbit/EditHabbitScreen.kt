package com.example.habbit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.thebluealliance.spectrum.SpectrumPalette
import com.example.habbit.databinding.AddhabbitScreenBinding


class EditHabbitScreen: Fragment(), SpectrumPalette.OnColorSelectedListener {

    private lateinit var binding: AddhabbitScreenBinding
    private lateinit var editHabbitViewModel: AddHabbitViewModel
    private lateinit var bundle : Bundle
    private var id: Long? = 0
    private var name: String? = ""
    private var colorFromBundle: Int? = 0
    private var tracking: Boolean? = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.addhabbit_screen, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = HabitDatabase.getInstance(application)!!.habitDAO
        val addHabbitViewModelFactory = AddHabbitViewModelFactory(dataSource,application)

        id = arguments?.getLong("id")
        name = arguments?.getString("name")
        colorFromBundle = arguments?.getInt("color")
        tracking = arguments?.getBoolean("tracking")

        binding.habitText.setText(name)
        binding.checkBox.setChecked(tracking!!)

        editHabbitViewModel = ViewModelProviders.of(this, addHabbitViewModelFactory).get(AddHabbitViewModel::class.java)
        binding.addHabbitViewModel = editHabbitViewModel
        binding.setLifecycleOwner(this)

        binding.addhabitButton.setOnClickListener { view: View ->
            when (binding.habitText.text.toString()) {
                "" -> Toast.makeText(context, "No name given", Toast.LENGTH_SHORT).show()
                else -> {
                    //addHabbitViewModel.name = binding.habitText.text.toString()
                    //addHabbitViewModel.track = binding.checkBox.isChecked
                    //val row = HabitBase(null,binding.habitText.text.toString(),check,this.color.toString(), 0, false)
                    //habitDatabase?.habitDAO()?.insertHabit(row)

                    name = binding.habitText.text.toString()
                    tracking = binding.checkBox.isChecked
                    colorFromBundle = editHabbitViewModel.color

                    editHabbitViewModel.updateHabitItem(id!!, binding.habitText.text.toString(), binding.checkBox.isChecked)
                    var lista = dataSource.selectAllHabit()
                    println(lista)

                    val itemFragment = ItemScreen()
                    bundle = Bundle()
                    bundle.putLong("id", id!!)
                    bundle.putString("name", name)
                    bundle.putBoolean("tracking", tracking!!)
                    bundle.putInt("color", colorFromBundle!!)

                    itemFragment.arguments = bundle

                    view.findNavController().navigate(R.id.action_editHabbitScreen2_to_mainScreen, bundle)
                }//{Navigation.createNavigateOnClickListener(R.id.action_addHabbitScreen_to_mainScreen)}
            }
        }

        binding.colorPalette.setOnColorSelectedListener(this)
        return binding.root
    }

    override fun onColorSelected(@ColorInt color: Int) {
//        activity?.custom_toolbar?.setBackgroundColor(color)
        editHabbitViewModel.color = color
        activity?.window!!.statusBarColor = color
        //this.color = color
        //Toast.makeText(context, Integer.toHexString(color), Toast.LENGTH_LONG).show()
    }
}