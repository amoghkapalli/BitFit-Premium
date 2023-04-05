package com.example.bitfit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {
    private var averageARG: String? = null
    private var minimumARG: String? = null
    private var maximumARG: String? = null

    lateinit var averageVal: TextView
    lateinit var minVal: TextView
    lateinit var maxVal: TextView

    private var listener: OnClearListener? = null


    interface OnClearListener {
        fun onClearData()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnClearListener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            averageARG = it.getString(AVERAGE)
            minimumARG = it.getString(MINIMUM)
            maximumARG = it.getString(MAXIMUM)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_dashboard, container, false)
        averageVal = view.findViewById<TextView>(R.id.averageValueTV)
        minVal = view.findViewById<TextView>(R.id.minValueTV)
        maxVal = view.findViewById<TextView>(R.id.maxValueTV)

        updateDashboard(averageARG?:"0", minimumARG ?: "0", maximumARG ?: "0")

        val clearDataBtn = view.findViewById<Button>(R.id.clearButton)
        // Deletes all records, updates the dashboard and notifies the main activity to notify the bitfit adapter
        clearDataBtn.setOnClickListener(View.OnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                (activity?.application as FoodApplication).db.FoodDao().deleteAll()
                listener?.onClearData()
            }
            updateDashboard("0","0","0")
        })

        return view
    }


    fun updateDashboard(average: String, minimum: String, maximum: String) {
        averageVal.text = average
        minVal.text = minimum
        maxVal.text = maximum
    }

    companion object {
        @JvmStatic
        fun newInstance(average: String, minimum: String, maximum: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(AVERAGE, average)
                    putString(MINIMUM, minimum)
                    putString(MAXIMUM, maximum)
                }
            }
    }
}