package com.money.expenz.ui

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.money.expenz.R
import com.money.expenz.data.IEDetails
import com.money.expenz.model.ExpenzAppBar.ExpenzTheme
import com.money.expenz.ui.home.ExpenzViewModel
import com.money.expenz.utils.ExpenzUtil.Companion.EXPENSE
import com.money.expenz.utils.ExpenzUtil.Companion.INCOME
import com.money.expenz.utils.ExpenzUtil.Companion.SUBSCRIPTION
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavController,
    viewModel: ExpenzViewModel,
) {
    val radioValue = remember { mutableStateOf("") }
    val category = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }
    val date = remember { mutableStateOf("") }
    val notes = remember { mutableStateOf("") }
    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        Button(
            modifier =
            Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter),
            onClick = {
                val ieDetails =
                    viewModel.loggedInUserId.value?.let {
                        IEDetails(
                            ie = radioValue.value,
                            category = category.value,
                            amount = amount.value.toIntOrNull() ?: 0,
                            date = date.value,
                            notes = notes.value,
                            userId = it,
                        )
                    }
                viewModel.insertIEDetails(ieDetails!!)
                viewModel.updateUserDetails(amount.value.toInt(), radioValue.value)
                navController.navigate(BottomNavItem.Home.route) {
                    popUpTo(BottomNavItem.Home.route) { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = ExpenzTheme.colorScheme.primaryContainer),
            shape = CutCornerShape(10),
        ) {
            Text(
                text = stringResource(id = R.string.add),
                color = ExpenzTheme.colorScheme.onSurfaceVariant,
                style = ExpenzTheme.typography.labelLarge,
            )
        }
    }
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp),
    ) {
        // set Radio options
        val radioOptions = listOf(INCOME, EXPENSE, SUBSCRIPTION)
        val (selectedOption, onOptionSelected) =
            remember {
                mutableStateOf(radioOptions[2])
            }
        Column(
            modifier =
            Modifier
                .fillMaxWidth(),
        ) {
            radioOptions.forEach { text ->
                Row(
                    modifier =
                    Modifier
                        .selectable(
                            selected = (selectedOption == text),
                            onClick = { onOptionSelected(text) },
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) },
                    )
                    Text(
                        text = text,
                        modifier = Modifier.padding(start = 2.dp),
                        color = ExpenzTheme.colorScheme.onSurfaceVariant,
                    )
                    radioValue.value = selectedOption
                }
            }
        }

        // Set Category
        var expanded by remember {
            mutableStateOf(false)
        }
        val categories =
            listOf(
                "Media",
                "Electricity",
                "Travel",
                "Food",
                "Shopping",
                "Gas",
                "Internet",
                "Medical",
                "Pets",
                "Salary",
                "Others",
            )
        Column(
            modifier =
            Modifier
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    value = category.value,
                    onValueChange = { category.value = it },
                    label = { Text(text = stringResource(id = R.string.category)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    categories.forEach { categorySelected ->
                        DropdownMenuItem(
                            text = { Text(text = categorySelected) },
                            onClick = {
                                category.value = categorySelected
                                expanded = false
                            },
                        )
                    }
                }
            }
        }
        // Line Space
        Spacer(modifier = Modifier.width(20.dp))

        // Set Amount
        val textFieldValue = remember { mutableStateOf(TextFieldValue()) }
        TextField(
            value = amount.value,
            onValueChange = { amount.value = it },
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(5.dp),
            keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            label = {
                Text(text = stringResource(id = R.string.amount))
            },
            placeholder = { Text(text = stringResource(id = R.string.enter_amount)) },
        )
        textFieldValue.value = TextFieldValue(amount.value)

        // Line Space
        Spacer(modifier = Modifier.width(20.dp))

        // Set Date
        val mContext = LocalContext.current
        // Declaring integer values
        // for year, month and day
        val mYear: Int
        val mMonth: Int
        val mDay: Int

        // Initializing a Calendar
        val mCalendar = Calendar.getInstance()

        // Fetching current year, month and day
        mYear = mCalendar.get(Calendar.YEAR)
        mMonth = mCalendar.get(Calendar.MONTH)
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

        mCalendar.time = Date()

        // Declaring a string value to store date in string format
        val mDate = remember { mutableStateOf("") }

        // Declaring DatePickerDialog and setting
        // initial values as current values (present year, month and day)
        val mDatePickerDialog =
            DatePickerDialog(
                mContext,
                { _: DatePicker, year: Int, month: Int, day: Int ->
                    mDate.value = "$day/${month + 1}/$year"
                },
                mYear,
                mMonth,
                mDay,
            )

        Column(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            val textState = remember { mutableStateOf(TextFieldValue()) }

            // click displays/shows the DatePickerDialog
            ReadonlyTextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                onClick = {
                    mDatePickerDialog.show()
                },
                label = {
                    Text(text = "Date")
                },
            )
            textState.value = TextFieldValue(mDate.value)
            date.value = mDate.value
        }

        // Line Space
        Spacer(modifier = Modifier.width(20.dp))

        // Set Notes
        val textStateNotes = remember { mutableStateOf(TextFieldValue()) }
        TextField(
            value = notes.value,
            onValueChange = { if (notes.value.length <= 100) notes.value = it },
            modifier =
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(5.dp),
            label = {
                Text(text = stringResource(id = R.string.notes))
            },
            placeholder = { Text(text = stringResource(id = R.string.any_notes)) },
        )
        textStateNotes.value = TextFieldValue(notes.value)
    }
}

@Composable
fun ReadonlyTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
) {
    Box {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            label = label,
        )
        Box(
            modifier =
            Modifier
                .matchParentSize()
                .clickable(onClick = onClick),
        )
    }
}

@Preview
@Composable
fun DefaultPreviewAdd() {
    val viewModel: ExpenzViewModel = viewModel()
    AddScreen(rememberNavController(), viewModel)
}
