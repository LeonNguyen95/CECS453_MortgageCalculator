package com.example.mortgagecalculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mortgagecalculator.R
import java.text.NumberFormat
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.DecimalFormat

val mediumPadding = 16.dp
val contentTextSize = 25.sp
val spacerAmount = 60.dp
val spacerInterestRate = 8.dp

@Composable
fun MortgageCalculatorApp(
    modifier: Modifier,
    mortgageCalculatorViewModel: MortgageCalculatorViewModel = viewModel()
) {
    var amount = mortgageCalculatorViewModel.getAmount()
    var years = mortgageCalculatorViewModel.getYears()
    var interestRate = mortgageCalculatorViewModel.getInterestRate()

    var showResultScreen by remember { mutableStateOf(false) }

    if (showResultScreen) {
        ResultScreen(
            amount = amount,
            years = years,
            interestRate = interestRate,
            onModifyDataClick = {
                showResultScreen = false
            }
        )
    } else {
        DataScreen(
            onDoneClick = {
                showResultScreen = true
            }
        )
    }
}

@Composable
fun ResultScreen(
    amount : Double = 0.0,
    years : Int = 30,
    interestRate : Double = 0.0, onModifyDataClick: () -> Unit = {},
    mortgageCalculatorViewModel: MortgageCalculatorViewModel = viewModel()
) {
    var monthlyPayment = mortgageCalculatorViewModel.monthlyPayment(amount, years, interestRate)
    var totalPayment = mortgageCalculatorViewModel.totalPayment()

    val firstColumnElements = listOf(
        stringResource(R.string.amount),
        stringResource(R.string.years),
        stringResource(R.string.interest_rate),
        stringResource(R.string.monthly_payment),
        stringResource(R.string.total_payment)
    )
    val secondColumnElements = listOf(
        formatCurrency(amount),
        years,
        formatPercentage(interestRate),
        formatCurrency(monthlyPayment),
        formatCurrency(totalPayment))

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(mediumPadding)
            .systemBarsPadding(), // This adds padding for the status bar and navigation bar
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF9C27B0)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "MortgageV0",
                style = MaterialTheme.typography.headlineLarge.merge(),
                color = Color.White,
                modifier = Modifier.padding(mediumPadding)
            )
        }

        // Data
        for (i in firstColumnElements.indices) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // first column
                Text (
                    text = firstColumnElements[i],
                    style = MaterialTheme.typography.displaySmall.merge(
                        TextStyle(fontSize = contentTextSize)
                    )
                )

                // second column
                Text (
                    text = secondColumnElements[i].toString(),
                    style = MaterialTheme.typography.displaySmall.merge(
                        TextStyle(fontSize = contentTextSize)
                    )
                )
            }

            // Add a red horizontal line to separate input and result
            if (i == 2) {
                HorizontalDivider(
                    color = Color.Red,
                    thickness = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
        }

        // Modify Data Button
        Button(
            onClick = { onModifyDataClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9C27B0),
                contentColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "MODIFY DATA",
                style = MaterialTheme.typography.headlineSmall.merge()
            )
        }
    }

}

@Composable
fun DataScreen(
    onDoneClick: () -> Unit = {},
    mortgageCalculatorViewModel: MortgageCalculatorViewModel = viewModel()
) {
    var amount by remember { mutableStateOf(mortgageCalculatorViewModel.getAmount().toString())}
    var interestRate by remember { mutableStateOf(mortgageCalculatorViewModel.getInterestRate().toString())}

    var formattedAmount by remember { mutableStateOf(formatWithCommas(mortgageCalculatorViewModel.getAmount())) }

    val radioOptions = listOf(10, 15, 30)
    val (selectedYear, onYearSelected) = remember { mutableIntStateOf(radioOptions[2]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(mediumPadding)
            .systemBarsPadding(), // This adds padding for the status bar and navigation bar
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Title (First Row)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF9C27B0)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "MortgageV0",
                style = MaterialTheme.typography.headlineLarge.merge(),
                color = Color.White,
                modifier = Modifier.padding(mediumPadding)
            )
        }

        // Years and 3 radio buttons (Second Row)
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text: Years
            Text(
                text = stringResource(R.string.years),
                style = MaterialTheme.typography.displaySmall.merge(
                    TextStyle(fontSize = contentTextSize)
                )
            )

            // Radio Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                radioOptions.forEach { year ->
                    Row(
                        modifier = Modifier
                            .selectable(
                                selected = (year == selectedYear),
                                onClick = { onYearSelected(year) }
                            )
                    ) {
                        RadioButton(
                            selected = (year == selectedYear),
                            onClick = {
                                onYearSelected(year)
                                mortgageCalculatorViewModel.setYears(year.toInt())
                            }
                        )
                        Text(
                            text = year.toString(),
                            style = MaterialTheme.typography.displaySmall.merge(
                                TextStyle(fontSize = contentTextSize)
                            )
                        )
                    }
                }
            }
        }

        // Amount (Third Row)
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            // Text: Amount
            Text(
                text = stringResource(R.string.amount),
                style = MaterialTheme.typography.displaySmall.merge(
                    TextStyle(fontSize = contentTextSize)
                )
            )
            Spacer(modifier = Modifier.width(spacerAmount))

            // Input field
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                //visualTransformation = DecimalInputVisualTransformation(decimalFormatter),
                label = { Text(text = "Enter Loan Amount")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
        }

        // Interest Rate (Fourth Row)
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            // Text: Interest Rate
            Text(
                text = stringResource(R.string.interest_rate),
                style = MaterialTheme.typography.displaySmall.merge(
                    TextStyle(fontSize = contentTextSize)
                )
            )
            Spacer(modifier = Modifier.width(spacerInterestRate))

            // Input field
            OutlinedTextField(
                value = interestRate,
                onValueChange = { interestRate = it },
                label = { Text(text = "Enter Interest Rate")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )
        }

        // Done Button (Fifth Row)
        Button(
            onClick = {
                mortgageCalculatorViewModel.setAmount(amount.toDoubleOrNull() ?: 0.0)
                mortgageCalculatorViewModel.setInterestRate(interestRate.toDouble())
                onDoneClick()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9C27B0),
                contentColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "DONE",
                style = MaterialTheme.typography.headlineSmall.merge()
            )
        }
    }
}

// Helper functions
fun formatCurrency(amount: Double) : String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    return formatter.format(amount)
}

fun formatPercentage(percentage: Double): String {
    if (percentage > 1)
        return "${percentage / 100} %"
    return "$percentage %"
}

fun formatWithCommas(amount : Double): String {
    var amountString = amount.toString()
    return if(amountString.isNotEmpty()) {
        val parseAmount = amountString.toDoubleOrNull() ?: 0.0
        val formatter = DecimalFormat("#,###")
        formatter.format(parseAmount)
    } else {
        ""
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true)
@Composable
fun GreetingPreview() {
    //ResultScreen()
    DataScreen()
}

