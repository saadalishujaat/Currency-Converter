package com.example.currencyconverterswipebox.ui.fragments.charts

import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.currencyconverterswipebox.R
import com.example.currencyconverterswipebox.databinding.FragmentChartsBinding
import com.example.currencyconverterswipebox.network.ApiState
import com.example.currencyconverterswipebox.ui.fragments.rates.RatesViewModel
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChartsFragment : Fragment() {

    private var _binding: FragmentChartsBinding? = null
    private val binding get() = _binding!!


    private val chartsViewModel: ChartsViewModel by viewModels()
    private val ratesViewModel: RatesViewModel by activityViewModels()
    private lateinit var spinnerAdapter : ArrayAdapter<String>
    private var currencyList: ArrayList<String> = arrayListOf()

    /**Override**/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChartsBinding.inflate(inflater, container, false)

        initUI()
        observers()
        setupChart()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /**Functions**/

    private fun initUI(){
        if(!::spinnerAdapter.isInitialized) {
            spinnerAdapter = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, arrayListOf())
        }
        binding.currencyTextView.setAdapter(spinnerAdapter)
    }

    private fun setupChart(){


        // background color
        binding.lineChart.setBackgroundColor(Color.WHITE)


        // disable description text
        binding.lineChart.description.isEnabled = false


        // enable touch gestures
        binding.lineChart.setTouchEnabled(true)

        // set listeners
//        binding.lineChart.setOnChartValueSelectedListener(this)
        binding.lineChart.setDrawGridBackground(false)


        // enable scaling and dragging
        binding.lineChart.isDragEnabled = true
        binding.lineChart.setScaleEnabled(/* enabled = */ true)
        // binding.lineChart.setScaleXEnabled(true);
        // binding.lineChart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        binding.lineChart.setPinchZoom(true)

        binding.lineChart.xAxis.enableGridDashedLine(10f, 10f, 0f)

        // disable dual axis (only use LEFT axis)
        binding.lineChart.axisRight.isEnabled = false

        // horizontal grid lines
        binding.lineChart.axisLeft.enableGridDashedLine(10f, 10f, 0f)
        binding.lineChart.axisLeft.axisMaximum = 200f
        binding.lineChart.axisLeft.axisMinimum = -50f

        val llXAxis = LimitLine(9f, "Index 10")
        llXAxis.lineWidth = 4f
        llXAxis.enableDashedLine(10f, 10f, 0f)
        llXAxis.labelPosition = LimitLabelPosition.RIGHT_BOTTOM
        llXAxis.textSize = 10f
        val ll1 = LimitLine(150f, "Upper Limit")
        ll1.lineWidth = 4f
        ll1.enableDashedLine(10f, 10f, 0f)
        ll1.labelPosition = LimitLabelPosition.RIGHT_TOP
        ll1.textSize = 10f
        val ll2 = LimitLine(-30f, "Lower Limit")
        ll2.lineWidth = 4f
        ll2.enableDashedLine(10f, 10f, 0f)
        ll2.labelPosition = LimitLabelPosition.RIGHT_BOTTOM
        ll2.textSize = 10f

        // draw limit lines behind data instead of on top
        binding.lineChart.axisLeft.setDrawLimitLinesBehindData(true)
        binding.lineChart.axisRight.setDrawLimitLinesBehindData(true)

        // add limit lines
        binding.lineChart.axisLeft.addLimitLine(ll1)
        binding.lineChart.axisLeft.addLimitLine(ll2)
        //xAxis.addLimitLine(llXAxis);

        setData(45, 180f)

        // draw points over time
        binding.lineChart.animateX(1500)
    }


    private fun observers() {

        ratesViewModel.getExchangeRates("EUR")
        lifecycleScope.launchWhenStarted {
            ratesViewModel.ratesStateFlow.collect { state ->
                when (state) {
                    is ApiState.Loading -> {

                    }

                    is ApiState.Success -> {
                        Log.d("testResult", "SuccessChannelsData: ${state.exchangeRates} ")
                        spinnerAdapter.addAll(state.exchangeRates.conversionRates.keys)
                    }

                    is ApiState.Failure -> {
                        binding.apply {

                            Log.d("testResult", "failed: ${state.error.toString()}\n\n")

                        }
                    }

                    is ApiState.Empty -> {
                        binding.apply {
                            Log.d("testResult", "observers: empty")
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setData(count: Int, range: Float) {
        val values = java.util.ArrayList<Entry>()
        for (i in 0 until count) {
            val `val` = (Math.random() * range).toFloat() - 30
            values.add(Entry(i.toFloat(), `val`, ContextCompat.getDrawable(requireContext() ,R.drawable.star)))
        }
        val set1: LineDataSet?
        if (binding.lineChart.data != null &&
            binding.lineChart.data.dataSetCount > 0
        ) {
            set1 = binding.lineChart.data.getDataSetByIndex(0) as LineDataSet?
            set1?.values = values
            set1?.notifyDataSetChanged()
            binding.lineChart.data.notifyDataChanged()
            binding.lineChart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")
            set1.setDrawIcons(false)

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f)

            // black lines and points
            set1.color = Color.BLACK
            set1.setCircleColor(Color.BLACK)

            // line thickness and point size
            set1.lineWidth = 1f
            set1.circleRadius = 3f

            // draw points as solid circles
            set1.setDrawCircleHole(false)

            // customize legend entry
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            // text size of values
            set1.valueTextSize = 9f

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
            set1.setDrawFilled(true)
            set1.fillFormatter = IFillFormatter { dataSet, dataProvider -> binding.lineChart.getAxisLeft().getAxisMinimum() }

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.fade_red)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.BLACK
            }
            val dataSets = java.util.ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            binding.lineChart.data = data
        }
    }
}