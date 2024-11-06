package com.example.mortgagecalculator.ui

data class MortgageCalculatorState(
    var amount: Double = 500000.00,
    var years: Int = 30,
    var interestRate: Double = 0.05,
    var monthlyPayment: Double = 0.00,
    var totalPayment: Double = 0.00
)
