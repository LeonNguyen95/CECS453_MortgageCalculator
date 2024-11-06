package com.example.mortgagecalculator.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MortgageCalculatorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MortgageCalculatorState())
    val uiState: StateFlow<MortgageCalculatorState> = _uiState.asStateFlow()

    fun setAmount(newAmount: Double) {
        if (newAmount >= 0) {
            _uiState.update { currentState -> currentState.copy(amount = newAmount) }
            uiState.value.amount = newAmount
        }
    }

    fun getAmount() : Double {
        return uiState.value.amount
    }

    fun setYears(newYears: Int) {
        if (newYears >= 0) {
            _uiState.update { currentState -> currentState.copy(years = newYears) }
            uiState.value.years = newYears
        }
    }
    fun getYears(): Int {
        return uiState.value.years
    }

    fun setInterestRate(newInterestRate: Double) {
        if (newInterestRate >= 0) {
            _uiState.update { currentState -> currentState.copy(interestRate = newInterestRate) }
            uiState.value.interestRate = newInterestRate
        }
    }

    fun getInterestRate(): Double {
        return uiState.value.interestRate
    }

    fun monthlyPayment(amount : Double, years :Int, interestRate: Double) : Double {
        var mRate = 0.0
        if (interestRate > 1) {
            mRate = (interestRate / 100) / 12
        } else {
            mRate = interestRate / 12
        }

        var temp = Math.pow((1 / (1 + mRate)).toDouble(), (years * 12).toDouble())
        var monthlyPayment = amount * mRate / (1 - temp).toDouble()

        _uiState.update { currentState -> currentState.copy(monthlyPayment = monthlyPayment) }
        uiState.value.monthlyPayment = monthlyPayment

        return (_uiState.value.monthlyPayment).toDouble()
    }

    fun totalPayment() : Double{
        var totalPayment = uiState.value.monthlyPayment * uiState.value.years * 12
        _uiState.update { currentState -> currentState.copy(totalPayment = totalPayment) }
        uiState.value.totalPayment = totalPayment

        return totalPayment.toDouble()
    }
}