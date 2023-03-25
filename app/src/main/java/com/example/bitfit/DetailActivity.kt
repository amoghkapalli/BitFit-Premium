package com.example.bitfit


import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

private const val TAG = "DetailActivity"

class DetailActivity : AppCompatActivity() {

    private lateinit var foodName: TextView
    private lateinit var date: TextView
    private lateinit var calories: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.food_card)

        foodName = findViewById(R.id.tv_foodName)
        date = findViewById(R.id.tv_date)
        calories = findViewById(R.id.tv_calories)


        val article = intent.getSerializableExtra(ARTICLE_EXTRA) as Food

        // Set title and abstract information for the article
        foodName.text = article.foodName
        date.text = article.date
        calories.text = article.totalCalories.toString()
    }
}