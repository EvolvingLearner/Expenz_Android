package com.money.expenz.ui


import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.money.expenz.R
import com.money.expenz.data.ExpenzAppBar.ExpenzTheme
import java.util.*

@Composable
fun AddScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp)
    ) {
        setUpViews(navController)
    }
}

@Composable
fun setUpViews(navController: NavController) {
    val radioOptions = listOf("Income", "Expense", "Subscription")
    var (selectedOption, onOptionSelected) = remember {
        mutableStateOf(radioOptions[2])
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        radioOptions.forEach { text ->
            Row(modifier = Modifier
                .selectable(
                    selected = (selectedOption == text),
                    onClick = { onOptionSelected(text) }
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text) })
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 2.dp),
                    color = ExpenzTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
    setCategory()
    Spacer(modifier = Modifier.width(20.dp))
    setAmount()
    Spacer(modifier = Modifier.width(20.dp))
    setDate()
    Spacer(modifier = Modifier.width(20.dp))
    addNotes()

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter),
            onClick = { navController.navigate(BottomNavItem.Home.route) },
            colors = ButtonDefaults.buttonColors(containerColor = ExpenzTheme.colorScheme.primaryContainer),
            shape = CutCornerShape(10)
        ) {
            Text(
                text = stringResource(id = R.string.add),
                color = ExpenzTheme.colorScheme.onSurfaceVariant,
                style = ExpenzTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun setCategory() {

    var isExpanded by remember {
        mutableStateOf(false)
    }
    var selectedCategory by remember {
        mutableStateOf("")
    }
    val categories = listOf(
        "Media",
        "Electricity",
        "Travel",
        "Food",
        "Shopping",
        "Gas",
        "Internet",
        "Medical",
        "Pets",
        "Others"
    )

    val icon = if (isExpanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp
    Column(
        modifier = Modifier
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        OutlinedTextField(
            value = selectedCategory,
            onValueChange = { selectedCategory = it },
            trailingIcon = { Icon(icon, "", Modifier.clickable { isExpanded = !isExpanded }) },
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.category)) }
        )

        DropdownMenu(
            modifier = Modifier.padding(5.dp),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(text = category) },
                    onClick = {
                        selectedCategory = category
                        isExpanded = false
                    })

            }
        }
    }
}

@Composable
fun setAmount() {
    var amount by remember {
        mutableStateOf("")
    }
    val textState = remember { mutableStateOf(TextFieldValue()) }
    TextField(value = amount, onValueChange = { amount = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        label = {
            Text(text = stringResource(id = R.string.amount))
        },
        placeholder = { Text(text = stringResource(id = R.string.enter_amount)) }
    )
    textState.value = TextFieldValue(amount)
}


@Composable
fun setDate() {
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
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDay: Int ->
            mDate.value = "$mDay/${mMonth + 1}/$mYear"
        },
        mYear,
        mMonth,
        mDay
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        val textState = remember { mutableStateOf(TextFieldValue()) }

        // click displays/shows the DatePickerDialog
        ReadonlyTextField(value = textState.value,
            onValueChange = { textState.value = it },
            onClick = {
                mDatePickerDialog.show()
            },
            label = {
                Text(text = "Date")
            })
        textState.value = TextFieldValue(mDate.value)
    }
}

@Composable
fun ReadonlyTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: @Composable () -> Unit
) {
    Box {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            label = label
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(onClick = onClick),
        )
    }
}

@Composable
fun addNotes() {
    var notes by remember {
        mutableStateOf("")
    }
    val textState = remember { mutableStateOf(TextFieldValue()) }
    TextField(value = notes, onValueChange = { if (notes.length <= 100) notes = it },
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(5.dp),
        label = {
            Text(text = stringResource(id = R.string.notes))
        },
        placeholder = { Text(text = stringResource(id = R.string.any_notes)) }
    )
    textState.value = TextFieldValue(notes)
}

@Preview
@Composable
fun DefaultPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp)
    ) {
        setUpViews(rememberNavController())
    }
}