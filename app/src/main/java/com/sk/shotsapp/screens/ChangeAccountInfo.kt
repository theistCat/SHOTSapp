package com.sk.shotsapp.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.sk.shotsapp.AppViewModel
import com.sk.shotsapp.R
import com.sk.shotsapp.ui.theme.BarColor
import com.sk.shotsapp.ui.theme.MyTypography
import java.util.*

@Composable
fun ChangeAccountInfo(viewModel: AppViewModel = hiltViewModel(), navController: NavController) {
    viewModel.isBottomBarEnabled.value = false

    Scaffold(topBar = { Title(whichScreen = "Change Account Info") }) {
        Column {
            DisplayName(viewModel = viewModel)
            Email(viewModel = viewModel)
            PasswordField(viewModel = viewModel)
//            PasswordFieldChange(viewModel = viewModel)
//            PasswordFieldChangeRetype(viewModel = viewModel)
            DatePickerView(viewModel)
            SaveProfile(viewModel = viewModel, navController = navController)
        }
    }
}

@Composable
fun Email(viewModel: AppViewModel) {
//    val focusManager = LocalFocusManager.current
//    val userEmail = viewModel.userEmail.value

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = Firebase.auth.currentUser?.email.toString(),
        label = { Text(text = stringResource(R.string.email)) },
        onValueChange = { viewModel.setUserEmail(it) },
        enabled = false,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedLabelColor = BarColor,
            cursorColor = BarColor,
            focusedIndicatorColor = BarColor,
            backgroundColor = Color.White
        ),
    )
}

@Composable
fun DisplayName(viewModel: AppViewModel) {
//    val focusManager = LocalFocusManager.current
    val usersName = viewModel.usersName.value
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = usersName,
        label = { Text(text = "Name") },
        onValueChange = { viewModel.setUsersName(it) },
        singleLine = true,
        isError = viewModel.usersName.value.isEmpty() && viewModel.usersName.value == "",
        colors = TextFieldDefaults.textFieldColors(
            focusedLabelColor = BarColor,
            cursorColor = BarColor,
            focusedIndicatorColor = BarColor,
            backgroundColor = Color.White
        ),
    )
}

@Composable
fun DatePickerView(viewModel: AppViewModel) {
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
//            viewModel.setUsersAge((Calendar.getInstance().get(Calendar.YEAR) - mYear).toString())
        }, mYear, mMonth, mDay
    )



    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.TopStart)
        .padding(top = 10.dp)
        .clickable {
            mDatePickerDialog.show()
        }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            viewModel.setUsersAge(mDate.value)
            Text(
                text = "Date Picker: ${mDate.value}",
                color = BarColor,
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = null,
                modifier = Modifier.size(20.dp, 20.dp),
                tint = BarColor
            )
        }
    }

    IconToggleButton(checked = viewModel.isCheck.value,
        onCheckedChange = { viewModel.setIsCheck(!viewModel.isCheck.value) })
    {
        Icon(
            painter = painterResource(if (viewModel.isCheck.value) R.drawable.man else R.drawable.woman),
            modifier = Modifier.size(150.dp),
            contentDescription = "Radio button icon",
            tint = BarColor
        )
    }

}

@Composable
fun SaveProfile(viewModel: AppViewModel, navController: NavController) {
    Button(
        onClick = {
            if (viewModel.usersName.value != "") {
                viewModel.isError.value = false
                Firebase.auth.currentUser?.updateProfile(userProfileChangeRequest {
                    displayName = viewModel.usersName.value
                })?.addOnSuccessListener {
//                    navController.navigate("profile")
                }
            }
            if (viewModel.password.value.isNotEmpty()) {
                viewModel.isError.value = false
                Firebase.auth.currentUser?.updatePassword(viewModel.password.value)
                    ?.addOnSuccessListener {}
            } else viewModel.isError.value = true
            val newUser = hashMapOf(
                "email" to Firebase.auth.currentUser?.email,
                "name" to viewModel.usersName.value,
                "age" to viewModel.usersAge.value,
                "sex" to "U",
            )
            viewModel.db.collection("users").add(newUser).addOnSuccessListener {
                navController.navigate("profile")
            }
        },
        enabled = false,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = BarColor,
            contentColor = Color.White
        )
    ) {
        Text(
            text = "Change account info",
            modifier = Modifier.padding(end = 4.dp),
            fontStyle = MyTypography.h5.fontStyle
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_edit),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }
}