package com.example.bitfit


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "DetailActivity"

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.food_input)

        val foodName:EditText = findViewById(R.id.foodName_entry)
        val calories:EditText = findViewById(R.id.calorieAmount_entry)
        val recordInputButton: Button = findViewById<Button>(R.id.AddItemButton)

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate: String = current.format(formatter).toString()
        recordInputButton.setOnClickListener(View.OnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("result", Food(date =formattedDate, foodName = foodName.text.toString(), totalCalories = calories.text.toString()))
            setResult(RESULT_OK, resultIntent)
            finish()
        })
    }
}