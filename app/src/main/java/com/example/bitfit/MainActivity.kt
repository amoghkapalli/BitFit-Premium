package com.example.bitfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var nutritionRV: RecyclerView
    private lateinit var nutritionAdapter: FoodAdapter
    private val nutrition = mutableListOf<Food>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nutritionRV = findViewById(R.id.foodListView)
        nutritionAdapter = FoodAdapter(this, nutrition)
        nutritionRV.adapter = nutritionAdapter
        nutritionRV.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecorator = DividerItemDecoration(this, it.orientation)
            nutritionRV.addItemDecoration(dividerItemDecorator)
        }
        lifecycleScope.launch{
            (application as FoodApplication).db.FoodDao().getAll().collect{
                    databaseList -> databaseList.map { entity ->
                Food(
                    entity.date,
                    entity.foodName,
                    entity.totalCalories
                )
            }.also { mappedList ->
                nutrition.clear()
                nutrition.addAll(mappedList)
                nutritionAdapter.notifyDataSetChanged()
            }
            }
        }
        val addNutrition = findViewById<Button>(R.id.AddItemButton)
        addNutrition.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivityForResult(intent, 1)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                //data updated in the recycler view
                val nutritionResult = data?.getSerializableExtra("result") as Food
                nutrition.add(nutritionResult)
                nutritionAdapter.notifyDataSetChanged()
                //data added to the DB
                lifecycleScope.launch(IO){
                    (application as FoodApplication).db.FoodDao().insert(
                        FoodEntity(
                            date=nutritionResult.date,
                            foodName = nutritionResult.foodName,
                            totalCalories = nutritionResult.totalCalories)
                    )
                }
            }
        }
    }
    /*
        binding?.btnDelete?.setOnClickListener {
            totalCaloriesAte = 0.0
            lifecycleScope.launch(IO) {
                (application as FoodApplication).db.foodDao().deleteAll()
            }
            binding?.tvSummary?.text = "Total calories needed per day: 2000 kcal\\nTotal calories you ate today: $totalCaloriesAte kcal"
        }
    */

}