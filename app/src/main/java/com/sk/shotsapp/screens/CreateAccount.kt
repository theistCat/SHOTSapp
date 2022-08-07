package com.sk.shotsapp.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.R
import com.sk.shotsapp.ui.theme.BarColor
import com.sk.shotsapp.ui.theme.MyTypography
import java.util.*

@Composable
fun CreateAccount(viewModel: AppViewModel, navController: NavController) {

    viewModel.isBottomBarEnabled.value = false
    Scaffold(topBar = { Title(whichScreen = "Create Account") }) {
        val route = "Welcome"

        Column {
            DisplayName(viewModel = viewModel)
//            EmailField(viewModel = viewModel)
            PasswordFieldChange(viewModel = viewModel)
//        PasswordFieldChangeRetype(viewModel = viewModel)
//            PasswordField(viewModel = viewModel)
            DatePickerView()
//            SaveProfile(viewModel = viewModel, navController = navController)
            NextBtn {
                if (viewModel.fromScreen == "login") {
                    viewModel.signInWithEmailAndPassword()
                } else {
                    viewModel.createUserWithEmailAndPassword()
                }
                navController.navigate(route)
            }
        }
    }
}

@Composable
fun NextBtn(action: () -> Unit) {
    Button(
        onClick = action,
        colors = ButtonDefaults.buttonColors(backgroundColor = BarColor, contentColor = Color.White)
    ) {
        Text(text = "Next", fontStyle = MyTypography.h5.fontStyle)
    }
}

@Composable
fun DatePickerView() {
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

    // Declaring a string value to
    // store date in string format
    val mDate = remember { mutableStateOf("") }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        LocalContext.current, { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
        }, mYear, mMonth, mDay
    )

    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.TopStart)
        .padding(top = 10.dp)
        .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
        .clickable {
            mDatePickerDialog.show()
        }) {
        Text(
            text = mDate.value ?: "Date Picker",
            color = BarColor,
            modifier = Modifier.fillMaxWidth()
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_calendar),
            contentDescription = null,
            modifier = Modifier.size(20.dp, 20.dp),
            tint = BarColor
        )


    }
}