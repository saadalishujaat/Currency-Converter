package com.example.currencyconverterswipebox.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverterswipebox.R


class RatesAdapter(private val onClick: (Pair<String,Double>) -> Unit) :
    ListAdapter<Pair<String,Double>, RatesAdapter.RatesViewHolder>(RatesDiffCallback) {

    private var amount: Double = 0.0

    /* ViewHolder for Flower, takes in the inflated view and the onClick behavior. */
    class RatesViewHolder(itemView: View, val onClick: (Pair<String,Double>) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val currencyCode: TextView = itemView.findViewById(R.id.currency_code)
        private val conversionValue: TextView = itemView.findViewById(R.id.conversion_value)
        private val currencyTo: TextView = itemView.findViewById(R.id.currency_to)
        private val currencyFrom: TextView = itemView.findViewById(R.id.currency_from)
        private var resultRate: Pair<String,Double>? = null


        init {
            itemView.setOnClickListener {
                resultRate?.let {
                    onClick(it)
                }
            }
        }

        /* Bind exchange rate data. */
        fun bind(amount:Double, rate: Pair<String,Double>) {
            resultRate = rate

            resultRate?.let {
                currencyCode.text = it.first.toString()
                conversionValue.text = (amount * it.second).toString()


                "1 EUR = ${it.second} ${it.first}".apply {  currencyTo.text = this}
                "1 ${it.first} = ${1/it.second} EUR".apply {  currencyFrom.text = this}
            }
        }
    }

    /* Creates and inflates view and return FlowerViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.exchange_rates_item, parent, false)
        return RatesViewHolder(view, onClick)
    }

    /* Gets current flower and uses it to bind view. */
    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        val rate = getItem(position)
        holder.bind(amount , rate)
    }

    fun setAmount(no: Double){
        amount = no
        notifyDataSetChanged()
    }
}

object RatesDiffCallback : DiffUtil.ItemCallback<Pair<String,Double>>() {
    override fun areItemsTheSame(oldItem: Pair<String,Double>, newItem: Pair<String,Double>): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Pair<String,Double>, newItem: Pair<String,Double>): Boolean {
        return oldItem.first == newItem.first
    }
}