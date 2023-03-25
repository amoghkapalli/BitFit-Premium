package com.example.bitfit

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

const val ARTICLE_EXTRA = "ARTICLE_EXTRA"
private const val TAG = "ArticleAdapter"

class FoodAdapter(private val context: Context, private val articles: List<Food>) :
    RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.food_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount() = articles.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val foods = itemView.findViewById<TextView>(R.id.tv_foodName)
        private val dat = itemView.findViewById<TextView>(R.id.tv_date)
        private val calories = itemView.findViewById<TextView>(R.id.tv_calories)

        fun bind(article: Food) {
            foods.text= article.foodName
            dat.text = article.date
            calories.text= article.totalCalories.toString()

        }

    }
}