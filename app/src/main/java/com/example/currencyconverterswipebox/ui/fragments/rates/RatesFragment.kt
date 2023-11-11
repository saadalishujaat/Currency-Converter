package com.example.currencyconverterswipebox.ui.fragments.rates

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.currencyconverterswipebox.databinding.FragmentRatesBinding
import com.example.currencyconverterswipebox.network.ApiState
import com.example.currencyconverterswipebox.ui.adapters.RatesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RatesFragment : Fragment() {

    private var _binding: FragmentRatesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val ratesViewModel: RatesViewModel by activityViewModels()

    private lateinit var ratesAdapter: RatesAdapter

    /**Override**/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentRatesBinding.inflate(inflater, container, false)

        setupAdapter()

        binding.amountEt.doOnTextChanged { text, start, before, count ->
            text?.let { amount ->
                if (amount.isNotEmpty()) {
                    if (::ratesAdapter.isInitialized) {
                        ratesAdapter.setAmount(amount.toString().toDouble())
                    } else {
                        setupAdapter()
                    }
                }else{
                    ratesAdapter.setAmount(0.0)
                }
            }
        }



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /**Functions**/


    private fun setupAdapter() {
        if (!::ratesAdapter.isInitialized) {
            ratesAdapter = RatesAdapter {
                // on click callback from adapter item
            }
        }
        binding.ratesRv.adapter = ratesAdapter
        observers()

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
                        ratesAdapter.submitList(state.exchangeRates.conversionRates.toList())
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

}