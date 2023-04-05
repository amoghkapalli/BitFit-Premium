package com.example.bitfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private var totalCaloriesAte: Double = 0.0
const val AVERAGE: String = "AVERAGE"
const val MINIMUM: String = "MINIMUM"
const val MAXIMUM: String = "MAXIMUM"
class MainActivity : AppCompatActivity(), DashboardFragment.OnClearListener{
    lateinit var entriesFragment: EntriesFragment
    lateinit var dashboardFragment: DashboardFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        entriesFragment = EntriesFragment()
        dashboardFragment = DashboardFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, entriesFragment, "entries_fragment").commit()

        val addNutrition = findViewById<Button>(R.id.addNewNutritionButton)
        addNutrition.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivityForResult(intent, 1)
        })
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.logs_menu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, entriesFragment, "entries_fragment").commit()
                    true
                }
                R.id.dashboard_menu -> {
                    var (average, min, max) = calculateDashboardValues()
                    val bundle = Bundle()
                    bundle.putString(AVERAGE, average.toString())
                    bundle.putString(MINIMUM, min.toString())
                    bundle.putString(MAXIMUM, max.toString())
                    dashboardFragment.arguments = bundle
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, dashboardFragment, "dashboard_fragment").commit()
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    private fun calculateDashboardValues(): Triple<Int, Int, Int> {
        var nutritionCalories: ArrayList<Int> = ArrayList()
        for (nutrition in entriesFragment.foodList) {
            nutrition.totalCalories?.toInt()?.let { nutritionCalories.add(it) }
        }

        if (nutritionCalories.size == 0) {
            return Triple(0, 0, 0)
        }

        return Triple(
            nutritionCalories.average().toInt(),
            nutritionCalories.min(),
            nutritionCalories.max()
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val foodResult = data?.getSerializableExtra("result") as Food
                entriesFragment.foodList.add(foodResult)
                entriesFragment.foodAdapter.notifyDataSetChanged()

                if (supportFragmentManager.findFragmentByTag("dashboard_fragment")?.isVisible == true) {
                    var (average, min, max) = calculateDashboardValues()

                    dashboardFragment.updateDashboard(
                        average.toString(),
                        min.toString(),
                        max.toString()
                    )
                }
                //data added to the DB
                lifecycleScope.launch(IO) {
                    (application as FoodApplication).db.FoodDao().insert(
                        FoodEntity(
                            date=foodResult.date,
                            foodName = foodResult.foodName,
                            totalCalories = foodResult.totalCalories
                        )
                    )
                }
            }
        }
    }

    override fun onClearData() {
        entriesFragment.foodList.clear()
        entriesFragment.foodAdapter.notifyDataSetChanged()
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