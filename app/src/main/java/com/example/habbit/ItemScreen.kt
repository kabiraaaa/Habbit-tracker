package com.example.habbit

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.habbit.databinding.ItemScreenBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class ItemScreen: Fragment(){

    private lateinit var binding: ItemScreenBinding
    private lateinit var itemScreenViewModel: ItemScreenViewModel
    private var id: Long? = 0
    private var name: String? = ""
    private var color: Int? = 0
    private var currentDate: String = ""
    private var tracking: Boolean? = false
    private lateinit var bundle : Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.item_screen, container, false)

        val application = requireNotNull(this.activity).application
        val base = HabitDatabase.getInstance(application)
        val dataSourceHabit = base!!.habitDAO
        val dataSourceJoined = base!!.joinedDAO

        val editFragment = EditHabbitScreen()
        bundle = Bundle()
        bundle.putAll(arguments)
        editFragment.arguments = bundle

        id = arguments?.getLong("id")
        name = arguments?.getString("name")
        color = arguments?.getInt("color")
        currentDate = arguments?.getString("date").toString()

        val itemScreenViewModelFactory = ItemScreenViewModelFactory(dataSourceJoined,dataSourceHabit, application)
        itemScreenViewModel = ViewModelProviders.of(this, itemScreenViewModelFactory).get(ItemScreenViewModel::class.java)
        binding.itemScreenViewModel = itemScreenViewModel

        itemScreenViewModel.getJoinedData(id!!, currentDate)

        binding.creatingDate.text = "Started: " + itemScreenViewModel.firstEntry
        binding.entryNumber.text = itemScreenViewModel.allEntries.toInt().toString()
        binding.monthlyNumber.text = itemScreenViewModel.monthEntries.toString()
        binding.avgNumber.text = "%.2f".format(itemScreenViewModel.monthAverage)


        val lineChart : LineChart = binding.itemLinechart
        val itemDataSet : LineDataSet = LineDataSet(itemScreenViewModel.lineArray, "Item")
        itemDataSet.setDrawValues(true)
        itemDataSet.valueTextSize = 15f
        itemDataSet.lineWidth = 5f
        itemDataSet.color = color!!
        itemDataSet.enableDashedLine(12f, 12f, 0f)
        itemDataSet.circleRadius = 8f
        itemDataSet.setCircleColor(color!!)
        val itemData: LineData = LineData(itemDataSet)
        lineChart.data = itemData
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.legend.isEnabled = false
        lineChart.description.isEnabled = false
        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.isEnabled = false
        lineChart.xAxis.setLabelCount(itemScreenViewModel.monthCounterLineChart)
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(itemScreenViewModel.labelArray)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.textSize = 12f
        println("CCC")
        lineChart.invalidate()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.actionbaritem_menu, menu)
//        activity?.custom_toolbar?.setBackgroundColor(color!!)
//        activity?.custom_toolbar?.setTitle(name)
        activity?.window!!.statusBarColor = color!!
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.title.isNullOrEmpty()){
            return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                    || super.onOptionsItemSelected(item)
        }

        else if (item.title!! == "Edit"){
            view?.findNavController()?.navigate(R.id.action_itemScreen_to_editHabbitScreen2, bundle)
            return false
        }

        else if(item.title == "Delete"){
            itemScreenViewModel.deleteItem(id!!)
            view?.findNavController()?.navigate(R.id.action_itemScreen_to_mainScreen)
            return false
        }
        return false


    }
}