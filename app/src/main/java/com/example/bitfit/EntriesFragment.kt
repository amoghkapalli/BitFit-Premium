package com.example.bitfit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EntriesFragment : Fragment() {
    private lateinit var foodRV: RecyclerView
    lateinit var foodAdapter: FoodAdapter
    val foodList = mutableListOf<Food>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_entries, container, false)

        // Setup the recycler view and it's data
        foodRV = view.findViewById(R.id.foodView)
        foodAdapter = FoodAdapter(view.context, foodList)
        foodRV.adapter = foodAdapter
        foodRV.layoutManager = LinearLayoutManager(view.context).also {
            val dividerItemDecorator = DividerItemDecoration(view.context, it.orientation)
            foodRV.addItemDecoration(dividerItemDecorator)
        }
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate: String = current.format(formatter).toString()
        lifecycleScope.launch{
            (activity?.application as FoodApplication).db.FoodDao().getAll().collect{
                    databaseList -> databaseList.map { entity ->
                Food(
                    entity.date,
                    entity.foodName,
                    entity.totalCalories
                )
            }.also { mappedList ->
                foodList.clear()
                foodList.addAll(mappedList)
                foodAdapter.notifyDataSetChanged() }
            }
        }

        return view
    }
}