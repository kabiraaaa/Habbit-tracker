package com.example.habbit

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habbit.databinding.MainScreenBinding

class MainScreen: Fragment(){

    private lateinit var binding: MainScreenBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreference : SharedPreferences
    var boxChecked : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //var mainColor = ColorDrawable(Color.BLUE)
        //(activity as AppCompatActivity).setSupportActionBar(custom_toolbar)
        //(activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(mainColor)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_screen, container, false)

        val firstRun = activity?.getPreferences(Context.MODE_PRIVATE)
        sharedPreference = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val application = requireNotNull(this.activity).application
        val base = HabitDatabase.getInstance(application)
        val dataSourceHabit = base!!.habitDAO
        val dataSourceDay = base!!.dayDAO
//        activity?.custom_toolbar?.setBackgroundColor(ContextCompat.getColor(context!!,R.color.app_bar_color))
        activity?.window!!.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
        linearLayoutManager = LinearLayoutManager(context)

        recyclerView = binding.mainRecycler.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
        }

        //val itemDecor = DividerItemDecoration(recyclerView.context, linearLayoutManager.orientation)
        //itemDecor.setOrientation(1)
        //recyclerView.addItemDecoration(itemDecor)
        val mainViewModelFactory = MainViewModelFactory(dataSourceHabit, dataSourceDay, base, application)
        mainViewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel::class.java)
        binding.mainViewModel = mainViewModel

        //check if first launch
        if (firstRun!!.getBoolean("first_run", true)) {
            firstRun.edit().putBoolean("first_run", false).apply()
            sharedPreference!!.edit().putString("lastDate", mainViewModel.currentDay.value.toString()).apply()
        }

        mainViewModel.habitList.observe(viewLifecycleOwner, Observer { newList ->
            recyclerView.adapter = MainAdapter(newList, this)
        })

        mainViewModel.currentDay.observe(viewLifecycleOwner, Observer { newDay ->
            binding.dateText.text = newDay
            if (!newDay.equals(sharedPreference?.getString("lastDate", "null"))){
                mainViewModel.dayChanged()
                sharedPreference!!.edit().putString("lastDate", newDay).apply()
            }
        })

        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
        inflater.inflate(R.menu.actionbar_menu, menu)

        //(activity as AppCompatActivity).setSupportActionBar(custom_toolbar)
        //(activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(mainColor)
        //activity?.actionBar?.setBackgroundDrawable(mainColor)
    }

    //override fun onPrepareOptionsMenu(menu: Menu) {
        //super.onPrepareOptionsMenu(menu)
        //menu.findItem(R.id.editHabbitScreen).setVisible(true)
        //menu.findItem(R.id.deleteHabbit).setVisible(true)
        //menu.findItem(R.id.addHabbitScreen).setVisible(false)
    //}

    fun onItemSelect(id: Long, color: Int, name: String, tracking: Boolean) {
        val itemFragment = ItemScreen()
        val bundle = Bundle()
        bundle.putLong("id", id)
        bundle.putInt("color", color)
        bundle.putString("name", name)
        bundle.putBoolean("tracking", tracking)
        bundle.putString("date", binding.dateText.text.toString())
        itemFragment.arguments = bundle
        view?.findNavController()?.navigate(R.id.action_mainScreen_to_itemScreen, bundle)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.title?.equals("Clear Data") == true){
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.clear_screen, null)
            val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            val application = requireNotNull(this.activity).application
//            mDialogView.button.setOnClickListener {
//                mainViewModel.deleteAll()
//                sharedPreference.edit().remove("lastDay")
//                sharedPreference!!.edit().putString("lastDate", binding.dateText.text.toString()).apply()
//                mAlertDialog.dismiss()
//            }
//            mDialogView.button2.setOnClickListener {
//                mAlertDialog.dismiss()
//            }
            return false
        }
        else{
            return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)}
    }

    fun checkingBox(id : Long) {
        if (boxChecked) {
            mainViewModel.onBoxChecking(id)
        }
        else {
            mainViewModel.onBoxUnchecking(id)
        }
    }

    override fun onResume() {
        super.onResume()
    }
}


