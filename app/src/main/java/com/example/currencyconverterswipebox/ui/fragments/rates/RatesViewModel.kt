package com.example.currencyconverterswipebox.ui.fragments.rates

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverterswipebox.network.ApiState
import com.example.currencyconverterswipebox.repository.RatesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatesViewModel  @Inject constructor(private val ratesRepository: RatesRepository): ViewModel() {


    private val ratesFlowMutable: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)

    val ratesStateFlow: StateFlow<ApiState> = ratesFlowMutable

    fun getExchangeRates(currencyCode: String) = viewModelScope.launch(Dispatchers.Default) {
        ratesRepository.getExchangeRates(currencyCode)
            .onStart {
            }
            .catch { e ->

            }.collect { exchangeRates ->
                ratesFlowMutable.value = ApiState.Success(exchangeRates)
                }
            }

}