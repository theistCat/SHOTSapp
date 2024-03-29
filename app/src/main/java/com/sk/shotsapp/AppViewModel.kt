package com.sk.shotsapp

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginViewModel"

@HiltViewModel
class AppViewModel @Inject constructor() : ViewModel() {

    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    private val _isCheck = mutableStateOf(false)
    val isCheck: State<Boolean> = _isCheck

    private val _error = mutableStateOf("")
    val error: State<String> = _error

    private val _userEmail = mutableStateOf("")
    val userEmail: State<String> = _userEmail

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _usersName = mutableStateOf("")
    val usersName: State<String> = _usersName

    private val _usersAge = mutableStateOf("")
    val usersAge: State<String> = _usersAge

    private val _gotLoc = mutableStateOf(LatLng(0.0, 0.0))
    val gotLoc: State<LatLng> = _gotLoc

    // Setters

    fun setLoc(gotLoc: LatLng) {
        _gotLoc.value = gotLoc
    }

    fun setUserEmail(email: String) {
        _userEmail.value = email
    }

    fun setIsCheck(isCheck: Boolean) {
        _isCheck.value = isCheck
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setUsersName(usersName: String) {
        _usersName.value = usersName
    }

    fun setUsersAge(usersAge: String) {
        _usersAge.value = usersAge
    }

    fun setError(error: String) {
        _error.value = error
    }

    init {
        _isLoggedIn.value = getCurrentUser() != null
    }

    fun createUserWithEmailAndPassword() = viewModelScope.launch {
        _error.value = ""
        Firebase.auth.createUserWithEmailAndPassword(userEmail.value, password.value)
            .addOnCompleteListener { task -> signInCompletedTask(task) }
    }

    fun signInWithEmailAndPassword() = viewModelScope.launch {
        try {
            _error.value = ""
            Firebase.auth.signInWithEmailAndPassword(userEmail.value, password.value)
                .addOnCompleteListener { task -> signInCompletedTask(task) }
        } catch (e: Exception) {
            _error.value = e.localizedMessage ?: "Unknown error"
            Log.d(TAG, "Sign in fail: $e")
        }
    }

    fun signInWithGoogleToken(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        signWithCredential(credential)
    }

    private fun signWithCredential(credential: AuthCredential) = viewModelScope.launch {
        try {
            _error.value = ""
            Firebase.auth.signInWithCredential(credential)
                .addOnCompleteListener { task -> signInCompletedTask(task) }
        } catch (e: Exception) {
            _error.value = e.localizedMessage ?: "Unknown error"
        }
    }

    private fun signInCompletedTask(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            Log.d(TAG, "SignInWithEmail:success")
            _userEmail.value = ""
            _password.value = ""

        } else {
            _error.value = task.exception?.localizedMessage ?: "Unknown error"
            // If sign in fails, display a message to the user.
            Log.w(TAG, "SignInWithEmail:failure", task.exception)
        }
        viewModelScope.launch {
            _isLoggedIn.value = getCurrentUser() != null
        }

    }

    private fun getCurrentUser(): FirebaseUser? {
        val user = Firebase.auth.currentUser
        Log.d(TAG, "user display name: ${user?.displayName}, email: ${user?.email}")
        return user
    }

    fun isValidEmailAndPassword(): Boolean {
        if (userEmail.value.isBlank() || password.value.isBlank()) {
            return false
        }
        return true
    }

    fun signOut() = viewModelScope.launch {
        Firebase.auth.signOut()
        _isLoggedIn.value = false
    }

    private val _db = Firebase.firestore
    var db: FirebaseFirestore = _db

    private val _doc = mutableListOf<String>()
    val doc: MutableList<String> = _doc

    private val _eventID = mutableListOf<String>()
    val eventId: MutableList<String> = _eventID

    private val _photoUrl = mutableListOf<String>()
    val photoUrl: MutableList<String> = _photoUrl

    private val _uids = mutableListOf<String>()
    val uids: MutableList<String> = _uids

    private val _nn = String()
    var nn: String = _nn

    private val _dd = String()
    var dd: String = _dd

    private val _lat = mutableListOf(0.0)
    var lat: MutableList<kotlin.Double> = _lat

    private val _lon = mutableListOf(0.0)
    var lon: MutableList<kotlin.Double> = _lon

    private val _isError = mutableStateOf(false)
    var isError: MutableState<Boolean> = _isError

    private val _isReady = mutableStateOf(false)
    var isReady: MutableState<Boolean> = _isReady

    private val _isBottomBarEnabled = mutableStateOf(false)
    var isBottomBarEnabled: MutableState<Boolean> = _isBottomBarEnabled

}
